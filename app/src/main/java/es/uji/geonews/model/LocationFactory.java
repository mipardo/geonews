package es.uji.geonews.model;

import java.time.LocalDate;

import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;

public class LocationFactory {
    private CoordsSearchService coordsSearchService;
    private int idLocationCounter;

    public LocationFactory(CoordsSearchService coordsSearchService){
        this.coordsSearchService = coordsSearchService;
        this.idLocationCounter = 1; // TODO: Recojer de la DB
    }

    public Location createLocation(String location) throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException {
        if(isGeographCoords(location)){
            GeographCoords coords = convertStringToCoords(location);
            return createLocationByCoords(coords);
        }
        return createLocationByPlaceName(location);
    }

    private Location createLocationByPlaceName(String placeName)
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        if(coordsSearchService.isAvailable()){
            GeographCoords coords = coordsSearchService.getCoordsFrom(placeName);
            return new Location(idLocationCounter++,placeName, coords, LocalDate.now());
        }
        return null;
    }

    private Location createLocationByCoords(GeographCoords coords)
            throws NotValidCoordinatesException, ServiceNotAvailableException {
        if(!areValidCoords(coords)) throw new NotValidCoordinatesException();

        if (!hasEnoughPrecision(coords)) return null;

        if (coordsSearchService.isAvailable()) {
            String placeName = coordsSearchService.getPlaceNameFromCoords(coords);
            return new Location(idLocationCounter++, placeName, coords, LocalDate.now());
        }
        return null;
    }

    private boolean isGeographCoords(String location){
        return location.contains(","); // TODO: Be more specific
    }

    private GeographCoords convertStringToCoords(String coords){
        String[] coordsSplitted = coords.trim().split(", ");
        double latitude = Double.parseDouble(coordsSplitted[0]);
        double longitude = Double.parseDouble(coordsSplitted[1]);
        return new GeographCoords(latitude, longitude);
    }

    private boolean areValidCoords(GeographCoords coords){
        return (coords.getLatitude() < 90 && coords.getLatitude() > -90 &&
                coords.getLongitude()<180 && coords.getLongitude()>-180);
    }

    private boolean hasEnoughPrecision(GeographCoords coords) {
        String latString = String.valueOf(coords.getLatitude()).split("\\.")[1];
        String lonString = String.valueOf(coords.getLatitude()).split("\\.")[1];
        return latString.length() >= 4 && lonString.length() >= 4;
    }
}
