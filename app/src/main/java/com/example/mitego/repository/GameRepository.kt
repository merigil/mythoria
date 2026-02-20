package com.example.mitego.repository

import android.location.Location
import com.example.mitego.model.Card
import com.example.mitego.model.GameState
import com.example.mitego.model.GameStatus
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.model.PointType
import com.example.mitego.model.Quiz
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

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _userLocation = MutableStateFlow<GeoPoint?>(null)
    val userLocation: StateFlow<GeoPoint?> = _userLocation.asStateFlow()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

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
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }

    private fun createPoints(): List<Point> {
        return listOf(
            // SAVASSONA
            Point("p_start", "Punt d'Inici", GeoPoint(41.956361, 2.340167), PointType.NARRATIVE, PointState.VISIBLE, score = 0),
            Point("p_baronessa", "La Baronessa", GeoPoint(41.953683, 2.335898), PointType.MANDATORY, score = 12, isMandatory = true),
            Point("p_jove", "El Jove criat", GeoPoint(41.9538348, 2.336077), PointType.MANDATORY, score = 12, isMandatory = true),
            Point("p_espassa", "L'Espassa Màgica", GeoPoint(41.954496, 2.336380), PointType.OBJECT, score = 30, isMandatory = true), 
            Point("p_pastora", "La Pastora", GeoPoint(41.956444, 2.340611), PointType.NARRATIVE, score = 10),
            Point("p_home_cami", "L'Home del Camí", GeoPoint(41.956237, 2.339180), PointType.NARRATIVE, score = 10),
            Point("p_bassa", "La Bassa de la Teuleria i el follet", GeoPoint(41.955057, 2.337857), PointType.NARRATIVE, score = 15),
            Point("p_mercader", "Mercader Ambulant", GeoPoint(41.954806, 2.336472), PointType.QUIZ, score = 0, quiz = Quiz(
                question = "A la capella vora el castell del Baró, amb petit campanar, quantes campanes hi cabrien?",
                options = listOf("2", "1", "cap campana"),
                correctOptionIndex = 0,
                pointsIfCorrect = 20
            )),
            Point("p_prat", "Un prat", GeoPoint(41.956118, 2.338699), PointType.NARRATIVE, score = 10),
            Point("p_ancia1", "L'Ancià dels mapes", GeoPoint(41.955870, 2.337467), PointType.NARRATIVE, score = 10),
            Point("p_ancia2", "L'Ancià dels mapes", GeoPoint(41.953393, 2.335838), PointType.NARRATIVE, score = 10),
            Point("p_moneda1", "Moneda Antiga 1", GeoPoint(41.953540, 2.335920), PointType.OBJECT, score = 0), 
            Point("p_moneda2", "Moneda Antiga 2", GeoPoint(41.956579, 2.340790), PointType.OBJECT, score = 0),
            Point("p_moneda3", "Moneda Antiga 3", GeoPoint(41.956300, 2.339300), PointType.OBJECT, score = 0),
            Point("p_pastor", "El Pastor", GeoPoint(41.956050, 2.338800), PointType.ENEMY, score = -15, isTrap = true),
            Point("p_clerge", "El Clerga", GeoPoint(41.956500, 2.338150), PointType.ENEMY, score = -10, isTrap = true),
            Point("p_nen", "Nen Trepella", GeoPoint(41.956465, 2.338860), PointType.ENEMY, score = -10, isTrap = true),
            Point("p_bota", "Bota de Vi Ranci", GeoPoint(41.955700, 2.337700), PointType.ENEMY, score = -12, isTrap = true),
            Point("p_llops1", "Llops!", GeoPoint(41.954430, 2.337794), PointType.ENEMY, score = 0, isTrap = false),
            Point("p_llops2", "Llops!", GeoPoint(41.952991, 2.336093), PointType.ENEMY, score = 0, isTrap = false),
            Point("p_llops3", "Llops!", GeoPoint(41.956423, 2.340554), PointType.ENEMY, score = 0, isTrap = false),
            Point("p_soldat", "Soldat", GeoPoint(41.955798, 2.337238), PointType.ENEMY, score = 0, isTrap = false),
            Point("p_baro", "El Baró", GeoPoint(41.953403, 2.336365), PointType.ENEMY, score = 0, isMandatory = true, state = PointState.LOCKED),
            Point("p_font_baronessa", "La Font de la Baronessa", GeoPoint(41.953800, 2.336105), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            
            // MANLLEU
            Point("s_start", "Plaça de Fra Bernadí", GeoPoint(41.930675, 2.254059), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_pregoner", "El Pregoner", GeoPoint(41.930467, 2.253944), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_barquer", "La barquera", GeoPoint(41.930191, 2.253875), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_vaquer", "El vaquer", GeoPoint(41.930153, 2.254478), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_rubi", "El rubí de la serpent", GeoPoint(41.930170, 2.254787), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = false),
            Point("s_terrissaire", "El terrissaire", GeoPoint(41.930406, 2.254734), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_remeiera", "La vella remeiera", GeoPoint(41.930389, 2.254480), PointType.NARRATIVE, PointState.LOCKED, score = 0),
            Point("s_morter", "El Morter", GeoPoint(41.930555, 2.254905), PointType.HIDDEN_OBJECT, PointState.LOCKED, score = 0, isAlwaysInvisible = false),
            Point("s_final", "La plaça", GeoPoint(41.930523, 2.254483), PointType.NARRATIVE, PointState.LOCKED, score = 0)
        )
    }

    private fun createCards(): List<Card> {
        return listOf(
            Card("c_start", "Punt d'Inici", "Benvingut! Has desbloquejat l'aventura.", "", "foto_castell_del_baro", true),
            Card("c_baronessa", "La Baronessa", "«El meu espòs mai no ha estat home de cor noble. Els seus gelos ferotges i el seu afany de possessió m’han tinguda captiva entre els murs del castell...»", "Llegenda", "la_baronessa", true),
            Card("c_jove", "El Jove criat", "«Fidel al meu senyor Baró he estat des que tinc memòria...»", "Llegenda", "el_jove", true),
            Card("c_espassa", "L'Espassa Màgica", "«Aquesta vella espasa, forjada en temps antics...»", "Objecte", "la_espassa", true),
            Card("c_baro", "El Baró", "«M’han estat portats a les meves torres fosques rumors d’una traïció...»", "Enemic", "el_baro", true),
            
            // --- LA SERPENT DE MANLLEU ---
            Card("c_s_pregoner", "El Pregoner", "«Apropeu-vos, nens i nenes, vells i joves, i escolteu la llegenda que avui encara pot despertar-se. La por recorre els carrers de Manlleu: caps de bestiar desapareixen i, ai las, fins i tot un infant. A la llera del riu Ter ha estat vista l’última vegada, una serpent immensa, amb una pedra preciosa al front que li dóna poder. Qui gosi robar-li la joia alliberarà la vila i assolirà riquesa eterna.»", "Llegenda", "serpent_pregoner", true),
            Card("c_s_barquer", "La barquera", "«Ah, escolta’m bé, que no parlo a la lleugera. Diuen que s’ha vist una bestia, sí, la Serpent del riu Ter. No sempre fou monstre, no: abans era dona, jove i orgullosa, plena d’enveja. Però una maledicció cruel la transformà en serp gegantina...»", "Llegenda", "serpent_la_barquera", true),
            Card("c_s_vaquer", "El vaquer", "«Fa dues setmanes tenia 10 vaques, avui en tinc la meitat, una bestia ve per la nit i se les endú i ara també un nen ha desaparegut! Si el rubí algú li treu del cap de ben segur que la serp els seus poders perdrà i podrem respirar tranquils...»", "Llegenda", "serpent_el_vaquer", true),
            Card("c_s_rubi", "El rubí de la serpent", "«Heu trobat el rubí encantat, i ahora el temps us pressiona: trenta minuts només per descobrir el morter que posarà fi a la malícia de la bèstia...»", "Objecte", "el_rubi", true),
            Card("c_s_terrissaire", "El terrissaire", "«Escolta bé, viatger, que no són paraules per a corallers tremolosos! Si vols vèncer aquella serpent que ens amenaça, hauràs de trobar un morter gegant...»", "Llegenda", "serpent_el_terrisaire", true),
            Card("c_s_remeiera", "La vella remeiera", "«Escolteu-me, fills i filles: el morter gegant s’amaga prop d’aquí, amagat sota les tres creus de la font, just sota l’església...»", "Llegenda", "serpent_la_remeiera", true),
            Card("c_s_morter", "El Morter", "«Heu trobat el morter amagat sota les tres creus, però això només és el començament de la vostra prova. La Serp, enfurismada com mai, fumeja i colpeja amb força terrible...»", "Objecte", "serpent_morter", true),
            Card("c_s_final", "Victòria a la Plaça", "«Heu resistit, valents, i la victòria és vostra! El rubí us pertany, i tot el poble, ple d’agraïment, us ofereix un cofre on, dins, descansa un antic pergamí que conté la llegenda sencera.»", "Victòria", "victoria_serpent", true)
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
        
        if (pointId == "s_vaquer") updatePointState("s_rubi", PointState.VISIBLE)
        if (pointId == "s_rubi") startSerpentTimer()
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

    fun updateUserLocation(lat: Double, lng: Double) {
        _userLocation.value = GeoPoint(lat, lng)
    }

    fun checkProximity(userLat: Double, userLng: Double, targetLat: Double, targetLng: Double, threshold: Float = 50f): Pair<Boolean, Float> {
        val userLoc = Location("user").apply { latitude = userLat; longitude = userLng }
        val targetLoc = Location("target").apply { latitude = targetLat; longitude = targetLng }
        val distance = userLoc.distanceTo(targetLoc)
        return Pair(distance <= threshold, distance)
    }
}
