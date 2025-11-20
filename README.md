# Tienda Online - Aplicación Android

Aplicación de tienda online desarrollada con Jetpack Compose, Room Database y arquitectura MVVM.

## Características

- **Autenticación:** Login y registro de usuarios con persistencia local
- **Catálogo de productos:** 15 productos con imágenes reales (Coil)
- **Carrito de compras:** Gestión completa con imágenes, cálculos automáticos y persistencia
- **Geolocalización:** Integración con Google Play Services para ubicación en tiempo real

## Tecnologías

- Jetpack Compose + Material Design 3
- Room Database
- MVVM Architecture
- Coil (carga de imágenes)
- Navigation Compose
- Coroutines + StateFlow

## Instalación

1. Clona el repositorio
2. Abre el proyecto en Android Studio
3. Ejecuta en un emulador o dispositivo físico

## Comandos útiles

```bash
# Instalar
./gradlew installDebug

# Desinstalar
./gradlew uninstallAll

# Limpiar y reinstalar
./gradlew clean && ./gradlew installDebug
```

## Requisitos

- SDK mínimo: API 24 (Android 7.0)
- SDK objetivo: API 36
