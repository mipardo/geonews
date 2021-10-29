package es.uji.geonews.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class LocationManager {
    private final List<Location> activeLocations;
    private final List<Location> nonActiveLocations;
    private final List<Location> favoriteLocations;
    private final ServiceManager serviceManager;

    public LocationManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
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
        CoordsSearchService coordsSearchService = new CoordsSearchService();
        if(coordsSearchService.isAvailable()){
            GeographCoords coords = coordsSearchService.getCoordsFrom(placeName);
            Location location = new Location(1,placeName, coords, LocalDate.now());
            nonActiveLocations.add(location);
            return location;
        }
        return null;
    }


    public ServiceManager getCoordsSearchService() {
        return serviceManager;
    }
}
