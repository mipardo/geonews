package es.uji.geonews.model;

public class GeographCoords {
    private double latitude;
    private double longitude;
    private String print;

    public GeographCoords() {
        super();
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

    public String getPrint() {
        return print;
    }

    public void setPrint(String print) {
        this.print = print;
    }
}
