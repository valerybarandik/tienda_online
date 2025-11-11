package com.example.tiendaonline.data.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Clase de datos que representa un CartItem con la información completa del Product.
 * Room usa esta clase para hacer JOIN entre las tablas cart_items y products.
 */
data class CartItemWithProduct(
    @Embedded
    val cartItem: CartItem,

    @Relation(
        parentColumn = "productId",
        entityColumn = "id"
    )
    val product: Product
)
