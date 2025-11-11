package com.example.tiendaonline.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Product para la base de datos Room.
 * Representa un producto en la tienda online.
 */
@Entity(tableName = "products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val description: String = "",

    val price: Double,

    val category: String = "General",

    val stock: Int = 0,

    // URL de la imagen o nombre del recurso drawable
    val imageUrl: String = "",

    val isAvailable: Boolean = true,

    val createdAt: Long = System.currentTimeMillis()
)
