package com.example.mitego.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mitego.repository.GameRepository
import com.example.mitego.ui.screens.LegendBookScreen
import com.example.mitego.ui.screens.LegendDetailScreen
import com.example.mitego.ui.screens.LoginScreen
import com.example.mitego.ui.screens.OnboardingScreen
import com.example.mitego.ui.screens.DashboardScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val repository = remember { GameRepository() } // Simple manual DI

    NavHost(navController = navController, startDestination = "landing") {
        composable("landing") {
            LandingScreen(
                onNavigateToMap = {
                    navController.navigate("login") { // Updated flow: Landing -> Login
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
                adventures = repository.adventures,
                onAdventureSelected = { adventure ->
                    repository.selectAdventure(adventure.id)
                    navController.navigate("map")
                }
            )
        }

        composable("map") {
            MapScreen(
                repository = repository,
                onPointClick = { point ->
                    repository.collectPoint(point.id)
                    val cardId = "c_${point.id.removePrefix("p_")}"
                    navController.navigate("legend_detail/$cardId")
                },
                onOpenBook = {
                    // Start of game / book interaction from Map
                     navController.popBackStack() // Go back to Dashboard? Or showing simpler book?
                     // For now, let's keep it consistent: Map back button goes to Dashboard usually, 
                     // but here we might want a specific in-game menu.
                     // The user said: "Aquesta pantalla (Dashboard) mostra les fitxes...". 
                     // Let's assume onOpenBook goes back to Dashboard for now.
                     navController.navigate("dashboard") {
                         popUpTo("map") { inclusive = true } // Or keep map in stack?
                     }
                }
            )
        }
        composable("legend_detail/{cardId}") { backStackEntry ->
            val cardId = backStackEntry.arguments?.getString("cardId")
            val card = repository.getCard(cardId ?: "")
            
            if (card != null) {
                LegendDetailScreen(
                    card = card,
                    onClose = { navController.popBackStack() }
                )
            }
        }
        // Removed old LegendBookScreen route as it's replaced by Dashboard
    }
}
