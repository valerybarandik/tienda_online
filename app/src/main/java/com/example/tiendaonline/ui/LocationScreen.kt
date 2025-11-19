package com.example.tiendaonline.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tiendaonline.ui.viewmodel.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    onNavigateBack: () -> Unit,
    locationViewModel: LocationViewModel = viewModel()
) {
    val locationState by locationViewModel.locationState.collectAsState()
    val currentLocation by locationViewModel.currentLocation.collectAsState()

    // Estado de permisos de ubicación
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    // Estado para tracking continuo
    var isTracking by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Geolocalización") },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isTracking) {
                            locationViewModel.stopLocationUpdates()
                        }
                        onNavigateBack()
                    }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta de permisos
            PermissionsCard(
                permissionsState = locationPermissionsState,
                onRequestPermissions = {
                    locationPermissionsState.launchMultiplePermissionRequest()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de estado
            StateCard(locationState = locationState)

            Spacer(modifier = Modifier.height(16.dp))

            // Botones de acción
            if (locationPermissionsState.allPermissionsGranted) {
                ActionButtons(
                    isTracking = isTracking,
                    locationState = locationState,
                    onGetLastLocation = { locationViewModel.getLastKnownLocation() },
                    onGetCurrentLocation = { locationViewModel.getCurrentLocation() },
                    onStartTracking = {
                        isTracking = true
                        locationViewModel.startLocationUpdates()
                    },
                    onStopTracking = {
                        isTracking = false
                        locationViewModel.stopLocationUpdates()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Información de ubicación
                currentLocation?.let { location ->
                    LocationInfoCard(
                        location = location,
                        locationViewModel = locationViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsCard(
    permissionsState: MultiplePermissionsState,
    onRequestPermissions: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (permissionsState.allPermissionsGranted)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (permissionsState.allPermissionsGranted)
                        Icons.Default.CheckCircle
                    else
                        Icons.Default.Warning,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = if (permissionsState.allPermissionsGranted)
                        MaterialTheme.colorScheme.onPrimaryContainer
                    else
                        MaterialTheme.colorScheme.onErrorContainer
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (permissionsState.allPermissionsGranted)
                        "Permisos otorgados"
                    else
                        "Permisos necesarios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            if (!permissionsState.allPermissionsGranted) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Esta aplicación necesita acceso a tu ubicación para funcionar correctamente.",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onRequestPermissions,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.LocationOn, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Otorgar permisos")
                }
            }
        }
    }
}

@Composable
fun StateCard(locationState: LocationViewModel.LocationState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when (locationState) {
                is LocationViewModel.LocationState.Loading,
                is LocationViewModel.LocationState.Tracking -> MaterialTheme.colorScheme.secondaryContainer
                is LocationViewModel.LocationState.Success -> MaterialTheme.colorScheme.tertiaryContainer
                is LocationViewModel.LocationState.Error -> MaterialTheme.colorScheme.errorContainer
                else -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when (locationState) {
                is LocationViewModel.LocationState.Idle -> {
                    Icon(Icons.Default.Place, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Listo para obtener ubicación")
                }
                is LocationViewModel.LocationState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Obteniendo ubicación...")
                }
                is LocationViewModel.LocationState.Tracking -> {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Rastreando ubicación...")
                }
                is LocationViewModel.LocationState.Success -> {
                    Icon(Icons.Default.CheckCircle, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text("Ubicación obtenida exitosamente")
                }
                is LocationViewModel.LocationState.Error -> {
                    Icon(Icons.Default.Close, contentDescription = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(locationState.message)
                }
            }
        }
    }
}

@Composable
fun ActionButtons(
    isTracking: Boolean,
    locationState: LocationViewModel.LocationState,
    onGetLastLocation: () -> Unit,
    onGetCurrentLocation: () -> Unit,
    onStartTracking: () -> Unit,
    onStopTracking: () -> Unit
) {
    val isLoading = locationState is LocationViewModel.LocationState.Loading ||
            locationState is LocationViewModel.LocationState.Tracking

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onGetLastLocation,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Última ubicación conocida")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = onGetCurrentLocation,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Icon(Icons.Default.Place, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Obtener ubicación actual")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (!isTracking) {
            Button(
                onClick = onStartTracking,
                modifier = Modifier.fillMaxWidth(),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Iniciar rastreo continuo")
            }
        } else {
            Button(
                onClick = onStopTracking,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Close, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Detener rastreo")
            }
        }
    }
}

@Composable
fun LocationInfoCard(
    location: android.location.Location,
    locationViewModel: LocationViewModel
) {
    val locationDetails = locationViewModel.getLocationDetails(location)
    val dateFormat = remember { SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Información de Ubicación",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LocationInfoRow(
                icon = Icons.Default.Place,
                label = "Coordenadas",
                value = locationViewModel.formatLocation(location)
            )

            Spacer(modifier = Modifier.height(8.dp))

            LocationInfoRow(
                icon = Icons.Default.CheckCircle,
                label = "Precisión",
                value = "${String.format("%.2f", locationDetails.accuracy)} metros"
            )

            locationDetails.altitude?.let { altitude ->
                Spacer(modifier = Modifier.height(8.dp))
                LocationInfoRow(
                    icon = Icons.Default.Add,
                    label = "Altitud",
                    value = "${String.format("%.2f", altitude)} metros"
                )
            }

            locationDetails.speed?.let { speed ->
                Spacer(modifier = Modifier.height(8.dp))
                LocationInfoRow(
                    icon = Icons.Default.Star,
                    label = "Velocidad",
                    value = "${String.format("%.2f", speed * 3.6)} km/h"
                )
            }

            locationDetails.bearing?.let { bearing ->
                Spacer(modifier = Modifier.height(8.dp))
                LocationInfoRow(
                    icon = Icons.Default.Star,
                    label = "Dirección",
                    value = "${String.format("%.1f", bearing)}°"
                )
            }

            locationDetails.provider?.let { provider ->
                Spacer(modifier = Modifier.height(8.dp))
                LocationInfoRow(
                    icon = Icons.Default.Info,
                    label = "Proveedor",
                    value = provider
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            LocationInfoRow(
                icon = Icons.Default.Info,
                label = "Hora",
                value = dateFormat.format(Date(locationDetails.time))
            )
        }
    }
}

@Composable
fun LocationInfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
