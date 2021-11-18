package es.uji.geonews.model.dao;


import java.time.LocalDateTime;
import java.util.Date;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;

public class LocationDao {
    private int id;
    private String alias;
    private String placeName;
    private GeographCoords geographCoords;
    private String registrationDate;
    private boolean isActive;

    public LocationDao(Location location){
        id = location.getId();
        alias = location.getAlias();
        placeName = location.getPlaceName();
        geographCoords = location.getGeographCoords();
        registrationDate = location.getRegistrationDate().toString();
        isActive = location.isActive();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
