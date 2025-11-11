package com.example.tiendaonline.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaonline.data.database.AppDatabase
import com.example.tiendaonline.data.entity.CartItem
import com.example.tiendaonline.data.entity.CartItemWithProduct
import com.example.tiendaonline.data.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica del carrito de compras.
 */
class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CartRepository

    // ID del usuario actual (por ahora hardcodeado, debería venir de AuthViewModel)
    private val _currentUserId = MutableStateFlow<Long?>(null)
    val currentUserId: StateFlow<Long?> = _currentUserId.asStateFlow()

    // Items del carrito con información del producto
    private val _cartItems = MutableStateFlow<List<CartItemWithProduct>>(emptyList())
    val cartItems: StateFlow<List<CartItemWithProduct>> = _cartItems.asStateFlow()

    // Cantidad total de items en el carrito
    private val _cartItemCount = MutableStateFlow(0)
    val cartItemCount: StateFlow<Int> = _cartItemCount.asStateFlow()

    // Total del carrito
    private val _cartTotal = MutableStateFlow(0.0)
    val cartTotal: StateFlow<Double> = _cartTotal.asStateFlow()

    init {
        val cartItemDao = AppDatabase.getDatabase(application).cartItemDao()
        repository = CartRepository(cartItemDao)
    }

    /**
     * Establece el usuario actual y carga su carrito.
     */
    fun setCurrentUser(userId: Long) {
        _currentUserId.value = userId
        loadCartData(userId)
    }

    /**
     * Carga los datos del carrito para un usuario.
     */
    private fun loadCartData(userId: Long) {
        // Observar items del carrito
        viewModelScope.launch {
            repository.getCartItemsWithProduct(userId).collect { items ->
                _cartItems.value = items
            }
        }

        // Observar cantidad de items
        viewModelScope.launch {
            repository.getCartItemCount(userId).collect { count ->
                _cartItemCount.value = count
            }
        }

        // Observar total del carrito
        viewModelScope.launch {
            repository.getCartTotal(userId).collect { total ->
                _cartTotal.value = total
            }
        }
    }

    /**
     * Agrega un producto al carrito.
     */
    fun addToCart(productId: Long, quantity: Int = 1) {
        val userId = _currentUserId.value ?: return

        viewModelScope.launch {
            try {
                repository.addToCart(userId, productId, quantity)
            } catch (e: Exception) {
                // Manejar error
                e.printStackTrace()
            }
        }
    }

    /**
     * Actualiza la cantidad de un item en el carrito.
     */
    fun updateQuantity(cartItemId: Long, newQuantity: Int) {
        viewModelScope.launch {
            try {
                repository.updateQuantity(cartItemId, newQuantity)
            } catch (e: Exception) {
                // Manejar error
                e.printStackTrace()
            }
        }
    }

    /**
     * Incrementa la cantidad de un item en el carrito.
     */
    fun incrementQuantity(cartItemId: Long) {
        val item = _cartItems.value.find { it.cartItem.id == cartItemId }
        item?.let {
            updateQuantity(cartItemId, it.cartItem.quantity + 1)
        }
    }

    /**
     * Decrementa la cantidad de un item en el carrito.
     */
    fun decrementQuantity(cartItemId: Long) {
        val item = _cartItems.value.find { it.cartItem.id == cartItemId }
        item?.let {
            if (it.cartItem.quantity > 1) {
                updateQuantity(cartItemId, it.cartItem.quantity - 1)
            } else {
                removeFromCart(it.cartItem)
            }
        }
    }

    /**
     * Elimina un item del carrito.
     */
    fun removeFromCart(cartItem: CartItem) {
        viewModelScope.launch {
            try {
                repository.removeFromCart(cartItem)
            } catch (e: Exception) {
                // Manejar error
                e.printStackTrace()
            }
        }
    }

    /**
     * Elimina un item del carrito por IDs.
     */
    fun removeFromCartByIds(productId: Long) {
        val userId = _currentUserId.value ?: return

        viewModelScope.launch {
            try {
                repository.removeFromCartByIds(userId, productId)
            } catch (e: Exception) {
                // Manejar error
                e.printStackTrace()
            }
        }
    }

    /**
     * Vacía el carrito del usuario actual.
     */
    fun clearCart() {
        val userId = _currentUserId.value ?: return

        viewModelScope.launch {
            try {
                repository.clearCart(userId)
            } catch (e: Exception) {
                // Manejar error
                e.printStackTrace()
            }
        }
    }
}
