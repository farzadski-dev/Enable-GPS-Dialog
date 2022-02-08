package dev.farzadski.setenablelocation;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class EnableGPS {
    private Context context;
    private AlertDialog.Builder dialogBuilder;

    private LocationManager locationManager;
    public static final int PERMISSION_REQUEST_COARSE_LOCATION = 148;

    public EnableGPS(Context context) {
        this.context = context;
        dialogBuilder = new AlertDialog.Builder(context);
    }

    public void enable() {
        if (isSDKGreaterThanOrEqualMarshmallow())
            if (isNotPermissionAccessCoarseLocationGrantedOnGreaterThanOrEqualMarshmallow())
                onGreaterThanOrEqualMarshmellowLocationRequested();
            else {
                setLocationManager();
                if (isNotPermissionAccessCoarseLocationGrantedOnLessThanMarshmallow())
                    onLessThanMarshmellowLocationRequested();
            }
    }

    private boolean isNotPermissionAccessCoarseLocationGrantedOnLessThanMarshmallow() {
        return !isGpsProviderEnabled() && !isNetworkProviderEnabled();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onGreaterThanOrEqualMarshmellowLocationRequested() {
        setTitleAndMessageOfOurDialog();
        onPositiveButtonActionForSDKGreaterThanOrEqualMarshmallow();
        showOurDialog();
    }

    private void onPositiveButtonActionForSDKLessThanMarshmallow() {
        dialogBuilder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        });
    }

    private void onLessThanMarshmellowLocationRequested() {
        setTitleAndMessageOfOurDialog();
        onPositiveButtonActionForSDKLessThanMarshmallow();
        showOurDialog();
    }


    private boolean isNetworkProviderEnabled() {
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }


    private boolean isGpsProviderEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void setLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onPositiveButtonActionForSDKGreaterThanOrEqualMarshmallow() {
        dialogBuilder
                .setPositiveButton(android.R.string.yes, (dialogInterface, i) ->
                        ((AppCompatActivity) context)
                                .requestPermissions(
                                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                        PERMISSION_REQUEST_COARSE_LOCATION));
    }

    private void showOurDialog() {
        dialogBuilder.setNegativeButton(android.R.string.no, null);
        dialogBuilder.show();
    }

    private void setTitleAndMessageOfOurDialog() {
        dialogBuilder.setTitle("Location Permission");
        dialogBuilder.setMessage("The app needs location permissions. Please grant this permission to continue using the features of the app.");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean isNotPermissionAccessCoarseLocationGrantedOnGreaterThanOrEqualMarshmallow() {
        return context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
    }

    private boolean isSDKGreaterThanOrEqualMarshmallow() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }


}
