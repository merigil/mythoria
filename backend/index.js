const express = require('express');
const crypto = require('crypto');
const cors = require('cors');
require('dotenv').config();

const app = express();
app.use(express.json());
app.use(cors());

const PORT = process.env.PORT || 3000;
const SECRET_KEY = process.env.HMAC_SECRET_KEY || 'clau-secreta-per-defecte';

/**
 * Middleware de seguretat per validar la signatura HMAC de la puntuació.
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
        return res.status(403).json({ error: 'Signatura invàlida. Ets un trampós?' });
    }

    next();
}

app.get('/', (req, res) => {
    res.send('API de CaçaMites activa! 🛡️');
});

/**
 * Endpoint per finalitzar un joc i registrar la puntuació.
 */
app.post('/api/joc/finalitzar', validarSeguretat, (req, res) => {
    const { jugador_id, llegenda_id, puntuacio } = req.body;

    // Aquí aniria la lògica d'insert a la base de dades (PostgreSQL)
    console.log(`Puntuació validada per al jugador ${jugador_id}: ${puntuacio}`);

    res.json({
        success: true,
        message: 'Puntuació registrada correctament',
        punts_guanyats: 1
    });
});

app.listen(PORT, () => {
    console.log(`Servidor CaçaMites escoltant al port ${PORT}`);
});
