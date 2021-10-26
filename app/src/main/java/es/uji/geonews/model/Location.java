package es.uji.geonews.model;

import java.util.Date;

public class Location {
    private int id;
    private String placeName;
    private GeographCoords geographCoords;
    private Date registrationDate;

    public Location(int id, String placeName, GeographCoords geographCoords, Date registrationDate) {
        this.id = id;
        this.placeName = placeName;
        this.geographCoords = geographCoords;
        this.registrationDate = registrationDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public GeographCoords getGeographCoords() {
        return geographCoords;
    }

    public void setGeographCoords(GeographCoords geographCoords) {
        this.geographCoords = geographCoords;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }
}
