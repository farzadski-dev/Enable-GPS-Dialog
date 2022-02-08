package dev.farzadski.setenablelocation;

import android.content.Context;
import android.content.IntentSender;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.Task;

public class EnableGPS {
    private Context context;

    private LocationRequest locationRequest;
    private LocationSettingsRequest.Builder builder;
    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 148;
    private SettingsClient client;
    private Task<LocationSettingsResponse> task;

    public EnableGPS(Context context) {
        this.context = context;
    }

    public void enable() {
        setLocationRequest();
        buildLocationSettingsRequest();
        setSettingsClient();
        setTaskForCheckLocationSettings();
        onTaskForCheckLocationSettingsListener();
    }

    private void onTaskForCheckLocationSettingsListener() {
        onTaskSuccessListener();
        onTaskFailureListener();
    }

    private void onTaskFailureListener() {
        task.addOnFailureListener(((AppCompatActivity) context), e -> {
            if (e instanceof ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    startResolutionForResult((ResolvableApiException) e);
                } catch (IntentSender.SendIntentException sendEx) {
                    // Ignore the error.
                }
            }
        });
    }

    private void startResolutionForResult(ResolvableApiException e) throws IntentSender.SendIntentException {
        ResolvableApiException resolvable = e;
        resolvable.startResolutionForResult(((AppCompatActivity) context),
                PERMISSION_REQUEST_COARSE_LOCATION);
    }

    private void onTaskSuccessListener() {
        task.addOnSuccessListener(((AppCompatActivity) context), locationSettingsResponse -> {
            // All location settings are satisfied. The client can initialize
            // location requests here.
            // ...
        });
    }

    private void setTaskForCheckLocationSettings() {
        task = client.checkLocationSettings(builder.build());
    }

    private void setSettingsClient() {
        client = LocationServices.getSettingsClient(context);
    }

    private void buildLocationSettingsRequest() {
        builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
    }

    private void setLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


}
