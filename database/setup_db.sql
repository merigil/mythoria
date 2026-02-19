-- Activar l'extensió de geolocalització
CREATE EXTENSION IF NOT EXISTS postgis;

-- Taula de Jugadors
CREATE TABLE IF NOT EXISTS jugadors (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    nickname VARCHAR(20) UNIQUE NOT NULL,
    password_hash TEXT NOT NULL,
    punts_globals INTEGER DEFAULT 0,
    creat_el TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Taula de les 300 Llegendes
CREATE TABLE IF NOT EXISTS llegendes (
    id SERIAL PRIMARY KEY,
    titol VARCHAR(100) NOT NULL,
    posicio GEOGRAPHY(POINT) NOT NULL, -- Lat/Long real
    radi_activacio INTEGER DEFAULT 50, -- metres
    config_joc JSONB, -- Paràmetres del mini-joc
    dificultat VARCHAR(10)
);

-- Taula de Partides (Logs de seguretat)
CREATE TABLE IF NOT EXISTS partides (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    jugador_id UUID REFERENCES jugadors(id),
    llegenda_id INTEGER REFERENCES llegendes(id),
    puntuacio INTEGER,
    guanyada BOOLEAN DEFAULT FALSE,
    timestamp_final TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    signatura_valida BOOLEAN
);

-- Índex per a rànquing ràpid
CREATE INDEX IF NOT EXISTS idx_punts_globals ON jugadors (punts_globals DESC);
-- Índex espacial per trobar llegendes a prop
CREATE INDEX IF NOT EXISTS idx_llegendes_posicio ON llegendes USING GIST (posicio);

-- INSERCIÓ DE DADES DE PROVA (VIC)
INSERT INTO llegendes (titol, posicio, radi_activacio, dificultat, config_joc) VALUES
('Llegenda del Pont', ST_SetSRID(ST_MakePoint(2.254045, 41.930723), 4326)::geography, 50, 'Fàcil', '{"tipus": "quiz", "temps": 60}'),
('El Secret del Riu', ST_SetSRID(ST_MakePoint(2.252604, 41.932029), 4326)::geography, 50, 'Mitjana', '{"tipus": "puzzle", "pecas": 12}'),
('L''Alquimista de Vic', ST_SetSRID(ST_MakePoint(2.251943, 41.935756), 4326)::geography, 50, 'Difícil', '{"tipus": "recuperar_objecte", "id": 101}'),
('La Torre Vigilant', ST_SetSRID(ST_MakePoint(2.254130, 41.932329), 4326)::geography, 50, 'Fàcil', '{"tipus": "velocitat", "metres": 100}'),
('L''Enigma de l''Horta', ST_SetSRID(ST_MakePoint(2.251378, 41.934813), 4326)::geography, 50, 'Mitjana', '{"tipus": "memoria", "nivells": 3}');
