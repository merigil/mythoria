# 📜 Projecte: Joc de Llegendes GPS (Catalunya)

## 📌 Descripció General

Aplicació de mini-jocs geolocalitzats dissenyada per a usuaris de 14 a 17 anys i adults. El sistema permet jugar a "llegendes" (mini-jocs) situades en punts GPS específics, amb un rànquing compartit en temps real per a 3.000 usuaris concurrents.

## 🛠 Arquitectura Tècnica

- **Client:** Android Studio (Kotlin, MVVM, Google Maps SDK).
- **Backend:** API REST stateless (Node.js/Fastify) desplegada a Railway.
- **Base de Dades:** PostgreSQL + PostGIS (Supabase) per a gestió espacial.
- **Cache/Ranking:** Redis (Sorted Sets) per a rànquings d'alta velocitat.
- **Realtime:** WebSockets/Supabase Realtime per a l'actualització del podi.

## 📍 Punts de Prova (Ubicació: Vic)

L'entorn inicial disposa de 5 nodes de prova per validar la lògica de proximitat:

- `41.930723, 2.254045` - Llegenda del Pont
- `41.932029, 2.252604` - El Secret del Riu
- `41.935756, 2.251943` - L'Alquimista de Vic
- `41.932329, 2.254130` - La Torre Vigilant
- `41.934813, 2.251378` - L'Enigma de l'Horta

## 🔒 Seguretat i Normativa (Anti-Cheat)

Donat que el públic inclou menors de 18 anys:

- **Validació GPS:** El servidor valida via PostGIS (`ST_DWithin`) que l'usuari estigui a < 50m de la llegenda abans d'acceptar la puntuació.
- **Signatura HMAC-SHA256:** Totes les peticions `POST /joc/finalitzar` han d'anar signades amb un hash (Puntuació + Timestamp + SecretKey) per evitar la injecció de punts.
- **Detecció de Mock GPS:** L'app Android bloqueja l'enviament si es detecta `isFromMockProvider`.

## 🚀 Instruccions per a Antigravity

- **DB Setup:** Executar el script SQL de `setup_db.sql` per crear les taules amb índexs GIST i B-Tree.
- **API Logic:** Implementar els endpoints de `/map/llegendes` (filtre per radi) i `/joc/finalitzar` (validació de signatura i proximitat).
- **Realtime Ranking:** Configurar el canal de Broadcast per notificar canvis al rànquing global immediatament.
- **Escalabilitat:** Optimitzar les consultes SQL per suportar 3.000 jugadors consultant el mapa simultàniament.
