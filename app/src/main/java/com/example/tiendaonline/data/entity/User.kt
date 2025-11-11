package com.example.tiendaonline.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad User para la base de datos Room.
 * Representa un usuario registrado en la aplicación.
 */
@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val email: String,

    // En un proyecto real, esto debería ser un hash seguro (bcrypt, etc.)
    val passwordHash: String,

    val isActive: Boolean = true,

    val createdAt: Long = System.currentTimeMillis()
)
