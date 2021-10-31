package es.uji.geonews.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class LocationManager {
    private final List<Location> activeLocations;
    private final List<Location> nonActiveLocations;
    private final List<Location> favoriteLocations;
    private final ServiceManager serviceManager;
    private final CoordsSearchService coordsSearchService;

    public LocationManager(ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        activeLocations = new ArrayList<>();
        nonActiveLocations = new ArrayList<>();
        favoriteLocations = new ArrayList<>();
        coordsSearchService = (CoordsSearchService) serviceManager.getService("Geocode");

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

    public Location addLocationByPlaceName(String placeName)
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        if(coordsSearchService.isAvailable()){
            GeographCoords coords = coordsSearchService.getCoordsFrom(placeName);
            Location location = new Location(1,placeName, coords, LocalDate.now());
            nonActiveLocations.add(location);
            return location;
        }
        return null;
    }

    public Location addLocationByCoords(GeographCoords coords)
            throws NotValidCoordinatesException, ServiceNotAvailableException{
        if(!areValidCoords(coords)){
            throw new NotValidCoordinatesException();
        }
        if (coordsSearchService.isAvailable()) {
            String placeName = coordsSearchService.getPlaceNameFromCoords(coords);
            Location location = new Location(1, placeName, coords, LocalDate.now());
            nonActiveLocations.add(location);
            return location;
        }

        return null;

    }

    private boolean areValidCoords(GeographCoords coords){

        return (coords.getLatitude()<90 && coords.getLatitude()>-90 && coords.getLongitude()<180 && coords.getLongitude()>-180);

    }
    public ServiceManager getCoordsSearchService() {
        return serviceManager;
    }


}
