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
