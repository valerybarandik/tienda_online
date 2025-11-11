package com.example.tiendaonline.data.repository

import com.example.tiendaonline.data.dao.CartItemDao
import com.example.tiendaonline.data.entity.CartItem
import com.example.tiendaonline.data.entity.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para manejar operaciones relacionadas con el carrito de compras.
 * Proporciona una capa de abstracción entre la UI y la fuente de datos.
 */
class CartRepository(private val cartItemDao: CartItemDao) {

    /**
     * Obtiene todos los items del carrito de un usuario con información del producto.
     */
    fun getCartItemsWithProduct(userId: Long): Flow<List<CartItemWithProduct>> {
        return cartItemDao.getCartItemsWithProduct(userId)
    }

    /**
     * Obtiene todos los items del carrito de un usuario.
     */
    fun getCartItems(userId: Long): Flow<List<CartItem>> {
        return cartItemDao.getCartItems(userId)
    }

    /**
     * Obtiene un item específico del carrito.
     */
    suspend fun getCartItem(userId: Long, productId: Long): CartItem? {
        return cartItemDao.getCartItem(userId, productId)
    }

    /**
     * Obtiene la cantidad total de items en el carrito de un usuario.
     */
    fun getCartItemCount(userId: Long): Flow<Int> {
        return cartItemDao.getCartItemCount(userId)
    }

    /**
     * Obtiene el total del carrito (suma de precio * cantidad).
     */
    fun getCartTotal(userId: Long): Flow<Double> {
        return cartItemDao.getCartTotal(userId)
    }

    /**
     * Agrega un producto al carrito o incrementa su cantidad si ya existe.
     */
    suspend fun addToCart(userId: Long, productId: Long, quantity: Int = 1) {
        val existingItem = cartItemDao.getCartItem(userId, productId)

        if (existingItem != null) {
            // Si el item ya existe, incrementa la cantidad
            cartItemDao.incrementQuantity(existingItem.id, quantity)
        } else {
            // Si no existe, crea un nuevo item
            val newItem = CartItem(
                userId = userId,
                productId = productId,
                quantity = quantity
            )
            cartItemDao.insertCartItem(newItem)
        }
    }

    /**
     * Actualiza la cantidad de un producto en el carrito.
     */
    suspend fun updateQuantity(cartItemId: Long, newQuantity: Int) {
        if (newQuantity <= 0) {
            // Si la cantidad es 0 o menos, elimina el item
            val cartItem = CartItem(id = cartItemId, userId = 0, productId = 0, quantity = 0)
            cartItemDao.deleteCartItem(cartItem)
        } else {
            cartItemDao.updateQuantity(cartItemId, newQuantity)
        }
    }

    /**
     * Elimina un item del carrito.
     */
    suspend fun removeFromCart(cartItem: CartItem) {
        cartItemDao.deleteCartItem(cartItem)
    }

    /**
     * Elimina un item del carrito por IDs.
     */
    suspend fun removeFromCartByIds(userId: Long, productId: Long) {
        cartItemDao.deleteCartItemByIds(userId, productId)
    }

    /**
     * Vacía el carrito de un usuario.
     */
    suspend fun clearCart(userId: Long) {
        cartItemDao.clearCart(userId)
    }

    /**
     * Inserta un item en el carrito.
     */
    suspend fun insertCartItem(cartItem: CartItem): Long {
        return cartItemDao.insertCartItem(cartItem)
    }

    /**
     * Actualiza un item del carrito.
     */
    suspend fun updateCartItem(cartItem: CartItem) {
        cartItemDao.updateCartItem(cartItem)
    }
}
