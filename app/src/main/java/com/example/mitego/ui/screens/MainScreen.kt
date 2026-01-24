package com.example.mitego.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mitego.logic.GameViewModel
import com.example.mitego.logic.LocationManager
import com.example.mitego.logic.ViewModelFactory
import com.example.mitego.repository.GameRepository

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val context = LocalContext.current
    
    // Inicialitzem el LocationManager i el ViewModel amb la factoria
    val locationManager = remember { LocationManager(context) }
    val viewModel: GameViewModel = viewModel(
        factory = ViewModelFactory(locationManager)
    )
    
    // Utilitzem el repositori del ViewModel per a tota l'App
    val repository = viewModel.repository

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
                onClose = { navController.popBackStack() }
            )
        }
        
        composable("dashboard") {
            DashboardScreen(
                onNavigateToMap = {
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
                    repository.onPointVisited(point.id)
                    val cardId = point.id.replace("p_", "c_")
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
            val cardId = backStackEntry.arguments?.getString("cardId")
            val card = repository.getCard(cardId ?: "")
            
            val pointId = cardId?.replace("c_", "p_")
            val point = repository.points.collectAsState().value.find { it.id == pointId }
            val score = point?.score ?: 0
            
            if (card != null) {
                LegendDetailScreen(
                    card = card,
                    points = score,
                    onClose = { navController.popBackStack() }
                )
            }
        }
    }
}
