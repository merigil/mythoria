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
import kotlin.random.Random

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

    private var currentLegendType: String = "BARO"

    init {
        initializeGame()
    }

    private fun initializeGame() {
        _cards.value = createCards()
        _points.value = createPoints().filter { it.id.startsWith("p_") || it.id == "p_start" }
        _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    }

    fun startNewLegend(type: String) {
        currentLegendType = type
        timerJob?.cancel()
        
        val allPoints = createPoints()
        val filteredPoints = when(type) {
            "SERPENT" -> allPoints.filter { it.id.startsWith("s_") }
            else -> allPoints.filter { it.id.startsWith("p_") }
        }
        
        // Marquem el primer punt com a visible
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
            Card("c_baronessa", "La Baronessa", "«El meu espòs mai no ha estat home de cor noble. Els seus gelos ferotges i el seu afany de possessió m’han tinguda captiva entre els murs del castell. Per això mateix, el destí m’ha concedit l’amor veritable, dolç i secret. T’ho suplico: ajuda’ns en aquesta dissort. No permetis que el Baró ens trobi i descarregui la seva ira; avança’t tu als seus passos i troba’l abans que el no ens atrapi.»", "Llegenda", "la_baronessa", true),
            Card("c_jove", "El Jove criat", "«Fidel al meu senyor Baró he estat des que tinc memòria, i he servit la seva house with obediencia ferma. Però per damunt de tota lleialtat hi batega el meu cor, que no coneix juraments ni cadenes. Heus aquí la balma on espero la meva estimada. La Baronessa és ben a prop, ho sent el meu cor abans que els meus ulls la vegin.»", "S’han escrit més parts de la llegenda! Visita La Trobadora.", "el_jove", true),
            Card("c_espassa", "L'Espassa Màgica", "«Aquesta vella espasa, forjada en temps antics, guarda el power sagrat de defensar els cors enamorats. Amb la seva fúria justa, pot fer fugir el Baró, allunyant-lo dels pensaments que volen fer mal als amants.»", "Objecte clau per guanyar.", "la_espassa", true),
            Card("c_baro", "El Baró", "«M’han estat portats a les meves torres fosques rumors d’una traïció vil i imperdonable, teixida per la meva esposa i el meu criat. Oh deshonor que crema com ferro roent al meu pit! Si llur traïdoria es confirma i els meus ulls els troben, tallaré d’arrel els seus caps i aquests rodolaran per la freda pedra del meu castell. Maleïts sigueu.»", "Atura el Baró abans que trobi els dos amants: tens 15 minuts, si encara no ho has fet, per acumular més força que el Baró, trobar l'espasa i parlar with la Baronessa i el Jove Criat.", "el_baro", true),
            Card("c_pastora", "La Pastora", "«El Baró? Un mal home és… i perdona’m si les paraules em fugen aspres. Ningú no sap el que ha patit la pobra baronessa, i nosaltres, humils pastors, encara hem de lliurar-li els millors formatges, com si ens en sobressin!\n\nTant de bo algú li posi fre a tanta supèrbia… Ei, potser podries ser tu. Sé on jeu amagada una espasa antiga, d’ençà dels temps que l’enemic assotaven aquestes terres.»", "mapa_espassa_01", "la_pastora", true),
            Card("c_home_cami", "L'Home del Camí", "«Segueix aquest sender entre els arbres. És més curt i arribaràs abans.»\n\nL’home t’orienta correctament i et fa guanyar punts.", "Llegenda", "home_del_cami", true),
            Card("c_bassa", "La Bassa de la Teuleria i el follet", "Has trobat la Bassa de la Teuleria i, quand t’hi apropes, veus que hi ha un simpàtic follet que canta una melodia estranya i et fa un gest perquè li apropis l’orella.\n\n\"El Baró no vindrà pel camí reial,\nvindrà silent per dalt la balma ancestral;\nTravessa, valent, la balma humida,\nque el seu secret en l’ombra és custodiada;\ncerca la pedra gran, ferma i serena,\nantiga sentinella de la pena;\nsi et queda a l’esquerra, clara i fidel,\na la dreta s’obre el corriol més bell.”", "Et dona 15 punts de força escoltar la cançó i obtens una pista valuosa.", "la_bassa", true),
            Card("c_mercader", "Mercader Ambulant", "«On vas, ànima errant, with sabates tan velles? \nPren-ne de noves, si l’enigma m’assisteix: \nA la capella vora el castell del Baró, with petit campanar, quantes campanes hi cabrien?»", "Pregunta", "mercader_ambulant", true),
            Card("c_prat", "Un prat", "Davant dels teus ulls s’estén un prat que et permet contemplar tota l’esplanada. A l’oest hi veus una torre molt alta que et crida poderosament l’atenció: és la torre del castell del Baró.\n\nAquesta visió t’anima i et fa guanyar 8 punts de força. Endavant!", "Lloc", "el_prat", true),
            Card("c_ancia1", "L'Ancià dels mapes", "«Segueix aquest corriol, car és més breu que cap altre que els teus peus puguin trepitjar. Per ell avançaràs with pas lleuger i hauràs arribat al destí que el teu cor anhela.»", "mapa_cami_01", "lancia_dels_mapes", true),
            Card("c_ancia2", "L'Ancià dels mapes", "«Segueix aquest corriol, car és més breu que cap altre que els teus peus puguin trepitjar. Per ell avançaràs with pas lleuger i hauràs arribat al destí que el teu cor anhela.»", "mapa_baro_01", "lancia_dels_mapes", true),
            Card("c_moneda1", "Moneda Antiga 1", "«Has trobat una moneda antiga! No serveix de gaire, però sempre fa il·lusió, oi? Va, no t’entretinguis!»", "Objecte", "moneda_bronze", true),
            Card("c_moneda2", "Moneda Antiga 2", "«Has trobat una moneda antiga! No t’ajuda, però fa gràcia trobar-ne una. Endavant, no perdis temps!»", "Objecte", "moneda_cobre", true),
            Card("c_moneda3", "Moneda Antiga 3", "«Una altra moneda antiga! No et dona avantatge, però arrenca un somriure. Continua el camí!»", "Objecte", "moneda_or", true),
            Card("c_pastor", "El Pastor", "«El Baró? Gran home, sí! Mes per culpa de sa muller ens té lligats, pagant-li delmes de deu formatges. És capritxosa, sempre vestida with draps luxosos i joies lluents. Ai, totes semblen tallades pel mateix patró! Vés, torna a casa, o acabaràs with el cap entortolligat com el de la meva dona.»", "Et fa enrabiar i et confon; això et fa perdre punts de força.", "el_pastor", true),
            Card("c_clerge", "El Clerga", "«A partir d’aquí no et serà permès de traspassar, pelegrí temerari. Cap ànima creua aquest llindar sense oferir devota pregària… i unes monedes pietoses, perquè jo intercedixi per la teva ànima pecadora davant l’Altíssim.»", "El clergue et reté i, a sobre, et fa pagar unes monedes. No segueixis per aquest camí; torna al camí de sota per arribar a l’entrada del castell.", "el_clerge", true),
            Card("c_nen", "Nen Trepella", "«Ui, ui, sí, sí… ves per aquí, jiji! Aquest és el camí correcte, jeje!»\n\nEl nen t’ha enganyat i t’ha fet anar en mala direcció. Perds punts de vida.", "Trampa", "el_nen_trapella", true),
            Card("c_bota", "Bota de Vi Ranci", "«Trobes una bota de vi i en fas un bon glop. El descans t’entreté més del compte.»", "Perds punts de vida.", "la_bota_de_vi", true),
            Card("c_llops1", "Llops!", "«T’estàs allunyant del camí i els udols dels llops s’acosten. No segueixis per aquí: torna enrere abans que sigui massa tard.»", "Avís", "llops_salvatges", true),
            Card("c_llops2", "Llops!", "«T’estàs allunyant del camí i els udols dels llops s’acosten. No segueixis per aquí: torna enrere abans que sigui massa tard.»", "Avís", "llops_salvatges", true),
            Card("c_llops3", "Llops!", "«T’estàs allunyant del camí i els udols dels llops s’acosten. No segueixis per aquí: torna enrere abans que sigui massa tard.»", "Avís", "llops_salvatges", true),
            Card("c_soldat", "Soldat", "«No pots passar! Sense autorització del senyor d’aquesta terra, hauràs de tornar a la zona del Baró.»", "No perds punts, però millor que no posis a prova la seva paciència… Torna enrere i no vagis endavant.", "soldat_stop", true),
            Card("c_font_baronessa", "La Font de la Baronessa", "«I així, with el Baró foragitat i el perill allunyat, la nostra sort va canviar. La maledicció s’ha trencat i el nostre amor podrà florir per tota l’eternitat.\n\nCom a senyal d’agraïment, et lliurem aquest antic pergamí, que guarda el veritable final de la llegenda d’aquest indret màgic, perquè la seva memòria mai no s’esborri.»", "Consulta la Trobadora per conèixer la totalitat de la llegenda.", "la_font_de_la_baronessa", true),
            
            // --- LA SERPENT DE MANLLEU ---
            Card("c_s_pregoner", "El Pregoner", "«Apropeu-vos, nens i nenes, vells i joves, i escolteu la llegenda que avui encara pot despertar-se. \nLa por recorre els carrers de Manlleu: caps de bestiar desapareixen i, ai las, fins i tot un infant. \nA la llera del riu Ter ha estat vista l’última vegada, una serpent immensa, with una pedra preciosa al front que li dóna poder. \nQui gosi robar-li la joia alliberarà la vila i assolirà riquesa eterna.»", "Llegenda", "serpent_pregoner", true),
            Card("c_s_barquer", "La barquera", "«Ah, escolta’m bé, que no parlo a la lleugera. Diuen que s’ha vist una bestia, sí, la Serpent del riu Ter. No sempre fou monstre, no: abans era dona, jove i orgullosa, plena d’enveja. Però una maledicció cruel la transformà en serp gegantina, condemnant-la a arrossegar-se per les vores del riu, apartada del món dels homes.\nVols travessar a l’altra banda? Fixa’t bé: jo no t’hi portaré, que la por em té pres. Busca aquells de bona fama i pregunta’ls; ells et mostraran el camí segur.»", "Llegenda", "serpent_la_barquera", true),
            Card("c_s_vaquer", "El vaquer", "«Fa dues setmanes tenia 10 vaques, avui en tinc la meitat, una bestia ve per la nit i se les endú i ara també un nen ha desaparegut! Si el rubí algú li treu del cap de ben segur que la serp els seus poders perdrà i podrem respirar tranquils. Està prop del riu, camina des d’aquí, recta cap al riu. Estigues al cas perquè no sabràs on està fins que no hi siguis al damunt.»", "Llegenda", "serpent_el_vaquer", true),
            Card("c_s_rubi", "El rubí de la serpent", "«Heu trobat el rubí encantat, i ahora el temps us pressiona: trenta minuts només per descobrir el morter que posarà fi a la malícia de la bèstia. La serpent, astuta i enfurismada, ha percebut la vostra presència i us persegueix with fúria. Córrer no és només un consell, sinó l’única esperança! Marxeu de pressa, seguiu els camins antics i dirigiu-vos al proper punt del mapa: allà es decidirà el destí vostre… i el de tot Manlleu!»", "Objecte", "el_rubi", true),
            Card("c_s_terrissaire", "El terrissaire", "«Escolta bé, viatger, que no són paraules per a corallers tremolosos! Si vols vèncer aquella serpent que ens amenaça, hauràs de trobar un morter gegant, dur-lo fins al mig de la plaça i amagar-t’hi dins —sí, tal com m’han contat els vells del poble. Allà, hauràs d’aguantar que la bèstia no el pugui trencar with la seva força terrible. \nPerò atenció: jo, humil botiguer de terrissa, no sé pas on trobar un morter tan gran! Ves a veure la vella remeiera, ella sap tot i t’orientarà.»", "Llegenda", "serpent_el_terrisaire", true),
            Card("c_s_remeiera", "La vella remeiera", "«Escolteu-me, fills i filles: el morter gegant s’amaga prop d’aquí, amagat sota les tres creus de la font, just sota l’església. Només els valents i astuts podran trobar-lo i vèncer la Serpent maleïda.»", "Llegenda", "serpent_la_remeiera", true),
            Card("c_s_morter", "El Morter", "«Heu trobat el morter amagat sota les tres creus, però això només és el començament de la vostra prova. La Serp, enfurismada com mai, fumeja i colpeja with força terrible. \nSi voleu triomfar i salvar la vila, heu de fer un pas més: endinsar-vos al cor de la plaça, resistir la seva ràbia i mantenir-vos ferms.»", "Objecte", "serpent_morter", true),
            Card("c_s_final", "Victòria a la Plaça", "«Heu resistit, valents, i la victòria és vostra! El rubí us pertany, i tot el poble, ple d’agraïment, us ofereix un cofre on, dins, descansa un antic pergamí que conté la llegenda sencera.»", "Pots consultar La Trobadora.", "victoria_serpent", true),
            Card("c_s_perdut", "Has perdut el camí!", "«Ai, pobre de tu! Has perdut el camí! Torna enrere, sí, torna fins al punt de la vaqueria, i prepara’t a començar la prova una altra vegada!»", "Derrota", "derrota_serpent", true)
        )
    }

    fun getCard(cardId: String): Card {
        return _cards.value.find { it.id == cardId } 
            ?: Card(cardId, "Indret Desconegut", "Misteri", "Has trobat un lloc misteriós...", "placeholder", true)
    }

    fun updatePointState(pointId: String, newState: PointState) {
        _points.update { list ->
            list.map { if (it.id == pointId) it.copy(state = newState) else it }
        }
    }

    fun onPointVisited(pointId: String) {
        val currentPoints = _points.value
        val point = currentPoints.find { it.id == pointId } ?: return
        
        if (point.state == PointState.COMPLETED || (point.state == PointState.LOCKED && !pointId.endsWith("_start"))) return

        // Si és un QUIZ, no el marquem com a completat fins que es respongui
        if (point.type == PointType.QUIZ) {
            updatePointState(pointId, PointState.VISIBLE) // Ja és visible per poder-hi interactuar
            return
        }

        completePoint(pointId)
    }

    fun onQuizAnswered(pointId: String, selectedIndex: Int) {
        val currentPoints = _points.value
        val point = currentPoints.find { it.id == pointId } ?: return
        val quiz = point.quiz ?: return

        if (point.state == PointState.COMPLETED) return

        val isCorrect = selectedIndex == quiz.correctOptionIndex
        val addedScore = if (isCorrect) quiz.pointsIfCorrect else 0

        _gameState.update { it.copy(totalScore = it.totalScore + addedScore) }
        completePoint(pointId)
    }

    private fun completePoint(pointId: String) {
        val currentPoints = _points.value
        val point = currentPoints.find { it.id == pointId } ?: return
        
        updatePointState(pointId, PointState.COMPLETED)

        _gameState.update { state ->
            val newScore = state.totalScore + (if (point.type != PointType.QUIZ) point.score else 0)
            val newInventory = state.inventory.toMutableSet()
            val newVisited = state.visitedPoints.toMutableSet().apply { add(pointId) }
            
            if (pointId == "p_espassa") newInventory.add("espassa")
            
            var newStatus = state.status
            if (pointId == "p_start" && state.status == GameStatus.WAITING_TO_START) {
                newStatus = GameStatus.ACTIVE_PLAY
                unlockInitialPoints()
            } else if (pointId == "p_baronessa") {
                updatePointState("p_baro", PointState.VISIBLE)
            } else if (pointId == "p_baro") {
                newStatus = GameStatus.BARO_CHALLENGE
                startBaroTimer()
            }
            
            // Lògica seqüencial per a la Serpent de Manlleu
            handleSerpentProgression(pointId)

            state.copy(
                totalScore = if (pointId.startsWith("p_moneda") && newVisited.count { it.startsWith("p_moneda") } == 3) newScore + 15 else newScore,
                inventory = newInventory,
                status = newStatus,
                visitedPoints = newVisited
            )
        }
    }

    private fun handleSerpentProgression(pointId: String) {
        when (pointId) {
            "s_start" -> updatePointState("s_pregoner", PointState.VISIBLE)
            "s_pregoner" -> updatePointState("s_barquer", PointState.VISIBLE)
            "s_barquer" -> updatePointState("s_vaquer", PointState.VISIBLE)
            "s_vaquer" -> updatePointState("s_rubi", PointState.VISIBLE) // Punt invisible (però marcat com visible internament)
            "s_rubi" -> {
                startSerpentTimer()
                updatePointState("s_terrissaire", PointState.VISIBLE)
            }
            "s_terrissaire" -> updatePointState("s_remeiera", PointState.VISIBLE)
            "s_remeiera" -> updatePointState("s_morter", PointState.VISIBLE) // Punt invisible
            "s_morter" -> updatePointState("s_final", PointState.VISIBLE)
            "s_final" -> finalizeSerpentChallenge()
        }
    }

    private fun unlockInitialPoints() {
        _points.update { list ->
            list.map { 
                if (it.state == PointState.LOCKED && it.id != "p_baro" && it.id != "p_font_baronessa" && !it.id.startsWith("s_")) it.copy(state = PointState.VISIBLE) else it
            }
        }
    }

    private fun startBaroTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = BARO_TIMER_SECONDS) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { 
                    val remaining = (it.timerSecondsRemaining ?: 0) - 1
                    it.copy(timerSecondsRemaining = remaining)
                }
                checkWinCondition() 
            }
            checkWinCondition(timeExpired = true)
        }
    }

    private fun startSerpentTimer() {
        if (timerJob?.isActive == true) return
        _gameState.update { it.copy(timerSecondsRemaining = SERPENT_TIMER_SECONDS) }
        timerJob = scope.launch {
            while ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
                delay(1000)
                _gameState.update { 
                    val remaining = (it.timerSecondsRemaining ?: 0) - 1
                    it.copy(timerSecondsRemaining = remaining)
                }
            }
            // Si el temps de la Serpent s'esgota, es perd la prova
            _gameState.update { it.copy(status = GameStatus.LOST) }
        }
    }

    private fun finalizeSerpentChallenge() {
        if ((_gameState.value.timerSecondsRemaining ?: 0) > 0) {
            _gameState.update { it.copy(status = GameStatus.WON, timerSecondsRemaining = null) }
            timerJob?.cancel()
        }
    }
    
    fun checkWinCondition(timeExpired: Boolean = false) {
        val state = _gameState.value
        val hasSword = state.inventory.contains("espassa")
        val hasScore = state.totalScore >= 101
        val hasBaronessa = state.visitedPoints.contains("p_baronessa")
        val hasJove = state.visitedPoints.contains("p_jove")

        if (timeExpired) {
             if (hasSword && hasScore && hasBaronessa && hasJove) {
                 winGame()
             } else {
                 _gameState.update { it.copy(status = GameStatus.LOST) }
             }
        } else {
            if (state.status == GameStatus.BARO_CHALLENGE && hasSword && hasScore && hasBaronessa && hasJove) {
                 winGame()
                 timerJob?.cancel()
            }
        }
    }

    private fun winGame() {
        _gameState.update { it.copy(status = GameStatus.WON, timerSecondsRemaining = null) }
        updatePointState("p_font_baronessa", PointState.VISIBLE)
    }

    fun restartGame() {
        timerJob?.cancel()
        
        if (_gameState.value.status == GameStatus.LOST) {
            // Comprovem si estem a la Serpent o al Baró
            val isSerpent = _points.value.any { it.id.startsWith("s_") && it.state != PointState.LOCKED }
            
            if (isSerpent) {
                // Reinici suau SERPENT: Tornem a la vaqueria
                _gameState.update { it.copy(
                    status = GameStatus.ACTIVE_PLAY,
                    timerSecondsRemaining = null
                )}
                // Tornem a habilitar el vaquer i bloquegem el que venia després
                updatePointState("s_vaquer", PointState.VISIBLE)
                updatePointState("s_rubi", PointState.LOCKED)
                updatePointState("s_terrissaire", PointState.LOCKED)
                updatePointState("s_remeiera", PointState.LOCKED)
                updatePointState("s_morter", PointState.LOCKED)
                updatePointState("s_final", PointState.LOCKED)
            } else {
                // Reinici suau BARÓ
                _gameState.update { it.copy(
                    status = GameStatus.ACTIVE_PLAY,
                    timerSecondsRemaining = null
                )}
                updatePointState("p_baro", PointState.VISIBLE)
            }
        } else {
            // Reinici total (cas normal o guanyat)
            val randomPoints = createPoints()
            _points.value = randomPoints
            _gameState.value = GameState(status = GameStatus.WAITING_TO_START)
    fun updateUserLocation(lat: Double, lng: Double) {
        _userLocation.value = GeoPoint(lat, lng)
    }

    /**
     * Calcula si l'usuari està a prop d'un punt objectiu.
     */
    fun checkProximity(
        userLat: Double, userLng: Double, 
        targetLat: Double, targetLng: Double, 
        threshold: Float = 50f
    ): Pair<Boolean, Float> {
        
        val userLocation = Location("user").apply {
            latitude = userLat
            longitude = userLng
        }
        
        val targetLocation = Location("target").apply {
            latitude = targetLat
            longitude = targetLng
        }
        
        val distance = userLocation.distanceTo(targetLocation)
        
        return Pair(distance <= threshold, distance)
    }

    /**
     * Comprova si un punt es pot recollir basant-se en la proximitat.
     */
    fun canCollectPoint(pointId: String, userLat: Double, userLng: Double): Boolean {
        val point = _points.value.find { it.id == pointId } ?: return false
        val (isNear, _) = checkProximity(userLat, userLng, point.coordinate.latitude, point.coordinate.longitude)
        return isNear
    }
}
