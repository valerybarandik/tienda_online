package com.example.tiendaonline.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.tiendaonline.R
import com.example.tiendaonline.ui.theme.White

data class Product(val name: String, val price: Double, @DrawableRes val imageRes: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(navController: NavController) {
    val products = listOf(
        Product("Producto 1", 10.0, R.drawable.ic_launcher_background),
        Product("Producto 2", 20.0, R.drawable.ic_launcher_background),
        Product("Producto 3", 30.0, R.drawable.ic_launcher_background),
        Product("Producto 4", 40.0, R.drawable.ic_launcher_background),
        Product("Producto 5", 50.0, R.drawable.ic_launcher_background)
    )

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TitleTopAppBar(
                title = "Productos",
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "User",
                            tint = White
                        )
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                navController.navigate("login") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(product = product)
                }
            }
            Button(
                onClick = { navController.navigate("shoppingCart") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Ver Carrito")
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = product.name)
                Text(text = "$${product.price}")
                Button(
                    onClick = { /* Add to cart */ },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    }
}
