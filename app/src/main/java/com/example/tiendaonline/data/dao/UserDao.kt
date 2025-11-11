package com.example.tiendaonline.data.dao

import androidx.room.*
import com.example.tiendaonline.data.entity.User
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad User.
 * Define las operaciones de base de datos para usuarios.
 */
@Dao
interface UserDao {

    /**
     * Obtiene todos los usuarios.
     */
    @Query("SELECT * FROM users ORDER BY name ASC")
    fun getAllUsers(): Flow<List<User>>

    /**
     * Obtiene un usuario por su ID.
     */
    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Long): User?

    /**
     * Obtiene un usuario por su email.
     */
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): User?

    /**
     * Verifica si existe un usuario con el email dado.
     */
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email LIMIT 1)")
    suspend fun emailExists(email: String): Boolean

    /**
     * Valida las credenciales de login de un usuario.
     */
    @Query("SELECT * FROM users WHERE email = :email AND passwordHash = :passwordHash LIMIT 1")
    suspend fun login(email: String, passwordHash: String): User?

    /**
     * Inserta un nuevo usuario.
     * Retorna el ID del usuario insertado.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: User): Long

    /**
     * Actualiza un usuario existente.
     */
    @Update
    suspend fun updateUser(user: User)

    /**
     * Elimina un usuario.
     */
    @Delete
    suspend fun deleteUser(user: User)

    /**
     * Actualiza el estado activo de un usuario.
     */
    @Query("UPDATE users SET isActive = :isActive WHERE id = :userId")
    suspend fun updateUserStatus(userId: Long, isActive: Boolean)
}
