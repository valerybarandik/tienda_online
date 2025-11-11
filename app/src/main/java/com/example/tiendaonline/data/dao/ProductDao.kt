package com.example.tiendaonline.data.dao

import androidx.room.*
import com.example.tiendaonline.data.entity.Product
import kotlinx.coroutines.flow.Flow

/**
 * DAO (Data Access Object) para la entidad Product.
 * Define las operaciones de base de datos para productos.
 */
@Dao
interface ProductDao {

    /**
     * Obtiene todos los productos como Flow para observar cambios en tiempo real.
     */
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<Product>>

    /**
     * Obtiene todos los productos disponibles.
     */
    @Query("SELECT * FROM products WHERE isAvailable = 1 ORDER BY name ASC")
    fun getAvailableProducts(): Flow<List<Product>>

    /**
     * Obtiene un producto por su ID.
     */
    @Query("SELECT * FROM products WHERE id = :productId")
    suspend fun getProductById(productId: Long): Product?

    /**
     * Busca productos por nombre.
     */
    @Query("SELECT * FROM products WHERE name LIKE '%' || :searchQuery || '%' ORDER BY name ASC")
    fun searchProducts(searchQuery: String): Flow<List<Product>>

    /**
     * Obtiene productos por categoría.
     */
    @Query("SELECT * FROM products WHERE category = :category ORDER BY name ASC")
    fun getProductsByCategory(category: String): Flow<List<Product>>

    /**
     * Inserta un nuevo producto.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: Product): Long

    /**
     * Inserta múltiples productos.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<Product>)

    /**
     * Actualiza un producto existente.
     */
    @Update
    suspend fun updateProduct(product: Product)

    /**
     * Elimina un producto.
     */
    @Delete
    suspend fun deleteProduct(product: Product)

    /**
     * Elimina todos los productos.
     */
    @Query("DELETE FROM products")
    suspend fun deleteAllProducts()

    /**
     * Actualiza el stock de un producto.
     */
    @Query("UPDATE products SET stock = :newStock WHERE id = :productId")
    suspend fun updateStock(productId: Long, newStock: Int)
}
