-- Inserció de la llegenda "El salt de la minyona" amb la seva configuració dinàmica
INSERT INTO llegendes (codi, titol, posicio, radi_activacio, dificultat, config_joc)
VALUES (
    'MINYONA',
    'El salt de la minyona',
    ST_SetSRID(ST_MakePoint(2.251292, 41.934695), 4326), -- Coordenades de TEST (Punt inicial real: 41.930669, 2.367743)
    5,
    'Mitjana',
    '{
        "tipus": "MINYONA",
        "punt_inici": {"lat": 41.930669, "lon": 2.367743},
        "test_punt_inici": {"lat": 41.934695, "lon": 2.251292},
        "punt_x": {"lat": 41.923656, "lon": 2.370418},
        "test_punt_x": {"lat": 41.935563, "lon": 2.251944},
        "radi_activacio": 5,
        "personatges_part1": [
            {"id": "m_caterina", "nom": "Caterina Solucions i Feina-Feta", "lat": 41.934912, "lon": 2.251250, "text": "Jo et diré el que has de fer: camina recte i no t''aturis per bajanades."},
            {"id": "m_quiteria", "nom": "Quitèria Llàgrima i Ai-Mare", "lat": 41.935066, "lon": 2.251249, "text": "Ai mare, quina desgràcia! La pobra minyona... no crec que se''n surti."},
            {"id": "m_bernat", "nom": "Bernat Nasfi i Xerraire", "lat": 41.935230, "lon": 2.251246, "text": "He sentit a dir que el diable ronda per aquí prop... m''ho ha dit la tieta de la veïna."},
            {"id": "m_filomena", "nom": "Filomena Grosseta i Escampafums", "lat": 41.935421, "lon": 2.251230, "text": "Quina calor que fa! I quina pudor de sofre, no trobeu?"},
            {"id": "m_banyeta", "nom": "Joan Banyeta i Sofre", "lat": 41.935553, "lon": 2.251397, "text": "La Minyona és una mentidera! Vull veure com salta a canvi d’unes sabates noves... Jo només sóc un pobre viatger, no em miris així."}
        ],
        "personatges_part2": [
            {"id": "m_joanet", "nom": "Joan Trastet i Nyapet", "lat": 41.935569, "lon": 2.251953, "text": "Tinc un invent que no serveix per a res, però potser algun d''aquests objectes sí!"},
            {"id": "m_miquel", "nom": "Miquel Cargol i Filferro", "lat": 41.935471, "lon": 2.252165, "text": "Per aturar una caiguda cal alguna cosa que esmorteixi o que floti a l''aire..."},
            {"id": "m_quimet", "nom": "Quimet Badoc i Escampall", "lat": 41.935661, "lon": 2.252321, "text": "He vist paracaigudes i llits elàstics... però ves a saber si funcionen!"}
        ],
        "objectes": [
            {"id": "m_o1", "nom": "Casc", "correcte": true, "lat": 41.935653, "lon": 2.251944},
            {"id": "m_o2", "nom": "Llit elàstic", "correcte": true, "lat": 41.935653, "lon": 2.252065},
            {"id": "m_o3", "nom": "Xarxa de protecció", "correcte": true, "lat": 41.935563, "lon": 2.252065},
            {"id": "m_o4", "nom": "Paracaigudes", "correcte": true, "lat": 41.935473, "lon": 2.252065},
            {"id": "m_o5", "nom": "Ala delta", "correcte": true, "lat": 41.935473, "lon": 2.251944},
            {"id": "m_o6", "nom": "Genolleres", "correcte": false, "lat": 41.935473, "lon": 2.251823},
            {"id": "m_o7", "nom": "Caixa de cartró buida", "correcte": false, "lat": 41.935563, "lon": 2.251823},
            {"id": "m_o8", "nom": "Coixí", "correcte": false, "lat": 41.935653, "lon": 2.251823},
            {"id": "m_o9", "nom": "Taula", "correcte": false, "lat": 41.935743, "lon": 2.251944},
            {"id": "m_o10", "nom": "Sofà", "correcte": false, "lat": 41.935743, "lon": 2.252065},
            {"id": "m_o11", "nom": "Tovallola", "correcte": false, "lat": 41.935743, "lon": 2.251823},
            {"id": "m_o12", "nom": "Bossa de roba", "correcte": false, "lat": 41.935383, "lon": 2.251944},
            {"id": "m_o13", "nom": "Llibre", "correcte": false, "lat": 41.935383, "lon": 2.252065},
            {"id": "m_o14", "nom": "Galleda", "correcte": false, "lat": 41.935383, "lon": 2.251823},
            {"id": "m_o15", "nom": "Rellotge de paret", "correcte": false, "lat": 41.935563, "lon": 2.251702}
        ],
        "temps_part2": 300,
        "objectius_correctes": 3,
        "victoria": {
            "id": "c_m_victoria",
            "titol": "La Minyona",
            "text": "Moltes gràcies, valent viatger! Amb la teva ajuda he pogut demostrar que el dimoni mentia. Ara el meu salt serà recordat com un acte de coratge, no de desesperació. Aquí tens la meva llegenda per al teu llibre.",
            "imatge": "minyona_gracies"
        }
    }'::jsonb
);
