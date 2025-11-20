# Tienda Online - Aplicación Android

Aplicación completa de tienda online desarrollada de forma nativa en Android con Jetpack Compose, Room Database y arquitectura MVVM.

## Características Principales

### Gestión de Usuarios
- **Login y Registro:** Sistema completo de autenticación con validación de campos
- **Persistencia de sesión:** Usuarios almacenados en base de datos local
- **Gestión de perfiles:** Cada usuario tiene su propio carrito de compras

### Catálogo de Productos
- **15 productos variados:** Electrónica, accesorios, audio y más
- **Imágenes reales:** Integración con Coil para carga eficiente de imágenes desde URLs
- **Información detallada:** Nombre, descripción, precio, stock y categoría
- **Vista de lista:** Diseño intuitivo con tarjetas de productos

### Carrito de Compras
- **Gestión completa:** Agregar, incrementar, decrementar y eliminar productos
- **Imágenes de productos:** Vista previa visual de cada item en el carrito
- **Cálculo automático:** Total y subtotales actualizados en tiempo real
- **Badge de notificación:** Indicador del número de items en el carrito
- **Persistencia:** El carrito se mantiene entre sesiones

### Geolocalización
- **Ubicación en tiempo real:** Integración con Google Play Services
- **Permisos gestionados:** Uso de Accompanist Permissions para solicitud de permisos
- **Pantalla dedicada:** Vista para mostrar coordenadas GPS del usuario

## Tecnologías Utilizadas

### UI y Diseño
- **Jetpack Compose:** 100% declarativo
- **Material Design 3:** Tema personalizado con colores modernos
- **Coil:** Carga asíncrona de imágenes desde URLs
- **Navigation Compose:** Navegación entre pantallas

### Arquitectura
- **MVVM:** Separación de lógica de negocio y UI
- **Room Database:** Persistencia local de datos
- **StateFlow:** Gestión reactiva del estado
- **Coroutines:** Operaciones asíncronas

### Componentes Principales
- **ViewModels:** ProductViewModel, CartViewModel, AuthViewModel, LocationViewModel
- **Repositorios:** Capa de abstracción para acceso a datos
- **DAOs:** ProductDao, UserDao, CartItemDao
- **Entidades:** Product, User, CartItem

## Base de Datos

### Entidades
- **Products:** 15 productos con imágenes reales
- **Users:** Sistema de autenticación
- **CartItems:** Relación entre usuarios y productos

### Características
- Migración destructiva para actualizaciones
- Población automática con datos de ejemplo
- Relaciones entre tablas

## Catálogo de Productos

1. Laptop Gaming - $1,299.99
2. Mouse Inalámbrico - $29.99
3. Teclado Mecánico - $89.99
4. Monitor 27 pulgadas - $399.99
5. Auriculares Bluetooth - $149.99
6. Webcam HD - $59.99
7. SSD 1TB - $119.99
8. Router WiFi 6 - $179.99
9. Smartphone 5G - $899.99
10. Tablet 10 pulgadas - $449.99
11. Smartwatch Pro - $299.99
12. Impresora Multifuncional - $199.99
13. Altavoz Bluetooth - $79.99
14. Power Bank 20000mAh - $49.99
15. Micrófono USB - $129.99

## Requisitos

- Android Studio Hedgehog o superior
- SDK mínimo: API 24 (Android 7.0)
- SDK objetivo: API 36
- Kotlin 1.9+
- Gradle 8.0+

## Instalación

1. Clona el repositorio:
   ```bash
   git clone <url-del-repositorio>
   ```

2. Abre el proyecto en Android Studio

3. Sincroniza las dependencias de Gradle

4. Ejecuta la aplicación en un emulador o dispositivo físico

## Uso desde Terminal

### Compilar el proyecto
```bash
./gradlew build
```

### Instalar en dispositivo/emulador
```bash
./gradlew installDebug
```

### Desinstalar la aplicación
```bash
./gradlew uninstallAll
```

### Limpiar y reinstalar
```bash
./gradlew clean && ./gradlew installDebug
```

## Estructura del Proyecto

```
app/src/main/java/com/example/tiendaonline/
├── data/
│   ├── entity/          # Entidades de Room
│   ├── dao/             # Data Access Objects
│   ├── repository/      # Repositorios
│   └── database/        # Configuración de la BD
├── ui/
│   ├── viewmodel/       # ViewModels
│   ├── theme/           # Tema y colores
│   └── *.kt             # Pantallas Compose
└── util/                # Utilidades (LocationHelper)
```

## Funcionalidades Pendientes

- [ ] Proceso de checkout completo
- [ ] Historial de pedidos
- [ ] Búsqueda y filtrado de productos
- [ ] Categorías de productos
- [ ] Favoritos
- [ ] Notificaciones push
- [ ] Integración con pasarela de pago

## Licencia

Este proyecto es un proyecto educativo desarrollado con fines de aprendizaje.
