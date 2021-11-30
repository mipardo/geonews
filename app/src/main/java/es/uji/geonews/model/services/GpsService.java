package es.uji.geonews.model.services;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;

public class GpsService extends Service {
    private android.location.LocationManager locationManager;
    private Context context;
    private GeographCoords currentPosition;

    public GpsService(Context context) {

        super(ServiceName.GPS, "Servicio GPS para la obtención de la ubicacion actual");
        locationManager = (android.location.LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        this.context = context;
    }

    public void updateGpsCoords() {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            currentPosition = null;
        }

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // GPS location can be null if GPS is switched off
                        if (location != null) {
                            currentPosition = new GeographCoords(location.getLatitude(), location.getLongitude());
                        } else {
                            currentPosition = null;
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        currentPosition = null;
                    }
                });
    }

    public GeographCoords currentCoords() throws GPSNotAvailableException {
        if (isAvailable()) {
           if (currentPosition != null) return currentPosition;
        }
        throw new GPSNotAvailableException();
    }

    @Override
    public boolean isAvailable() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return false;
        }
        return true;
    }

}
