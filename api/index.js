const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const { Pool } = require('pg');
const crypto = require('crypto');
const cors = require('cors');
require('dotenv').config();

const app = express();
const server = http.createServer(app);
const io = new Server(server, {
    cors: { origin: "*" }
});

app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 3000;
const SECRET_KEY = process.env.HMAC_SECRET_KEY || 'clau-secreta-per-defecte';

// Connexió a Supabase (PostgreSQL)
const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: { rejectUnauthorized: false }
});

/**
 * Middleware de seguretat HMAC
 */
function validarSeguretat(req, res, next) {
    const { puntuacio, timestamp } = req.body;
    const signaturaRebuda = req.headers['x-signature'];

    if (!puntuacio || !timestamp || !signaturaRebuda) {
        return res.status(400).json({ error: 'Falten dades de seguretat' });
    }

    const data = `${puntuacio}:${timestamp}`;
    const hash = crypto.createHmac('sha256', SECRET_KEY)
        .update(data)
        .digest('hex');

    if (hash !== signaturaRebuda) {
        return res.status(403).json({ error: 'Signatura invàlida' });
    }
    next();
}

/**
 * Lògica de Proximitat: Detectar si el jugador està a prop de llegendes
 * Utilitza ST_DWithin de PostGIS
 */
app.get('/api/llegendes/proximitat', async (req, res) => {
    const { lat, lon, radi = 50 } = req.query;

    if (!lat || !lon) {
        return res.status(400).json({ error: 'Falten coordenades' });
    }

    try {
        const query = `
            SELECT id, titol, radi_activacio, dificultat, config_joc,
                   ST_Distance(posicio, ST_SetSRID(ST_MakePoint($1, $2), 4326)::geography) as distancia_metres
            FROM llegendes
            WHERE ST_DWithin(posicio, ST_SetSRID(ST_MakePoint($1, $2), 4326)::geography, $3)
            ORDER BY distancia_metres ASC
        `;
        const { rows } = await pool.query(query, [lon, lat, radi]);

        // Mapejat per a la resposta JSON que espera Android
        const result = rows.map(r => ({
            id: r.id,
            titol: r.titol,
            distancia_metres: parseFloat(r.distancia_metres),
            activa: true,
            config: r.config_joc
        }));

        res.json(result);
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Error del servidor' });
    }
});

/**
 * Finalitzar joc i actualitzar rànquing
 */
app.post('/api/joc/finalitzar', validarSeguretat, async (req, res) => {
    const { jugador_id, llegenda_id, puntuacio } = req.body;

    try {
        // 1. Registrar partida
        await pool.query(
            'INSERT INTO partides (jugador_id, llegenda_id, puntuacio, guanyada, signatura_valida) VALUES ($1, $2, $3, $4, $5)',
            [jugador_id, llegenda_id, puntuacio, true, true]
        );

        // 2. Actualitzar punts globals del jugador
        const { rows } = await pool.query(
            'UPDATE jugadors SET punts_globals = punts_globals + $1 WHERE id = $2 RETURNING nickname, punts_globals',
            [puntuacio, jugador_id]
        );

        if (rows.length > 0) {
            const jugador = rows[0];
            // 3. Emetre esdeveniment via WebSockets per al rànquing general
            io.emit('actualitzacio_ranking', {
                nickname: jugador.nickname,
                nous_punts: jugador.punts_globals,
                missatge: `${jugador.nickname} ha superat una llegenda!`
            });
        }

        res.json({ success: true, message: 'Puntuació registrada i rànquing actualitzat' });
    } catch (err) {
        console.error(err);
        res.status(500).json({ error: 'Error al registrar la partida' });
    }
});

// WebSockets logic
io.on('connection', (socket) => {
    console.log('Jugador connectat al canal de rànquing 📊');
    socket.on('disconnect', () => {
        console.log('Jugador desconnectat');
    });
});

server.listen(PORT, () => {
    console.log(`Servidor CaçaMites 🛡️ escoltant al port ${PORT}`);
});
