package es.uji.geonews.model;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class LocationManager {
    private final List<Location> activeLocations;
    private final List<Location> nonActiveLocations;
    private final List<Location> favoriteLocations;
    private final Service coordsSearchService;

    public LocationManager(Service coordsSearchService) {
        this.coordsSearchService = coordsSearchService;
        activeLocations = new ArrayList<>();
        nonActiveLocations = new ArrayList<>();
        favoriteLocations = new ArrayList<>();
    }

    public List<Location> getActiveLocations() {
        return activeLocations;
    }

    public List<Location> getNonActiveLocations() {
        return nonActiveLocations;
    }

    public List<Location> getFavoriteLocations() {
        return favoriteLocations;
    }

    public Location addLocationByPlaceName(String placeName){
        return null;
    }


    public Service getCoordsSearchService() {
        return coordsSearchService;
    }
}
