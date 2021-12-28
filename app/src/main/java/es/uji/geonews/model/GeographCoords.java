package es.uji.geonews.model;

public class GeographCoords {
    private double latitude;
    private double longitude;
    private String country;

    public GeographCoords() {}

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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return latitude + ", " + longitude;
    }

    public void normalize() {
        latitude = latitude + 0.0001;
        longitude = longitude + 0.0001;
    }
}
