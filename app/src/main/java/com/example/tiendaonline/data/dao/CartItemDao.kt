package com.example.tiendaonline.data.dao

import androidx.room.*
import com.example.tiendaonline.data.entity.CartItem
import com.example.tiendaonline.data.entity.CartItemWithProduct
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad CartItem.
 * Define las operaciones de base de datos para items del carrito.
 */
@Dao
interface CartItemDao {

    /**
     * Obtiene todos los items del carrito de un usuario con información del producto.
     */
    @Transaction
    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY addedAt DESC")
    fun getCartItemsWithProduct(userId: Long): Flow<List<CartItemWithProduct>>

    /**
     * Obtiene todos los items del carrito de un usuario.
     */
    @Query("SELECT * FROM cart_items WHERE userId = :userId ORDER BY addedAt DESC")
    fun getCartItems(userId: Long): Flow<List<CartItem>>

    /**
     * Obtiene un item específico del carrito.
     */
    @Query("SELECT * FROM cart_items WHERE userId = :userId AND productId = :productId LIMIT 1")
    suspend fun getCartItem(userId: Long, productId: Long): CartItem?

    /**
     * Obtiene la cantidad total de items en el carrito de un usuario.
     */
    @Query("SELECT COALESCE(SUM(quantity), 0) FROM cart_items WHERE userId = :userId")
    fun getCartItemCount(userId: Long): Flow<Int>

    /**
     * Obtiene el total del carrito (suma de precio * cantidad).
     */
    @Query("""
        SELECT COALESCE(SUM(p.price * c.quantity), 0.0)
        FROM cart_items c
        INNER JOIN products p ON c.productId = p.id
        WHERE c.userId = :userId
    """)
    fun getCartTotal(userId: Long): Flow<Double>

    /**
     * Inserta un item en el carrito.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(cartItem: CartItem): Long

    /**
     * Actualiza un item del carrito.
     */
    @Update
    suspend fun updateCartItem(cartItem: CartItem)

    /**
     * Incrementa la cantidad de un producto en el carrito.
     */
    @Query("UPDATE cart_items SET quantity = quantity + :increment WHERE id = :cartItemId")
    suspend fun incrementQuantity(cartItemId: Long, increment: Int = 1)

    /**
     * Actualiza la cantidad de un producto en el carrito.
     */
    @Query("UPDATE cart_items SET quantity = :newQuantity WHERE id = :cartItemId")
    suspend fun updateQuantity(cartItemId: Long, newQuantity: Int)

    /**
     * Elimina un item del carrito.
     */
    @Delete
    suspend fun deleteCartItem(cartItem: CartItem)

    /**
     * Elimina un item del carrito por IDs.
     */
    @Query("DELETE FROM cart_items WHERE userId = :userId AND productId = :productId")
    suspend fun deleteCartItemByIds(userId: Long, productId: Long)

    /**
     * Vacía el carrito de un usuario.
     */
    @Query("DELETE FROM cart_items WHERE userId = :userId")
    suspend fun clearCart(userId: Long)

    /**
     * Elimina todos los items del carrito.
     */
    @Query("DELETE FROM cart_items")
    suspend fun deleteAllCartItems()
}
