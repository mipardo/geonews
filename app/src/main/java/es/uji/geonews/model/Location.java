package es.uji.geonews.model;

import java.time.LocalDate;

public class Location {
    private int id;
    private String alias;
    private String placeName;
    private GeographCoords geographCoords;
    private LocalDate registrationDate;
    private boolean isActive;

    public Location(int id, String placeName, GeographCoords geographCoords, LocalDate registrationDate ) {
        this.id = id;
        this.placeName = placeName;
        this.geographCoords = geographCoords;
        this.registrationDate = registrationDate;
        this.alias = "";
        this.isActive = false;
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

    public boolean isActive() {
        return isActive;
    }

    public boolean activate() {
        if (!isActive) {
            isActive = true;
            return true;
        }
        return false;
    }

    public boolean deactivate() {
        if (isActive) {
            isActive = false;
            return true;
        }
        return false;
    }

    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }
}
