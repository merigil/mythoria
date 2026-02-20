const crypto = require('crypto');
const http = require('http');

const API_URL = 'http://localhost:3000';
const SECRET_KEY = 'clau-secreta-per-defecte';

async fun request(method, path, body = null) {
    return new Promise((resolve, reject) => {
        const url = new URL(`${API_URL}${path}`);
        const options = {
            method: method,
            hostname: url.hostname,
            port: url.port,
            path: url.pathname + url.search,
            headers: {
                'Content-Type': 'application/json'
            }
        };

        if (body && body.signature) {
            options.headers['x-signature'] = body.signature;
            delete body.signature;
        }

        const req = http.request(options, (res) => {
            let data = '';
            res.on('data', (chunk) => data += chunk);
            res.on('end', () => {
                try {
                    resolve({ status: res.statusCode, data: JSON.parse(data) });
                } catch (e) {
                    resolve({ status: res.statusCode, data: data });
                }
            });
        });

        req.on('error', reject);
        if (body) req.write(JSON.stringify(body));
        req.end();
    });
}

function generateSignature(score, timestamp) {
    const data = `${score}:${timestamp}`;
    return crypto.createHmac('sha256', SECRET_KEY)
        .update(data)
        .digest('hex');
}

async function runTests() {
    console.log('🧪 Iniciant proves de seguretat de l\'API CaçaMites...\n');

    // TEST 1: Proximitat vàlida (Vic - Pont)
    console.log('Test 1: Consulta de proximitat vàlida (Vic)');
    const res1 = await request('GET', '/api/llegendes/proximitat?lat=41.930723&lon=2.254045');
    console.log(`Status: ${res1.status}, Llegendes robades: ${res1.data.length}`);
    if (res1.status === 200 && res1.data.length > 0) console.log('✅ OK');
    else console.log('❌ Error');

    // TEST 2: Finalitzar joc amb HMAC vàlid i Proximitat vàlida
    console.log('\nTest 2: Finalitzar joc (Correcte)');
    const score2 = 10;
    const ts2 = Date.now().toString();
    const sig2 = generateSignature(score2, ts2);
    const res2 = await request('POST', '/api/joc/finalitzar', {
        jugador_id: '550e8400-e29b-41d4-a716-446655440000',
        llegenda_id: 1, // Pont
        puntuacio: score2,
        timestamp: ts2,
        lat: 41.930723,
        lon: 2.254045,
        signature: sig2
    });
    console.log(`Status: ${res2.status}, Resposta: ${JSON.stringify(res2.data)}`);
    if (res2.status === 200) console.log('✅ OK');
    else console.log('❌ Error (Potser cal configurar DATABASE_URL)');

    // TEST 3: Intent de trampa (HMAC invàlid)
    console.log('\nTest 3: Intent de trampa (HMAC fals)');
    const res3 = await request('POST', '/api/joc/finalitzar', {
        jugador_id: '550e8400-e29b-41d4-a716-446655440000',
        llegenda_id: 1,
        puntuacio: 1000,
        timestamp: Date.now().toString(),
        lat: 41.930723,
        lon: 2.254045,
        signature: 'una-signatura-falsa'
    });
    console.log(`Status: ${res3.status}, Resposta: ${JSON.stringify(res3.data)}`);
    if (res3.status === 403) console.log('✅ Bloquejat per HMAC (Correcte)');
    else console.log('❌ Error: Hauria d\'haver estat bloquejat');

    // TEST 4: Intent de trampa (Proximitat invàlida)
    console.log('\nTest 4: Intent de trampa (Massa lluny)');
    const score4 = 10;
    const ts4 = Date.now().toString();
    const sig4 = generateSignature(score4, ts4);
    const res4 = await request('POST', '/api/joc/finalitzar', {
        jugador_id: '550e8400-e29b-41d4-a716-446655440000',
        llegenda_id: 1,
        puntuacio: score4,
        timestamp: ts4,
        lat: 41.385063, // Barcelona
        lon: 2.173404,  // Barcelona
        signature: sig4
    });
    console.log(`Status: ${res4.status}, Resposta: ${JSON.stringify(res4.data)}`);
    if (res4.status === 403) console.log('✅ Bloquejat per Geofencing (Correcte)');
    else console.log('❌ Error: Hauria d\'haver estat bloquejat per distància');

    console.log('\n🚀 Proves finalitzades.');
}

runTests().catch(console.error);
