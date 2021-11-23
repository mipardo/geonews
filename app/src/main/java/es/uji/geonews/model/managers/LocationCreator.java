package es.uji.geonews.model.managers;

import java.time.LocalDate;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class LocationCreator {
    private GeocodeService geocodeService;
    private int idLocationCounter;

    public LocationCreator(GeocodeService geocodeService){
        this.geocodeService = geocodeService;
        this.idLocationCounter = 1;
    }

    public int getIdLocationCounter() {
        return idLocationCounter;
    }

    public void setIdLocationCounter(int idLocationCounter) {
        this.idLocationCounter = idLocationCounter;
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
        if(geocodeService.isAvailable()){
            GeographCoords coords = geocodeService.getCoords(placeName);
            return new Location(idLocationCounter++,placeName, coords, LocalDate.now());
        }
        throw  new ServiceNotAvailableException();
    }

    private Location createLocationByCoords(GeographCoords coords)
            throws NotValidCoordinatesException, ServiceNotAvailableException {
        if(!areValidCoords(coords)) throw new NotValidCoordinatesException();

        if (!hasEnoughPrecision(coords)){
            coords.normalize();
        }

        if (geocodeService.isAvailable()) {
            String placeName = geocodeService.getPlaceName(coords);
            return new Location(idLocationCounter++, placeName, coords, LocalDate.now());
        }
        throw  new ServiceNotAvailableException();
    }

    private boolean isGeographCoords(String coords){
        boolean isCoord = true;

        String[] coordsSplitted = coords.trim().split(",");
        if (coordsSplitted.length == 2){
            try{
                Double.parseDouble(coordsSplitted[0].trim());
                Double.parseDouble(coordsSplitted[1].trim());
            } catch (Exception e){
                isCoord = false;
            }
        } else {
            isCoord = false;
        }
        return isCoord;
    }

    private GeographCoords convertStringToCoords(String coords){
        String[] coordsSplitted = coords.trim().split(",");
        double latitude = Double.parseDouble(coordsSplitted[0].trim());
        double longitude = Double.parseDouble(coordsSplitted[1].trim());
        return new GeographCoords(latitude, longitude);
    }

    private boolean areValidCoords(GeographCoords coords){
        return (coords.getLatitude() < 90 && coords.getLatitude() > -90 &&
                coords.getLongitude() < 180 && coords.getLongitude() > -180);
    }

    private boolean hasEnoughPrecision(GeographCoords coords) {
        String latString = String.valueOf(coords.getLatitude()).split("\\.")[1];
        String lonString = String.valueOf(coords.getLatitude()).split("\\.")[1];
        return latString.length() >= 4 && lonString.length() >= 4;
    }
}
