package es.uji.geonews.model;

import androidx.annotation.NonNull;

public class GeographCoords {
    private double latitude;
    private double longitude;

    public GeographCoords(double latitude, double longitude) {

        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @NonNull
    @Override
    public String toString() {
        return latitude + "," + longitude;
    }
}
