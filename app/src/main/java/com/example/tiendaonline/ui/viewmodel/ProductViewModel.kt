package com.example.tiendaonline.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaonline.data.database.AppDatabase
import com.example.tiendaonline.data.entity.Product
import com.example.tiendaonline.data.repository.ProductRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar la lógica relacionada con productos.
 */
class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ProductRepository

    // StateFlow para la lista de productos
    val products: StateFlow<List<Product>>

    // StateFlow para el estado de búsqueda
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        val productDao = AppDatabase.getDatabase(application).productDao()
        repository = ProductRepository(productDao)

        // Observa los productos disponibles
        products = repository.availableProducts.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    /**
     * Actualiza la búsqueda de productos.
     */
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    /**
     * Busca productos por nombre.
     */
    fun searchProducts(query: String): StateFlow<List<Product>> {
        return repository.searchProducts(query).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    /**
     * Obtiene un producto por su ID.
     */
    suspend fun getProductById(productId: Long): Product? {
        return repository.getProductById(productId)
    }

    /**
     * Inserta un nuevo producto.
     */
    fun insertProduct(product: Product) {
        viewModelScope.launch {
            repository.insertProduct(product)
        }
    }

    /**
     * Actualiza un producto existente.
     */
    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    /**
     * Elimina un producto.
     */
    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
        }
    }

    /**
     * Actualiza el stock de un producto.
     */
    fun updateStock(productId: Long, newStock: Int) {
        viewModelScope.launch {
            repository.updateStock(productId, newStock)
        }
    }
}
