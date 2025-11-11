package com.example.tiendaonline.data.repository

import com.example.tiendaonline.data.dao.ProductDao
import com.example.tiendaonline.data.entity.Product
import kotlinx.coroutines.flow.Flow

/**
 * Repositorio para manejar operaciones relacionadas con productos.
 * Proporciona una capa de abstracción entre la UI y la fuente de datos.
 */
class ProductRepository(private val productDao: ProductDao) {

    /**
     * Obtiene todos los productos.
     */
    val allProducts: Flow<List<Product>> = productDao.getAllProducts()

    /**
     * Obtiene todos los productos disponibles.
     */
    val availableProducts: Flow<List<Product>> = productDao.getAvailableProducts()

    /**
     * Obtiene un producto por su ID.
     */
    suspend fun getProductById(productId: Long): Product? {
        return productDao.getProductById(productId)
    }

    /**
     * Busca productos por nombre.
     */
    fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query)
    }

    /**
     * Obtiene productos por categoría.
     */
    fun getProductsByCategory(category: String): Flow<List<Product>> {
        return productDao.getProductsByCategory(category)
    }

    /**
     * Inserta un nuevo producto.
     */
    suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(product)
    }

    /**
     * Inserta múltiples productos.
     */
    suspend fun insertProducts(products: List<Product>) {
        productDao.insertProducts(products)
    }

    /**
     * Actualiza un producto.
     */
    suspend fun updateProduct(product: Product) {
        productDao.updateProduct(product)
    }

    /**
     * Elimina un producto.
     */
    suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(product)
    }

    /**
     * Elimina todos los productos.
     */
    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }

    /**
     * Actualiza el stock de un producto.
     */
    suspend fun updateStock(productId: Long, newStock: Int) {
        productDao.updateStock(productId, newStock)
    }
}
