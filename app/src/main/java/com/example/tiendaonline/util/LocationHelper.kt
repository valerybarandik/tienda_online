package com.example.tiendaonline.util

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource

/**
 * Helper class para manejar la geolocalización del dispositivo.
 * Proporciona métodos para obtener la ubicación actual del usuario
 * utilizando Google Play Services Location API.
 */
class LocationHelper(private val context: Context) {

    private val fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    /**
     * Verifica si los permisos de ubicación están otorgados
     */
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Obtiene la última ubicación conocida del dispositivo
     * @param onSuccess Callback que se ejecuta cuando se obtiene la ubicación
     * @param onFailure Callback que se ejecuta cuando hay un error
     */
    fun getLastKnownLocation(
        onSuccess: (Location) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onFailure(SecurityException("Permisos de ubicación no otorgados"))
            return
        }

        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onSuccess(location)
                    } else {
                        onFailure(Exception("No se pudo obtener la última ubicación conocida"))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } catch (e: SecurityException) {
            onFailure(e)
        }
    }

    /**
     * Obtiene la ubicación actual del dispositivo con alta precisión
     * @param onSuccess Callback que se ejecuta cuando se obtiene la ubicación
     * @param onFailure Callback que se ejecuta cuando hay un error
     */
    fun getCurrentLocation(
        onSuccess: (Location) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        if (!hasLocationPermission()) {
            onFailure(SecurityException("Permisos de ubicación no otorgados"))
            return
        }

        try {
            val cancellationTokenSource = CancellationTokenSource()

            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                cancellationTokenSource.token
            )
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        onSuccess(location)
                    } else {
                        onFailure(Exception("No se pudo obtener la ubicación actual"))
                    }
                }
                .addOnFailureListener { exception ->
                    onFailure(exception)
                }
        } catch (e: SecurityException) {
            onFailure(e)
        }
    }

    /**
     * Inicia actualizaciones continuas de ubicación
     * @param intervalMillis Intervalo en milisegundos entre actualizaciones
     * @param onLocationUpdate Callback que se ejecuta con cada actualización de ubicación
     * @param onFailure Callback que se ejecuta cuando hay un error
     * @return LocationCallback que puede ser usado para detener las actualizaciones
     */
    fun startLocationUpdates(
        intervalMillis: Long = 10000L,
        onLocationUpdate: (Location) -> Unit,
        onFailure: (Exception) -> Unit
    ): LocationCallback? {
        if (!hasLocationPermission()) {
            onFailure(SecurityException("Permisos de ubicación no otorgados"))
            return null
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            intervalMillis
        )
            .setMinUpdateIntervalMillis(intervalMillis / 2)
            .setWaitForAccurateLocation(false)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    onLocationUpdate(location)
                }
            }

            override fun onLocationAvailability(availability: LocationAvailability) {
                if (!availability.isLocationAvailable) {
                    onFailure(Exception("Servicios de ubicación no disponibles"))
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
            return locationCallback
        } catch (e: SecurityException) {
            onFailure(e)
            return null
        }
    }

    /**
     * Detiene las actualizaciones de ubicación
     * @param locationCallback El LocationCallback retornado por startLocationUpdates
     */
    fun stopLocationUpdates(locationCallback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    companion object {
        /**
         * Array de permisos de ubicación requeridos
         */
        val LOCATION_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

        /**
         * Request code para solicitud de permisos de ubicación
         */
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
    }
}
