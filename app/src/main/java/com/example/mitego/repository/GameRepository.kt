package com.example.mitego.repository

import com.example.mitego.model.Adventure
import com.example.mitego.model.Card
import com.example.mitego.model.GameResult
import com.example.mitego.model.GameState
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.model.PointType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import java.util.Random

class GameRepository {

    private val BARONESSA_CENTER = GeoPoint(41.9323395951151, 2.252533598712291)
    
    // Game State
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    private val _points = MutableStateFlow<List<Point>>(emptyList())
    val points: StateFlow<List<Point>> = _points.asStateFlow()

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    private val gameScope = CoroutineScope(Dispatchers.Main)

    // Adventure Definition
    private val baronessaAdventure by lazy {
        val generatedPoints = generateBaronessaPoints()
        val cards = generateBaronessaCards(generatedPoints)
        
        Adventure(
            id = "adv_baronessa",
            title = "La Font de la Baronessa",
            description = "Descobreix els secrets de la Baronessa...",
            imageUrl = "",
            points = generatedPoints,
            cards = cards,
            isActive = true
        )
    }

    private val castellAdventure = Adventure(
        id = "adv_castell",
        title = "El Castell Perdut",
        description = "Explora les ruïnes de l'antic castell...",
        imageUrl = "",
        points = emptyList(),
        cards = emptyList(),
        isActive = false
    )

    val adventures: List<Adventure> = listOf(baronessaAdventure, castellAdventure)
    private var currentAdventure: Adventure? = null

    fun selectAdventure(adventureId: String) {
        val adventure = adventures.find { it.id == adventureId }
        if (adventure != null) {
            currentAdventure = adventure
            _points.value = adventure.points
            _cards.value = adventure.cards
            startGame()
        }
    }

    private fun startGame() {
        _gameState.value = GameState(isGameActive = true, currentScore = 0)
        // Set all points locked except start point if needed, or visible based on rules
        // For now, assuming start point unlocks others or all visible for MVP exploration
    }

    fun collectPoint(pointId: String) {
        val currentPoints = _points.value.toMutableList()
        val pointIndex = currentPoints.indexOfFirst { it.id == pointId }
        
        if (pointIndex != -1) {
            val point = currentPoints[pointIndex]
            if (point.state == PointState.COMPLETED) return

            // Update Point State
            val updatedPoint = point.copy(state = PointState.COMPLETED)
            currentPoints[pointIndex] = updatedPoint
            _points.value = currentPoints

            // Update Game State
            _gameState.update { state ->
                val newScore = state.currentScore + point.scoreValue
                val newKeys = if (point.isKeyItem) state.keyItemsCollected + point.id else state.keyItemsCollected
                
                var newState = state.copy(
                    currentScore = newScore,
                    keyItemsCollected = newKeys
                )

                // Check Baron Logic
                if (point.id == "p_baro") {
                    newState = newState.copy(isTimerActive = true)
                    startTimer()
                }

                // Check Win Condition (collected items + score > 100 + visited Baron)
                // Note: User rule says "win when time ends AND we have everything".
                // But usually, you win immediately if you defeat the boss? 
                // Let's follow: "Quan s'acaba el temps i tenim tot... guanyem".
                // So here we just update state. Check win on timer end.
                
                newState
            }
            
            // Unlock Card if exists
            unlockCard("c_${pointId.removePrefix("p_")}")
        }
    }

    private fun startTimer() {
        gameScope.launch {
            while (_gameState.value.isTimerActive && _gameState.value.timeRemainingMs > 0) {
                delay(1000)
                _gameState.update { 
                    it.copy(timeRemainingMs = it.timeRemainingMs - 1000) 
                }
            }
            if (_gameState.value.isTimerActive) {
                checkEndGameCondition()
            }
        }
    }

    private fun checkEndGameCondition() {
        _gameState.update { state ->
            val hasWon = state.hasWon()
            state.copy(
                isGameActive = false,
                isTimerActive = false,
                gameResult = if (hasWon) GameResult.WIN else GameResult.DEFEAT_TIME
            )
        }
    }

    fun getCard(cardId: String): Card? {
        return _cards.value.find { it.id == cardId }
    }
    
    fun unlockCard(cardId: String) {
        // Implementation to unlock logic if needed separate from collectPoint
    }

    // --- Data Generation ---

    private fun generateBaronessaPoints(): List<Point> {
        return listOf(
            createPoint("p_inici", "Inici del Joc", 0.0, 0.0, PointType.MANDATORY, 0, false, PointState.VISIBLE), // Start visible
            createPoint("p_jove", "El Jove", 100.0, 50.0, PointType.MANDATORY, 15, true),
            createPoint("p_baronessa", "La Baronessa", BARONESSA_CENTER.latitude, BARONESSA_CENTER.longitude, PointType.MANDATORY, 12, true, PointState.VISIBLE, true), // Center
            createPoint("p_espasa", "L'Espasa", -50.0, 100.0, PointType.MANDATORY, 30, true),
            createPoint("p_baro", "El Baró", 400.0, 400.0, PointType.ENEMY, 0, false), // Far away
            
            // Optional Points
            createPoint("p_nen", "Nen Trapella", 50.0, -50.0, PointType.NARRATIVE, -5, false),
            createPoint("p_mercader", "Mercader Ambulant", -100.0, -100.0, PointType.OBJECT, 15, false),
            createPoint("p_home", "Home del Camí", 150.0, 0.0, PointType.NARRATIVE, 15, false),
            createPoint("p_pedra", "Pedra Alta", 200.0, 100.0, PointType.OBJECT, 15, false),
            createPoint("p_pont", "Pont Antic", 0.0, 200.0, PointType.OBJECT, 15, false),
            createPoint("p_pastora", "La Pastora", -150.0, 50.0, PointType.NARRATIVE, 10, false),
            createPoint("p_pastor", "El Pastor", -200.0, -50.0, PointType.NARRATIVE, -10, false),
            createPoint("p_cavall", "El Cavall", 300.0, 50.0, PointType.NARRATIVE, -5, false),
            createPoint("p_vi", "La Bota de Vi", -50.0, 300.0, PointType.OBJECT, -8, false),
            createPoint("p_moneda1", "Moneda de Plata 1", 250.0, -150.0, PointType.OBJECT, 0, false),
            createPoint("p_moneda2", "Moneda de Plata 2", -250.0, 250.0, PointType.OBJECT, 0, false),
            createPoint("p_moneda3", "Moneda de Plata 3", 100.0, -300.0, PointType.OBJECT, 0, false),
            createPoint("p_extra", "Racó Secret", -350.0, 50.0, PointType.SURPRISE, 0, false) // 18th point
        )
    }

    // Helper to offset coordinates in meters from center
    private fun createPoint(id: String, title: String, offLatM: Double, offLonM: Double, type: PointType, score: Int, isKey: Boolean, initialState: PointState = PointState.LOCKED, isAbsolute: Boolean = false): Point {
        // Approx conversion: 1 deg lat ~= 111km, 1 deg lon ~= 111km * cos(lat)
        val lat: Double
        val lon: Double
        
        if (isAbsolute) {
             lat = offLatM
             lon = offLonM
        } else {
             val latOffset = offLatM / 111111.0
             val lonOffset = offLonM / (111111.0 * Math.cos(Math.toRadians(BARONESSA_CENTER.latitude)))
             lat = BARONESSA_CENTER.latitude + latOffset
             lon = BARONESSA_CENTER.longitude + lonOffset
        }

        return Point(
            id = id,
            title = title,
            coordinate = GeoPoint(lat, lon),
            type = type,
            state = initialState,
            scoreValue = score,
            isKeyItem = isKey
        )
    }
    
    private fun generateBaronessaCards(points: List<Point>): List<Card> {
        return points.map { point ->
            Card(
                id = "c_${point.id.removePrefix("p_")}",
                title = point.title,
                type = "Llegenda",
                description = "Has trobat: ${point.title}. ${if(point.scoreValue != 0) "Punts: ${point.scoreValue}" else ""}",
                imageUrl = "",
                isUnlocked = false
            )
        }.toMutableList().apply {
            // Update specific descriptions
            find { it.id == "c_baronessa" }?.description = """
                Conta la llegenda que, en temps antics, un baró malcarat i gelós es va casar amb una dona jove i molt bella, a qui tractava més com una possessió que no pas com una esposa. En haver de marxar a la guerra, dominat per la desconfiança, la va deixar sota la vigilància d’un criat fidel, amb l’ordre de controlar-la perquè no li fos infidel.

                La solitud i la convivència, però, van fer néixer entre la baronessa i el criat un amor sincer i prohibit. A poc a poc, trobaven refugi a la balma de la Font de la Baronessa, on expressaven el seu amor en secret, envoltats del murmuri de l’aigua.

                Quan el baró va tornar inesperadament, els va sorprendre junts. Cegat per la fúria i l’orgull ferit, els va matar a tots dos. Des d’aleshores, diu la tradició que les nits de lluna plena se senten els plors de la baronessa a la font, i que qui els escolta ha patit una infidelitat.
            """.trimIndent()
        }
    }
}
