package com.cacamites.app.repository

import android.location.Location
import com.cacamites.app.model.Card
import com.cacamites.app.model.GameState
import com.cacamites.app.model.GameStatus
import com.cacamites.app.model.Point
import com.cacamites.app.model.PointState
import com.cacamites.app.model.PointType
import com.cacamites.app.model.Quiz
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint

class GameRepository {

    private val BARO_TIMER_SECONDS = 15 * 60L 
    private val SERPENT_TIMER_SECONDS = 30 * 60L
    private var timerJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)
    private val _vibratedPoints = mutableSetOf<String>()

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation: StateFlow<GeoPoint?> = _userLocation.asStateFlow()

    init {
        initializeGame()
    }

    private fun initializeGame() {
        _cards.value = createCards()
        _points.value = createPoints().filter { it.id.startsWith("p_") || it.id == "p_start" }
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }

    fun startNewLegend(type: String) {
        timerJob?.cancel()
        val allPoints = createPoints()
        val filteredPoints = when(type) {
            "SERPENT" -> allPoints.filter { it.id.startsWith("s_") }
            else -> allPoints.filter { it.id.startsWith("p_") }
        }
        
        val initialPoints = filteredPoints.map { 
            if (it.id.endsWith("_start")) it.copy(state = PointState.VISIBLE) else it 
        }
        
        _points.value = initialPoints
        _gameState.update { it.copy(legendId = type, status = GameStatus.WAITING_TO_START) }
    }

    private fun createPoints(): List<Point> {
        return listOf(
            // --- LA FONT DE LA BARONESSA (SAVASSONA) ---
            Point("p_start", "Punt d'Inici", GeoPoint(41.932224, 2.252608), PointType.NARRATIVE, PointState.VISIBLE, score = 0),
            Point("p_llops1", "Llops Punt 01", GeoPoint(41.931965, 2.251870), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_llops2", "Llops Punt 02", GeoPoint(41.931997, 2.252057), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_llops3", "Llops Punt 03", GeoPoint(41.932001, 2.252188), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_soldat", "Soldat", GeoPoint(41.931959, 2.252261), PointType.ENEMY, score = 0, isTrap = true),
            Point("p_baronessa", "La Baronessa i el Jove", GeoPoint(41.931551, 2.251217), PointType.MANDATORY, score = 20, isMandatory = true),
            Point("p_espasa", "L'Espasa Màgica", GeoPoint(41.932000, 2.252444), PointType.OBJECT, score = 30, isMandatory = true), 
            Point("p_pastora", "La Pastora", GeoPoint(41.931391, 2.251742), PointType.NARRATIVE, score = 10),
            Point("p_mercader", "Mercader Ambulant", GeoPoint(41.931339, 2.251871), PointType.OBJECT, score = 15),
            Point("p_ancia", "L'Ancià dels mapes", GeoPoint(41.931764, 2.251324), PointType.OBJECT, score = 5),
            Point("p_bassa", "La bassa i el follet", GeoPoint(41.931638, 2.251472), PointType.OBJECT, score = 15),
            Point("p_prat", "Un prat", GeoPoint(41.931674, 2.251708), PointType.NARRATIVE, score = 15),
            Point("p_clerge", "El Clerga", GeoPoint(41.931690, 2.251900), PointType.QUIZ, score = 0, quiz = Quiz(
                question = "Quin és l’estil d’aquesta església?",
                options = listOf("Clàssic", "Romànic Llombard", "Gòtic"),
                correctOptionIndex = 1,
                pointsIfCorrect = 15
            )),
            Point("p_pastor", "El Pastor", GeoPoint(41.931659, 2.252168), PointType.ENEMY, score = -10, isTrap = true),
            Point("p_nen", "Nen Trepella", GeoPoint(41.931396, 2.252176), PointType.ENEMY, score = -5, isTrap = true),
            Point("p_bota", "Bota de Vi", GeoPoint(41.931224, 2.251789), PointType.ENEMY, score = -8, isTrap = true),
            Point("p_baro", "El Baró", GeoPoint(41.931540, 2.251650), PointType.ENEMY, score = 0, isMandatory = true, state = PointState.LOCKED),
            Point("p_moneda1", "Moneda de Bronze", GeoPoint(41.931071, 2.251945), PointType.HIDDEN_OBJECT, score = 0), 
            Point("p_moneda2", "Moneda de Coure", GeoPoint(41.930961, 2.251918), PointType.HIDDEN_OBJECT, score = 0),
            Point("p_moneda3", "Moneda d'Or", GeoPoint(41.930668, 2.251813), PointType.HIDDEN_OBJECT, score = 0),
            Point("p_font_baronessa", "La font de la Baronessa", GeoPoint(41.931559, 2.251943), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            
            // --- LA SERPENT DE MANLLEU ---
            Point("s_start", "Punt d'Inici", GeoPoint(41.932250, 2.252556), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_pregoner", "El Pregoner", GeoPoint(41.930508, 2.253943), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_barquer", "La barquera", GeoPoint(41.930191, 2.253875), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_vaquer", "El vaquer", GeoPoint(41.930153, 2.254478), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_rubi", "El diamant de la serpent", GeoPoint(41.930557, 2.254912), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = true),
            Point("s_terrissaire", "El terrissaire", GeoPoint(41.930406, 2.254734), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_remeiera", "La vella remeiera", GeoPoint(41.930389, 2.254480), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_morter", "El Morter", GeoPoint(41.930529, 2.254812), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = true),
            Point("s_final", "La plaça", GeoPoint(41.930523, 2.254483), PointType.NARRATIVE, PointState.LOCKED, score = 0)
        )
    }

    private fun createCards(): List<Card> {
        return listOf(
            // --- CARDS BARONESSA ---
            Card("c_p_start", "Punt d'Inici", "Benvingut, noble explorador. Sóc la Isolda, la trobadora. Avui recorreràs les terres del Baró per protegir un amor prohibit. Estigues atent als senyals i no tinguis por de preguntar als vilatans.", "Inici", "la_trobadora_01", true),
            Card("c_p_llops1", "Llops Punt 01", "T’estàs allunyant del camí i els udols dels llops s’acosten. No segueixis per aquí: torna enrere abans que sigui massa tard.", "Perill", "llops_salvatges", true),
            Card("c_p_llops2", "Llops Punt 02", "(Udols distants) Els llops ronden aquesta zona. No et desvies de la ruta principal!", "Perill", "llops_salvatges", true),
            Card("c_p_llops3", "Llops Punt 03", "Compte! Les feres han marcat aquest territori. Si segueixes endavant, no podré protegir-te.", "Perill", "llops_salvatges", true),
            Card("c_p_soldat", "Soldat", "No pots passar! No tens autorització del senyor d’aquesta terra; hauràs de tornar enrere. No perds punts, però millor que no posis a prova la seva paciència… Torna enrere i no vagis endavant.", "Obstacle", "soldat_stop", true),
            Card("c_p_baronessa", "La Baronessa i el Jove", "El meu espòs mai no ha estat home de cor noble. Els seus gelos ferotges i el seu afany de possessió m’han tinguda captiva entre els murs del castell. T’ho suplico: ajuda’ns en aquesta dissort. No permetis que el Baró ens trobi; avança’t tu als seus passos.", "Obligatori", "baronessa_criat_junts01", true),
            Card("c_p_espasa", "L'Espasa Màgica", "Aquesta vella espasa, forjada en temps antics, guarda el poder sagrat de defensar els cors enamorats. Amb la seva fúria justa, pot fer fugir el Baró, allunyant-lo dels pensaments que volen fer mal als amants.", "Objecte", "la_espassa", true),
            Card("c_p_pastora", "La Pastora", "El Baró? Un mal home és… Ningú no sap el que ha patit la pobra baronessa, i nosaltres encara hem de lliurar-li els millors formatges! Tant de bo algú li posi fre a tanta supèrbia… (La pastora et dona ànims).", "Llegenda", "la_pastora", true),
            Card("c_p_mercader", "Mercader Ambulant", "On vas, ànima errant, amb sabates tan tristes? Pren-ne aquestes de noves. (Et dóna unes sabates noves i això et fa caminar més àgilment).", "Ajuda", "mercader_ambulant", true),
            Card("c_p_ancia", "L'Ancià dels mapes", "Segueix aquest corrió, car és més breu que cap altre que els teus peus puguin trepitjar. Per ell avançaràs amb pas lleuger i hauràs arribat al destí que el teu cor anhela. (Et dona un mapa amb un camí secret).", "Mapa", "ancia_mapes", true),
            Card("c_p_bassa", "La bassa i el follet", "Has trobat la Bassa de la Teuleria. Un follet canta: \"Cerca el banc de pedra del camí: al forat de la roca, allí, la trobaràs\". (Et dona 15 punts de força i una pista valuosa).", "Pista", "bassa_follet", true),
            Card("c_p_prat", "Un prat", "Davant els teus ulls s’obre un camp ample. A ponent s’alça un castell que t’atreu amb força: és el castell del Baró. La visió t’infon coratge. Endavant!", "Lloc", "prat_01", true),
            Card("c_p_clerge", "El Clerga", "Interroga el clergue sobre l'estil de l'església.", "Pregunta", "el_clerga_01", true),
            Card("c_p_pastor", "El Pastor", "El Baró? Gran home! Mes per culpa de sa muller ens té lligats. Vés, torna a casa, o acabaràs amb el cap entortolligat! (Et fa enrabiar i perds punts de força).", "Enemic", "el_pastor", true),
            Card("c_p_nen", "Nen Trepella", "Ui, ui, sí, sí… has de córrer pel prat! Ves per aquí, esquiva fang i gallines! (T’enganya fent-te anar per un camp que no porta enlloc).", "Trampa", "el_nen_trapella", true),
            Card("c_p_bota", "Bota de Vi", "Robes una bota de vi i en fas un bon glop. El descans s’entreté més del compte i perds el ritme.", "Obstacle", "la_bota_de_vi", true),
            Card("c_p_baro", "El Baró", "Rumors de traïció han arribat a les meves torres! Tallaré els seus caps! Atura el Baró abans que trobi els amants.", "Boss", "el_baro", true),
            Card("c_p_moneda1", "Moneda de Bronze", "Has trobat una moneda antiga! No t'ajuda ara, però col·lecciona-les totes tres!", "Objecte", "moneda_bronze", true),
            Card("c_p_moneda2", "Moneda de Coure", "Has trobat una moneda antiga! No t'ajuda ara, però col·lecciona-les totes tres!", "Objecte", "moneda_cobre", true),
            Card("c_p_moneda3", "Moneda d'Or", "Has trobat una moneda antiga! No t'ajuda ara, però col·lecciona-les totes tres!", "Objecte", "moneda_or", true),
            Card("c_p_font_baronessa", "La font de la Baronessa", "Amb el Baró foragitat, el nostre amor podrà florir. Com a agraïment, et lliurem aquest antic pergamí que guarda el veritable final de la llegenda. Gràcies, viatger!", "Victòria", "la_font_final", true),

            // --- CARDS SERPENT ---
            Card("c_s_start", "Punt d'Inici", "Acabes d’arribar a la noble vila de Manlleu. La teva atenció és captada per un grup de gent que s’arreplega al voltant d’un pregoner. Apropa-t’hi i escolta què anuncia.", "Inici", "placa_de_fra_bernadi_manlleu", true),
            Card("c_s_pregoner", "El Pregoner", "\"Apropeu-vos! La por recorre els carrers: caps de bestiar desapareixen i un infant ha estat vist per últim cop a la llera del Ter...\"", "Llegenda", "serpent_pregoner", true),
            Card("c_s_barquer", "El barquer", "\"Diuen que no sempre fou monstre, sinó una jove orgullosa maleïda. Vols travessar el riu? Jo no t’hi portaré...\"", "Llegenda", "serpent_la_barquera", true),
            Card("c_s_vaquer", "El vaquer", "\"Tenia deu vaques i ara en tinc la meitat! Està prop del riu, camina recta cap allà. Si algú li pren el diamant del cap...\"", "Llegenda", "serpent_el_vaquer", true),
            Card("c_s_rubi", "El diamant de la serpent", "Heu trobat el diamant encantat! Ara la serpent us persegueix enfurismada. Teniu 30 minuts per trobar el morter gegant abans que us atrapi.", "Objecte", "serpent_rubi_serpent", true),
            Card("c_s_terrissaire", "El terrissaire", "\"Per vèncer-la hauràs de trobar un morter gegant i amagar-t'hi dins al mig de la plaça...\"", "Llegenda", "serpent_el_terrisaire", true),
            Card("c_s_remeiera", "La vella remeiera", "\"Escolteu-me: el morter gegant s’amaga sota les tres creus de la font, just sota l’església...\"", "Llegenda", "serpent_la_remeiera", true),
            Card("c_s_morter", "El Morter", "Heu trobat el morter! Però la Serp colpeja amb força terrible. Ara correu cap al cor de la plaça per resistir la seva ràbia.", "Objecte", "serpent_morter", true),
            Card("c_s_final", "La plaça", "Heu resistit, valents! El rubí us pertany i la vila està salvada. El poble us ofereix un cofre amb el pergamí de la llegenda sencera.", "Victòria", "serpent_plaça_manlleu", true)
        )
    }

    fun getCard(cardId: String): Card {
        return _cards.value.find { it.id == cardId } 
            ?: Card(cardId, "Indret Desconegut", "Has trobat un lloc misteriós...", "Misteri", null, true)
    }

    fun updatePointState(pointId: String, newState: PointState) {
        _points.update { list ->
            list.map { if (it.id == pointId) it.copy(state = newState) else it }
        }
    }

    fun onPointVisited(pointId: String) {
        val point = _points.value.find { it.id == pointId } ?: return
        if (point.state == PointState.COMPLETED) return
        
        updatePointState(pointId, PointState.COMPLETED)
        
        _gameState.update { state ->
            val newInventory = state.inventory.toMutableSet().apply { 
                if (point.type == PointType.HIDDEN_OBJECT || point.type == PointType.OBJECT) add(pointId)
            }
            state.copy(
                totalScore = state.totalScore + point.score,
                inventory = newInventory,
                visitedPoints = state.visitedPoints + pointId
            )
        }
        
        if (pointId == "p_start") unlockInitialPoints()
        if (pointId == "p_baronessa") updatePointState("p_baro", PointState.VISIBLE)
        if (pointId == "p_baro") startBaroTimer()
        
        // Serpent Sequence
        if (pointId == "s_start") updatePointState("s_pregoner", PointState.VISIBLE)
        if (pointId == "s_pregoner") updatePointState("s_barquer", PointState.VISIBLE)
        if (pointId == "s_barquer") updatePointState("s_vaquer", PointState.VISIBLE)
        if (pointId == "s_vaquer") updatePointState("s_rubi", PointState.VISIBLE)
        if (pointId == "s_rubi") {
            startSerpentTimer()
            updatePointState("s_terrissaire", PointState.VISIBLE)
        }
        if (pointId == "s_terrissaire") updatePointState("s_remeiera", PointState.VISIBLE)
        if (pointId == "s_remeiera") updatePointState("s_morter", PointState.VISIBLE)
        if (pointId == "s_morter") updatePointState("s_final", PointState.VISIBLE)
    }

    private fun unlockInitialPoints() {
        _points.update { list ->
            list.map { 
                if (it.state == PointState.LOCKED && !it.id.startsWith("s_") && it.id != "p_baro") it.copy(state = PointState.VISIBLE) else it
            }
        }
    }

    fun onQuizAnswered(pointId: String, selectedIndex: Int) {
        val point = _points.value.find { it.id == pointId } ?: return
        val quiz = point.quiz ?: return
        if (point.state == PointState.COMPLETED) return

        val isCorrect = selectedIndex == quiz.correctOptionIndex
        val addedScore = if (isCorrect) quiz.pointsIfCorrect else 0

        _gameState.update { it.copy(totalScore = it.totalScore + addedScore) }
        updatePointState(pointId, PointState.COMPLETED)
    }

    private fun startBaroTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = BARO_TIMER_SECONDS, status = GameStatus.ACTIVE_PLAY) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { it.copy(timerSecondsRemaining = (it.timerSecondsRemaining ?: 0) - 1) }
                checkWinCondition()
            }
            _gameState.update { it.copy(status = GameStatus.LOST) }
        }
    }

    private fun startSerpentTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = SERPENT_TIMER_SECONDS, status = GameStatus.ACTIVE_PLAY) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { it.copy(timerSecondsRemaining = (it.timerSecondsRemaining ?: 0) - 1) }
            }
            _gameState.update { it.copy(status = GameStatus.LOST) }
        }
    }

    fun checkWinCondition() {
        val state = _gameState.value
        val hasEspasa = state.inventory.contains("p_espasa")
        val hasScore = state.totalScore >= 101
        val hasBaronessa = state.visitedPoints.contains("p_baronessa")

        if (hasEspasa && hasScore && hasBaronessa && state.visitedPoints.contains("p_baro")) {
            _gameState.update { it.copy(status = GameStatus.WON, timerSecondsRemaining = null) }
            timerJob?.cancel()
            updatePointState("p_font_baronessa", PointState.VISIBLE)
        }
    }

    fun updateUserLocation(lat: Double, lng: Double) {
        _userLocation.value = GeoPoint(lat, lng)
    }

    fun checkProximity(userLat: Double, userLng: Double, targetLat: Double, targetLng: Double, threshold: Float = 20f): Pair<Boolean, Float> {
        val userLoc = Location("user").apply { latitude = userLat; longitude = userLng }
        val targetLoc = Location("target").apply { latitude = targetLat; longitude = targetLng }
        val distance = userLoc.distanceTo(targetLoc)
        return Pair(distance <= threshold, distance)
    }

    fun isVibrated(pointId: String): Boolean = _vibratedPoints.contains(pointId)
    fun markAsVibrated(pointId: String) { _vibratedPoints.add(pointId) }
}
