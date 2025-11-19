package com.example.tiendaonline.ui.viewmodel

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tiendaonline.util.LocationHelper
import com.google.android.gms.location.LocationCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel para manejar el estado de la geolocalización
 */
class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationHelper = LocationHelper(application)

    // Estado de la ubicación
    private val _locationState = MutableStateFlow<LocationState>(LocationState.Idle)
    val locationState: StateFlow<LocationState> = _locationState.asStateFlow()

    // Última ubicación conocida
    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    // Callback para actualizaciones continuas
    private var locationCallback: LocationCallback? = null

    /**
     * Verifica si los permisos de ubicación están otorgados
     */
    fun hasLocationPermission(): Boolean {
        return locationHelper.hasLocationPermission()
    }

    /**
     * Obtiene la última ubicación conocida
     */
    fun getLastKnownLocation() {
        viewModelScope.launch {
            if (!hasLocationPermission()) {
                _locationState.value = LocationState.Error("Permisos de ubicación no otorgados")
                return@launch
            }

            _locationState.value = LocationState.Loading

            locationHelper.getLastKnownLocation(
                onSuccess = { location ->
                    _currentLocation.value = location
                    _locationState.value = LocationState.Success(location)
                },
                onFailure = { exception ->
                    _locationState.value = LocationState.Error(
                        exception.message ?: "Error al obtener ubicación"
                    )
                }
            )
        }
    }

    /**
     * Obtiene la ubicación actual con alta precisión
     */
    fun getCurrentLocation() {
        viewModelScope.launch {
            if (!hasLocationPermission()) {
                _locationState.value = LocationState.Error("Permisos de ubicación no otorgados")
                return@launch
            }

            _locationState.value = LocationState.Loading

            locationHelper.getCurrentLocation(
                onSuccess = { location ->
                    _currentLocation.value = location
                    _locationState.value = LocationState.Success(location)
                },
                onFailure = { exception ->
                    _locationState.value = LocationState.Error(
                        exception.message ?: "Error al obtener ubicación"
                    )
                }
            )
        }
    }

    /**
     * Inicia actualizaciones continuas de ubicación
     * @param intervalMillis Intervalo en milisegundos entre actualizaciones
     */
    fun startLocationUpdates(intervalMillis: Long = 10000L) {
        viewModelScope.launch {
            if (!hasLocationPermission()) {
                _locationState.value = LocationState.Error("Permisos de ubicación no otorgados")
                return@launch
            }

            _locationState.value = LocationState.Tracking

            locationCallback = locationHelper.startLocationUpdates(
                intervalMillis = intervalMillis,
                onLocationUpdate = { location ->
                    _currentLocation.value = location
                    _locationState.value = LocationState.Success(location)
                },
                onFailure = { exception ->
                    _locationState.value = LocationState.Error(
                        exception.message ?: "Error en actualizaciones de ubicación"
                    )
                }
            )
        }
    }

    /**
     * Detiene las actualizaciones continuas de ubicación
     */
    fun stopLocationUpdates() {
        locationCallback?.let {
            locationHelper.stopLocationUpdates(it)
            locationCallback = null
            _locationState.value = LocationState.Idle
        }
    }

    /**
     * Limpia el error actual
     */
    fun clearError() {
        if (_locationState.value is LocationState.Error) {
            _locationState.value = LocationState.Idle
        }
    }

    /**
     * Formatea las coordenadas de ubicación
     */
    fun formatLocation(location: Location): String {
        return "Lat: ${String.format("%.6f", location.latitude)}, " +
                "Lng: ${String.format("%.6f", location.longitude)}"
    }

    /**
     * Obtiene información adicional de la ubicación
     */
    fun getLocationDetails(location: Location): LocationDetails {
        return LocationDetails(
            latitude = location.latitude,
            longitude = location.longitude,
            accuracy = location.accuracy,
            altitude = if (location.hasAltitude()) location.altitude else null,
            speed = if (location.hasSpeed()) location.speed else null,
            bearing = if (location.hasBearing()) location.bearing else null,
            provider = location.provider,
            time = location.time
        )
    }

    override fun onCleared() {
        super.onCleared()
        stopLocationUpdates()
    }

    /**
     * Estados posibles de la geolocalización
     */
    sealed class LocationState {
        object Idle : LocationState()
        object Loading : LocationState()
        object Tracking : LocationState()
        data class Success(val location: Location) : LocationState()
        data class Error(val message: String) : LocationState()
    }

    /**
     * Clase de datos con detalles de ubicación
     */
    data class LocationDetails(
        val latitude: Double,
        val longitude: Double,
        val accuracy: Float,
        val altitude: Double?,
        val speed: Float?,
        val bearing: Float?,
        val provider: String?,
        val time: Long
    )
}
