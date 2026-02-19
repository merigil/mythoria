/**
 * Script de manteniment per actualitzar coordenades de llegendes des d'un CSV.
 * Ús: node update_coords.js ruta/al/fitxer.csv
 */
const { Pool } = require('pg');
const fs = require('fs');
const path = require('path');
require('dotenv').config({ path: path.join(__dirname, '../.env') });

const pool = new Pool({
    connectionString: process.env.DATABASE_URL,
    ssl: { rejectUnauthorized: false }
});

async function updateFromCSV(filePath) {
    const data = fs.readFileSync(filePath, 'utf8');
    const lines = data.split('\n').slice(1); // Ometre capçalera

    for (const line of lines) {
        if (!line.trim()) continue;
        const [id, titol, lat, lon] = line.split(',');

        try {
            const query = `
                UPDATE llegendes 
                SET posicio = ST_SetSRID(ST_MakePoint($1, $2), 4326)::geography,
                    titol = $3
                WHERE id = $4
            `;
            await pool.query(query, [lon.trim(), lat.trim(), titol.trim(), id.trim()]);
            console.log(`✅ Actualitzada llegenda ${id}: ${titol}`);
        } catch (err) {
            console.error(`❌ Error actualitzant llegenda ${id}:`, err.message);
        }
    }

    console.log('\n🚀 Procés finalitzat.');
    await pool.end();
}

const csvPath = process.argv[2];
if (!csvPath) {
    console.log('⚠️ Si us plau, proporciona la ruta al fitxer CSV.');
    process.exit(1);
}

updateFromCSV(csvPath);
