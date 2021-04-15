package pk.codebae.geofencing;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {

    public static final String TAG = "GeoFenceTag";
    PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    public GeofencingRequest geofencingRequest (Geofence geofence) {
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    public Geofence getGeofence(String id, LatLng latLng, float radius, int transitionType) {
        return new Geofence.Builder().
                setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(id)
                .setTransitionTypes(transitionType)
                .setLoiteringDelay(5000)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    public PendingIntent getPendingIntent() {
        if (pendingIntent != null) {
            return  pendingIntent;
        }

        Intent intent =  new Intent(this, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 1122, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        return pendingIntent;
    }


    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes
                        .GEOFENCE_NOT_AVAILABLE:
                    return "Geofence is not Available";

                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_GEOFENCES:
                    return "Too Many Geofences";

                case GeofenceStatusCodes
                        .GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "Too Many Pending Intents";
                case GeofenceStatusCodes.GEOFENCE_INSUFFICIENT_LOCATION_PERMISSION:
                    return "Location Permission Insufficient";
            }
        }

        return e.getLocalizedMessage();
    }
}

