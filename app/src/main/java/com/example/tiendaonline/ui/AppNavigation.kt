package com.example.tiendaonline.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tiendaonline.ui.viewmodel.AuthViewModel
import com.example.tiendaonline.ui.viewmodel.CartViewModel
import com.example.tiendaonline.ui.viewmodel.LocationViewModel
import com.example.tiendaonline.ui.viewmodel.ProductViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // ViewModels compartidos entre pantallas
    val authViewModel: AuthViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()
    val cartViewModel: CartViewModel = viewModel()
    val locationViewModel: LocationViewModel = viewModel()

    NavHost(navController = navController, startDestination = "login") {
        // Pantalla de login
        composable("login") {
            LoginScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Pantalla de registro
        composable("registration") {
            RegistrationScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        // Pantalla de lista de productos (requiere userId)
        composable(
            route = "productList/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            ProductListScreen(
                navController = navController,
                userId = userId,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }

        // Pantalla de carrito de compras (requiere userId)
        composable(
            route = "shoppingCart/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.LongType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getLong("userId") ?: 0L
            ShoppingCartScreen(
                navController = navController,
                userId = userId,
                cartViewModel = cartViewModel
            )
        }

        // Pantalla de geolocalización
        composable("location") {
            LocationScreen(
                onNavigateBack = { navController.popBackStack() },
                locationViewModel = locationViewModel
            )
        }
    }
}
