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
    version = 6,
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
                    .fallbackToDestructiveMigration()
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

            override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
                super.onDestructiveMigration(db)
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
                    imageUrl = "https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Mouse Inalámbrico",
                    description = "Mouse ergonómico con conexión Bluetooth",
                    price = 29.99,
                    category = "Accesorios",
                    stock = 50,
                    imageUrl = "https://images.unsplash.com/photo-1527864550417-7fd91fc51a46?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Teclado Mecánico",
                    description = "Teclado mecánico RGB para gaming",
                    price = 89.99,
                    category = "Accesorios",
                    stock = 30,
                    imageUrl = "https://images.unsplash.com/photo-1587829741301-dc798b83add3?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Monitor 27 pulgadas",
                    description = "Monitor 4K UHD con tecnología IPS",
                    price = 399.99,
                    category = "Electrónica",
                    stock = 15,
                    imageUrl = "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Auriculares Bluetooth",
                    description = "Auriculares inalámbricos con cancelación de ruido",
                    price = 149.99,
                    category = "Audio",
                    stock = 25,
                    imageUrl = "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Webcam HD",
                    description = "Cámara web Full HD 1080p",
                    price = 59.99,
                    category = "Accesorios",
                    stock = 40,
                    imageUrl = "https://images.unsplash.com/photo-1587825140708-dfaf72ae4b04?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "SSD 1TB",
                    description = "Disco sólido NVMe de alta velocidad",
                    price = 119.99,
                    category = "Almacenamiento",
                    stock = 20,
                    imageUrl = "https://images.unsplash.com/photo-1531492746076-161ca9bcad58?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Router WiFi 6",
                    description = "Router de última generación con WiFi 6",
                    price = 179.99,
                    category = "Redes",
                    stock = 12,
                    imageUrl = "https://images.unsplash.com/photo-1606904825846-647eb07f5be2?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Smartphone 5G",
                    description = "Smartphone de última generación con 5G y cámara de 108MP",
                    price = 899.99,
                    category = "Electrónica",
                    stock = 18,
                    imageUrl = "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Tablet 10 pulgadas",
                    description = "Tablet Android con pantalla OLED y stylus incluido",
                    price = 449.99,
                    category = "Electrónica",
                    stock = 22,
                    imageUrl = "https://images.unsplash.com/photo-1561154464-82e9adf32764?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Smartwatch Pro",
                    description = "Reloj inteligente con monitor de salud y GPS",
                    price = 299.99,
                    category = "Accesorios",
                    stock = 35,
                    imageUrl = "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Impresora Multifuncional",
                    description = "Impresora WiFi con escáner y copiadora integrada",
                    price = 199.99,
                    category = "Oficina",
                    stock = 14,
                    imageUrl = "https://images.unsplash.com/photo-1612815154858-60aa4c59eaa6?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Altavoz Bluetooth",
                    description = "Altavoz portátil resistente al agua con 20h de batería",
                    price = 79.99,
                    category = "Audio",
                    stock = 45,
                    imageUrl = "https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Power Bank 20000mAh",
                    description = "Batería portátil de alta capacidad con carga rápida",
                    price = 49.99,
                    category = "Accesorios",
                    stock = 55,
                    imageUrl = "https://images.unsplash.com/photo-1609091839311-d5365f9ff1c5?w=800",
                    isAvailable = true
                ),
                Product(
                    name = "Micrófono USB",
                    description = "Micrófono de condensador profesional para streaming",
                    price = 129.99,
                    category = "Audio",
                    stock = 28,
                    imageUrl = "https://images.unsplash.com/photo-1590602847861-f357a9332bbc?w=800",
                    isAvailable = true
                )
            )

            productDao.insertProducts(sampleProducts)
        }
    }
}
