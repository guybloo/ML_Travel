package com.example.ml_travel;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import androidx.core.app.ActivityCompat;


public class LocationTracker implements LocationListener {

    private LocationManager locationManager;
    private Context context;
    private LocationInfo info;
    private boolean isTracking;

    LocationTracker(Context cnt) {
        context = cnt;
        info = new LocationInfo();
        isTracking = false;
    }

    void startTracking() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Error", "Can't get location permissions");
            return;
        }

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        assert locationManager != null;


        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this, Looper.getMainLooper());
        isTracking = true;
    }

    void stopTracking() {
        locationManager.removeUpdates(this);
        info = new LocationInfo();
    }


    @Override
    public void onLocationChanged(Location location) {
        if (!((location.getLongitude() == info.getLongitude()) &&
                (location.getLatitude() == info.getLatitude()) &&
                (location.getAccuracy() == info.getAccuracy()))) {
            info.setParams(location.getLongitude(), location.getLatitude(), location.getAccuracy());
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {

    }

//    private void openDialog(String msg) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setMessage(msg)
//                .setPositiveButton("Got it", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
//                    }
//                })
//                .create().show();
//    }

    boolean isTracking() {
        return isTracking;
    }

    public LocationInfo getInfo() {
        return info;
    }
}
