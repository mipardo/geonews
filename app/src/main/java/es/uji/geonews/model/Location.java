package es.uji.geonews.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.uji.geonews.model.services.Service;

public class Location {
    private int id;
    private String alias;
    private String placeName;
    private GeographCoords geographCoords;
    private LocalDate registrationDate;
    private boolean isActive;
    private List<String> services;

    public Location(int id, String placeName, GeographCoords geographCoords, LocalDate registrationDate ) {
        this.id = id;
        this.placeName = placeName;
        this.geographCoords = geographCoords;
        this.registrationDate = registrationDate;
        this.alias = "";
        this.isActive = false;
        this.services = new ArrayList<>();
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

    public void addService(String serviceName) {
        services.add(serviceName);
    }

    public boolean hasService(String serviceName) {
        return services.contains(serviceName);
    }
}
