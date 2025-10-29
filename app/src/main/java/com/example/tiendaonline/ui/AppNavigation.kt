package com.example.tiendaonline.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("productList") { ProductListScreen(navController) }
        composable("shoppingCart") { ShoppingCartScreen(navController) }
        composable("registration") { RegistrationScreen(navController) }
    }
}
