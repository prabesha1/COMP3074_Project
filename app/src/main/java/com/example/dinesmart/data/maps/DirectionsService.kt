package com.example.dinesmart.data.maps

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri
import com.google.android.gms.maps.model.LatLng

class DirectionsService(private val context: Context) {

    // Open Google Maps with directions
    fun openDirections(
        destination: LatLng,
        destinationName: String,
        currentLocation: LatLng? = null
    ) {
        val uri = if (currentLocation != null) {
            // With origin
            "https://www.google.com/maps/dir/?api=1&origin=${currentLocation.latitude},${currentLocation.longitude}&destination=${destination.latitude},${destination.longitude}&travelmode=driving".toUri()
        } else {
            // Without origin (uses current location automatically)
            "https://www.google.com/maps/dir/?api=1&destination=${destination.latitude},${destination.longitude}&travelmode=driving".toUri()
        }

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback to web browser
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(webIntent)
        }
    }

    // Open with address instead of coordinates
    fun openDirectionsByAddress(
        destinationAddress: String,
        currentLocation: LatLng? = null
    ) {
        val encodedAddress = Uri.encode(destinationAddress)
        val uri = if (currentLocation != null) {
            "https://www.google.com/maps/dir/?api=1&origin=${currentLocation.latitude},${currentLocation.longitude}&destination=$encodedAddress&travelmode=driving".toUri()
        } else {
            "https://www.google.com/maps/dir/?api=1&destination=$encodedAddress&travelmode=driving".toUri()
        }

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(webIntent)
        }
    }

    // Get walking, biking, or transit directions
    fun openDirectionsWithMode(
        destination: LatLng,
        travelMode: TravelMode = TravelMode.DRIVING,
        currentLocation: LatLng? = null
    ) {
        val mode = when (travelMode) {
            TravelMode.DRIVING -> "driving"
            TravelMode.WALKING -> "walking"
            TravelMode.BICYCLING -> "bicycling"
            TravelMode.TRANSIT -> "transit"
        }

        val uri = if (currentLocation != null) {
            "https://www.google.com/maps/dir/?api=1&origin=${currentLocation.latitude},${currentLocation.longitude}&destination=${destination.latitude},${destination.longitude}&travelmode=$mode".toUri()
        } else {
            "https://www.google.com/maps/dir/?api=1&destination=${destination.latitude},${destination.longitude}&travelmode=$mode".toUri()
        }

        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        try {
            context.startActivity(intent)
        } catch (e: Exception) {
            val webIntent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(webIntent)
        }
    }
}

enum class TravelMode {
    DRIVING,
    WALKING,
    BICYCLING,
    TRANSIT
}

