package com.cacamites.app.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.cacamites.app.repository.GameRepository

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val repository = remember { GameRepository() }

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
                onAdventureSelected = { type ->
                    repository.startNewLegend(type)
                    navController.navigate("map")
                }
            )
        }

        composable("map") {
            MapScreen(
                repository = repository,
                onPointClick = { point ->
                    repository.onPointVisited(point.id)
                    val cardId = if (point.id.startsWith("s_")) "c_s_${point.id.removePrefix("s_")}" else "c_${point.id.removePrefix("p_")}"
                    navController.navigate("legend_detail/$cardId")
                },
                onOpenBook = {
                     navController.navigate("dashboard") {
                         popUpTo("map") { inclusive = true }
                     }
                }
            )
        }
        
        composable("legend_detail/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")
            val card = repository.getCard(cardId ?: "")
            
            LegendDetailScreen(
                card = card,
                onClose = { navController.popBackStack() },
                repository = repository
            )
        }
    }
}

