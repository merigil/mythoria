package com.example.mitego.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mitego.logic.GameViewModel
import com.example.mitego.logic.LocationManager
import com.example.mitego.logic.VibrationManager
import com.example.mitego.logic.ViewModelFactory
import com.example.mitego.repository.GameRepository

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    val locationManager = remember { LocationManager(context) }
    val vibrationManager = remember { VibrationManager(context) }
    val viewModel: GameViewModel = viewModel(
        factory = ViewModelFactory(locationManager, vibrationManager)
    )
    
    val repository = viewModel.repository
    val gameState by repository.gameState.collectAsState()

    NavHost(navController = navController, startDestination = "permissions") {
        composable("permissions") {
            WelcomeScreen(
                onPermissionsGranted = {
                    navController.navigate("landing") {
                        popUpTo("permissions") { inclusive = true }
                    }
                }
            )
        }

        composable("landing") {
            LandingScreen(
                onNavigateToMap = {
                    navController.navigate("login") {
                        popUpTo("landing") { inclusive = true }
                    }
                }
            )
        }
        
        composable("login") {
            LoginScreen(
                onLoginClick = {
                    navController.navigate("onboarding") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        
        composable("onboarding") {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate("dashboard") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                onSkip = {
                    navController.navigate("dashboard") {
                        popUpTo("onboarding") { inclusive = true }
                    }
                },
                onNavigateToTrobador = {
                    navController.navigate("trobador/intro")
                }
            )
        }

        composable("trobador/{mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "full"
            TrobadorScreen(
                mode = mode,
                gameState = gameState,
                onClose = { navController.popBackStack() }
            )
        }
        
        composable("dashboard") {
            DashboardScreen(
                onNavigateToMap = { legendId ->
                    repository.startNewLegend(legendId)
                    navController.navigate("map")
                },
                onNavigateToTrobador = {
                    navController.navigate("trobador/intro")
                }
            )
        }

        composable("map") {
            MapScreen(
                repository = repository,
                onPointClick = { point ->
                    val cardId = if (point.id.startsWith("s_")) {
                        point.id.replace("s_", "c_s_")
                    } else {
                        point.id.replace("p_", "c_")
                    }
                    navController.navigate("legend_detail/$cardId")
                },
                onOpenBook = {
                     navController.navigate("dashboard") {
                         popUpTo("map") { inclusive = true }
                     }
                },
                onShowScoreboard = {
                    navController.navigate("scoreboard")
                },
                onNavigateToTrobador = {
                    navController.navigate("trobador/full")
                }
            )
        }
        
        composable("scoreboard") {
            ScoreboardScreen(
                repository = repository,
                onClose = { navController.popBackStack() }
            )
        }
        composable("legend_detail/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
            val card = repository.getCard(cardId)
            
            val pointId = if (cardId.startsWith("c_s_")) {
                cardId.replace("c_s_", "s_")
            } else {
                cardId.replace("c_", "p_")
            }
            
            val pointsList by repository.points.collectAsState()
            val point = pointsList.find { it.id == pointId }
            val score = point?.score ?: 0
            
            if (card != null) {
                LegendDetailScreen(
                    card = card,
                    points = score,
                    point = point,
                    onClose = { 
                        if (point?.type != com.example.mitego.model.PointType.QUIZ) {
                            repository.onPointVisited(pointId)
                        }
                        navController.popBackStack() 
                    },
                    onNavigateToTrobador = {
                        navController.navigate("trobador/full")
                    },
                    onAnswerQuiz = { index ->
                        repository.onQuizAnswered(pointId, index)
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
