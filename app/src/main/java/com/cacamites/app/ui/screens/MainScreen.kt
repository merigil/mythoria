package com.cacamites.app.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cacamites.app.viewmodel.GameViewModel
import com.cacamites.app.repository.GameRepository

@Composable
fun MainScreen(viewModel: GameViewModel = viewModel()) {
    val navController = rememberNavController()
    val repository = viewModel.repository
    val context = LocalContext.current

    NavHost(navController = navController, startDestination = "landing") {
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
                }
            )
        }
        
        composable("dashboard") {
            DashboardScreen(
                onNavigateToMap = { type ->
                    repository.startNewLegend(type)
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
                    val cardId = "c_${point.id}"
                    navController.navigate("legend_detail/$cardId")
                },
                onOpenBook = {
                     navController.navigate("dashboard")
                },
                onShowScoreboard = {
                    Toast.makeText(context, "Rànquing en temps real (Redis) properament", Toast.LENGTH_SHORT).show()
                },
                onNavigateToTrobador = {
                    navController.navigate("trobador/full")
                }
            )
        }
        
        composable("trobador/{mode}") { backStackEntry ->
            val mode = backStackEntry.arguments?.getString("mode") ?: "full"
            val gameState by repository.gameState.collectAsState()
            TrobadorScreen(
                mode = mode,
                gameState = gameState,
                onClose = { navController.popBackStack() }
            )
        }

        composable("legend_detail/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId") ?: ""
            val card = repository.getCard(cardId)
            
            LegendDetailScreen(
                card = card,
                onClose = { navController.popBackStack() },
                repository = repository
            )
        }
    }
}
