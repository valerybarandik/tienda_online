package com.example.tiendaonline.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.tiendaonline.data.dao.CartItemDao
import com.example.tiendaonline.data.dao.ProductDao
import com.example.tiendaonline.data.dao.UserDao
import com.example.tiendaonline.data.entity.CartItem
import com.example.tiendaonline.data.entity.Product
import com.example.tiendaonline.data.entity.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Base de datos principal de la aplicación usando Room.
 * Define las entidades y proporciona acceso a los DAOs.
 */
@Database(
    entities = [
        Product::class,
        User::class,
        CartItem::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    // DAOs
    abstract fun productDao(): ProductDao
    abstract fun userDao(): UserDao
    abstract fun cartItemDao(): CartItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Obtiene la instancia singleton de la base de datos.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tienda_online_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }

        /**
         * Callback para poblar la base de datos con datos de ejemplo al crearla.
         */
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        /**
         * Puebla la base de datos con datos de ejemplo.
         */
        private suspend fun populateDatabase(database: AppDatabase) {
            val productDao = database.productDao()

            // Inserta productos de ejemplo
            val sampleProducts = listOf(
                Product(
                    name = "Laptop Gaming",
                    description = "Laptop de alto rendimiento para gaming y trabajo",
                    price = 1299.99,
                    category = "Electrónica",
                    stock = 10,
                    imageUrl = "laptop_gaming",
                    isAvailable = true
                ),
                Product(
                    name = "Mouse Inalámbrico",
                    description = "Mouse ergonómico con conexión Bluetooth",
                    price = 29.99,
                    category = "Accesorios",
                    stock = 50,
                    imageUrl = "mouse",
                    isAvailable = true
                ),
                Product(
                    name = "Teclado Mecánico",
                    description = "Teclado mecánico RGB para gaming",
                    price = 89.99,
                    category = "Accesorios",
                    stock = 30,
                    imageUrl = "keyboard",
                    isAvailable = true
                ),
                Product(
                    name = "Monitor 27 pulgadas",
                    description = "Monitor 4K UHD con tecnología IPS",
                    price = 399.99,
                    category = "Electrónica",
                    stock = 15,
                    imageUrl = "monitor",
                    isAvailable = true
                ),
                Product(
                    name = "Auriculares Bluetooth",
                    description = "Auriculares inalámbricos con cancelación de ruido",
                    price = 149.99,
                    category = "Audio",
                    stock = 25,
                    imageUrl = "headphones",
                    isAvailable = true
                ),
                Product(
                    name = "Webcam HD",
                    description = "Cámara web Full HD 1080p",
                    price = 59.99,
                    category = "Accesorios",
                    stock = 40,
                    imageUrl = "webcam",
                    isAvailable = true
                ),
                Product(
                    name = "SSD 1TB",
                    description = "Disco sólido NVMe de alta velocidad",
                    price = 119.99,
                    category = "Almacenamiento",
                    stock = 20,
                    imageUrl = "ssd",
                    isAvailable = true
                ),
                Product(
                    name = "Router WiFi 6",
                    description = "Router de última generación con WiFi 6",
                    price = 179.99,
                    category = "Redes",
                    stock = 12,
                    imageUrl = "router",
                    isAvailable = true
                )
            )

            productDao.insertProducts(sampleProducts)
        }
    }
}
