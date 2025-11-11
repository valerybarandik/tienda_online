package com.example.tiendaonline.data.repository

import com.example.tiendaonline.data.dao.UserDao
import com.example.tiendaonline.data.entity.User
import kotlinx.coroutines.flow.Flow
import java.security.MessageDigest

/**
 * Repositorio para manejar operaciones relacionadas con usuarios.
 * Proporciona una capa de abstracción entre la UI y la fuente de datos.
 */
class UserRepository(private val userDao: UserDao) {

    /**
     * Obtiene todos los usuarios.
     */
    val allUsers: Flow<List<User>> = userDao.getAllUsers()

    /**
     * Obtiene un usuario por su ID.
     */
    suspend fun getUserById(userId: Long): User? {
        return userDao.getUserById(userId)
    }

    /**
     * Obtiene un usuario por su email.
     */
    suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)
    }

    /**
     * Verifica si existe un usuario con el email dado.
     */
    suspend fun emailExists(email: String): Boolean {
        return userDao.emailExists(email)
    }

    /**
     * Intenta hacer login con email y password.
     * @return El usuario si las credenciales son correctas, null si no.
     */
    suspend fun login(email: String, password: String): User? {
        val passwordHash = hashPassword(password)
        return userDao.login(email, passwordHash)
    }

    /**
     * Registra un nuevo usuario.
     * @return El ID del usuario creado si fue exitoso, -1 si el email ya existe.
     */
    suspend fun registerUser(name: String, email: String, password: String): Long {
        // Verifica si el email ya existe
        if (emailExists(email)) {
            return -1L
        }

        val passwordHash = hashPassword(password)
        val user = User(
            name = name,
            email = email,
            passwordHash = passwordHash
        )

        return try {
            userDao.insertUser(user)
        } catch (e: Exception) {
            -1L
        }
    }

    /**
     * Actualiza un usuario.
     */
    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    /**
     * Elimina un usuario.
     */
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user)
    }

    /**
     * Actualiza el estado activo de un usuario.
     */
    suspend fun updateUserStatus(userId: Long, isActive: Boolean) {
        userDao.updateUserStatus(userId, isActive)
    }

    /**
     * Genera un hash simple de la contraseña usando SHA-256.
     * NOTA: En un proyecto real deberías usar bcrypt o similar con salt.
     */
    private fun hashPassword(password: String): String {
        val bytes = password.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}
