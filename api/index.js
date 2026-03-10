const fastify = require('fastify')({ logger: true });
const cors = require('@fastify/cors');
const postgres = require('@fastify/postgres');
const websocket = require('@fastify/websocket');
const Redis = require('ioredis');
const crypto = require('crypto');
require('dotenv').config();

const PORT = process.env.PORT || 3000;
const SECRET_KEY = process.env.HMAC_SECRET_KEY || 'clau-secreta-per-defecte';

// Connexió a Redis (per a rànquings Sorted Sets)
const redis = new Redis(process.env.REDIS_URL);

// Registre de plugins
fastify.register(cors, { origin: "*" });
fastify.register(postgres, {
    connectionString: process.env.DATABASE_URL,
    ssl: { rejectUnauthorized: false }
});
fastify.register(websocket);

/**
 * Hook de seguretat HMAC
 */
fastify.decorate('validarHMAC', async (request, reply) => {
    const { puntuacio, timestamp } = request.body;
    const signaturaRebuda = request.headers['x-signature'];

    if (!puntuacio || !timestamp || !signaturaRebuda) {
        reply.code(400).send({ error: 'Falten dades de seguretat' });
        return;
    }

    const data = `${puntuacio}:${timestamp}`;
    const hash = crypto.createHmac('sha256', SECRET_KEY)
        .update(data)
        .digest('hex');

    if (hash !== signaturaRebuda) {
        reply.code(403).send({ error: 'Signatura invàlida' });
    }
});

/**
 * Lògica de Proximitat: Detectar llegendes a prop (PostGIS)
 */
fastify.get('/api/llegendes/proximitat', async (request, reply) => {
    const { lat, lon, radi = 50 } = request.query;

    if (!lat || !lon) {
        return reply.code(400).send({ error: 'Falten coordenades' });
    }

    try {
        const query = `
            SELECT id, titol, radi_activacio, dificultat, config_joc,
                   ST_Distance(posicio, ST_SetSRID(ST_MakePoint($1, $2), 4326)::geography) as distancia_metres
            FROM llegendes
            WHERE ST_DWithin(posicio, ST_SetSRID(ST_MakePoint($1, $2), 4326)::geography, $3)
            ORDER BY distancia_metres ASC
        `;
        const { rows } = await fastify.pg.query(query, [lon, lat, radi]);

        return rows.map(r => ({
            id: r.id,
            titol: r.titol,
            distancia_metres: parseFloat(r.distancia_metres),
            activa: true,
            config: r.config_joc
        }));
    } catch (err) {
        fastify.log.error(err);
        return reply.code(500).send({ error: 'Error del servidor' });
    }
});

/**
 * Obtenir una llegenda per codi (p.ex. 'MINYONA')
 */
fastify.get('/api/llegendes/:codi', async (request, reply) => {
    const { codi } = request.params;
    try {
        const query = 'SELECT id, codi, titol, config_joc, dificultat FROM llegendes WHERE codi = $1';
        const { rows } = await fastify.pg.query(query, [codi.toUpperCase()]);

        if (rows.length === 0) {
            return reply.code(404).send({ error: 'Llegenda no trobada' });
        }

        return rows[0];
    } catch (err) {
        fastify.log.error(err);
        return reply.code(500).send({ error: 'Error del servidor' });
    }
});

/**
 * Finalitzar joc: Registre en DB + Redis Update + Broadcast
 * Seguretat: Valida HMAC i Proximitat GPS (ST_DWithin)
 */
fastify.post('/api/joc/finalitzar', { preHandler: [fastify.validarHMAC] }, async (request, reply) => {
    const { jugador_id, llegenda_id, puntuacio, lat, lon } = request.body;

    if (!lat || !lon) {
        return reply.code(400).send({ error: 'Falten coordenades GPS per validar la proximitat' });
    }

    try {
        // 1. Validar proximitat real al servidor (Anti-Cheat)
        const proximityQuery = `
            SELECT id FROM llegendes 
            WHERE id = $1 AND ST_DWithin(posicio, ST_SetSRID(ST_MakePoint($2, $3), 4326)::geography, radi_activacio)
        `;
        const proximityCheck = await fastify.pg.query(proximityQuery, [llegenda_id, lon, lat]);

        if (proximityCheck.rows.length === 0) {
            return reply.code(403).send({ error: 'Validació GPS fallida: Estàs massa lluny de la llegenda' });
        }

        // 2. Persistència en PostgreSQL (Log de seguretat)
        await fastify.pg.query(
            'INSERT INTO partides (jugador_id, llegenda_id, puntuacio, guanyada, signatura_valida) VALUES ($1, $2, $3, $4, $5)',
            [jugador_id, llegenda_id, puntuacio, true, true]
        );

        // 3. Incrementar punts en Redis (Sorted Set per a rànquing d'alta velocitat)
        const nousPunts = await redis.zincrby('ranking_global', puntuacio, jugador_id);

        // 4. Obtenir nickname per al broadcast
        const { rows } = await fastify.pg.query('SELECT nickname FROM jugadors WHERE id = $1', [jugador_id]);
        const nickname = rows[0]?.nickname || 'Anònim';

        // 5. Actualitzar PostgreSQL asíncronament (per consistència)
        fastify.pg.query('UPDATE jugadors SET punts_globals = $1 WHERE id = $2', [nousPunts, jugador_id]);

        // 6. Broadcast en temps real a tots els connectats
        fastify.websocketServer.clients.forEach(client => {
            if (client.readyState === 1) { // OPEN
                client.send(JSON.stringify({
                    type: 'ACTUALITZACIO_RANKING',
                    nickname,
                    nous_punts: nousPunts,
                    missatge: `${nickname} ha superat una llegenda!`
                }));
            }
        });

        return { success: true, points: nousPunts };
    } catch (err) {
        fastify.log.error(err);
        return reply.code(500).send({ error: 'Error en processar la puntuació' });
    }
});

/**
 * Rànquing: Obtenir el TOP 10 des de Redis (ultra-ràpid)
 */
fastify.get('/api/ranking/top', async (request, reply) => {
    try {
        const topIds = await redis.zrevrange('ranking_global', 0, 9, 'WITHSCORES');
        const ranking = [];
        for (let i = 0; i < topIds.length; i += 2) {
            const id = topIds[i];
            const score = topIds[i + 1];
            const { rows } = await fastify.pg.query('SELECT nickname FROM jugadors WHERE id = $1', [id]);
            ranking.push({ nickname: rows[0]?.nickname || 'Anònim', punts: score });
        }
        return ranking;
    } catch (err) {
        return reply.code(500).send({ error: 'Error en carregar el rànquing' });
    }
});

/**
 * WebSocket entry point
 */
fastify.get('/ws/ranking', { websocket: true }, (connection, req) => {
    fastify.log.info('Jugador connectat al Live Ranking via WS 📡');
    connection.socket.send(JSON.stringify({ message: 'Benvingut al Live Ranking de CaçaMites!' }));
});

const start = async () => {
    try {
        await fastify.listen({ port: PORT, host: '0.0.0.0' });
        console.log(`🚀 Fastify CaçaMites actiu al port ${PORT}`);
    } catch (err) {
        fastify.log.error(err);
        process.exit(1);
    }
};

start();
