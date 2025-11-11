package com.example.tiendaonline.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaonline.data.database.AppDatabase
import com.example.tiendaonline.data.entity.User
import com.example.tiendaonline.data.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la autenticación de usuarios (login y registro).
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    // Estado del usuario actual
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    // Estado de login/registro
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    // Mensaje de error
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        val userDao = AppDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
    }

    /**
     * Intenta hacer login con email y password.
     */
    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                // Validaciones básicas
                if (email.isBlank() || password.isBlank()) {
                    _errorMessage.value = "Email y contraseña son requeridos"
                    _authState.value = AuthState.Error
                    return@launch
                }

                val user = repository.login(email, password)

                if (user != null) {
                    _currentUser.value = user
                    _authState.value = AuthState.Success
                    _errorMessage.value = null
                } else {
                    _errorMessage.value = "Email o contraseña incorrectos"
                    _authState.value = AuthState.Error
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al iniciar sesión: ${e.message}"
                _authState.value = AuthState.Error
            }
        }
    }

    /**
     * Registra un nuevo usuario.
     */
    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                // Validaciones básicas
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    _errorMessage.value = "Todos los campos son requeridos"
                    _authState.value = AuthState.Error
                    return@launch
                }

                if (!isValidEmail(email)) {
                    _errorMessage.value = "Email inválido"
                    _authState.value = AuthState.Error
                    return@launch
                }

                if (password.length < 6) {
                    _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
                    _authState.value = AuthState.Error
                    return@launch
                }

                val userId = repository.registerUser(name, email, password)

                if (userId > 0) {
                    // Registro exitoso, ahora hacer login automático
                    login(email, password)
                } else {
                    _errorMessage.value = "El email ya está registrado"
                    _authState.value = AuthState.Error
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrar usuario: ${e.message}"
                _authState.value = AuthState.Error
            }
        }
    }

    /**
     * Cierra la sesión del usuario actual.
     */
    fun logout() {
        _currentUser.value = null
        _authState.value = AuthState.Idle
        _errorMessage.value = null
    }

    /**
     * Limpia el mensaje de error.
     */
    fun clearError() {
        _errorMessage.value = null
        if (_authState.value == AuthState.Error) {
            _authState.value = AuthState.Idle
        }
    }

    /**
     * Valida si un email tiene formato válido.
     */
    private fun isValidEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    /**
     * Estados posibles de autenticación.
     */
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        object Success : AuthState()
        object Error : AuthState()
    }
}
