package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GPSManager;
import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU03 {
    private static LocationManager locationManager;

    @Before
    public void init(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableKnownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        GeographCoords coords = GPSManager.getMyCoords();
        Location newLocation = locationManager.addLocation(coords.toString());
        // Then
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals("Castelló de la Plana", newLocation.getPlaceName());
    }

    @Test
    public void registerLocationByCurrentPosition_GPSAvailableUnknownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        locationManager.addLocation("Castelló de la Plana");
        GeographCoords coords = GPSManager.getMyCoords();
        coords.setLatitude(33.65001); // TODO: [Preguntar]
        coords.setLongitude(-41.19001);
        Location newLocation = locationManager.addLocation(coords.toString());
        // Then
        assertEquals(2, locationManager.getNonActiveLocations().size());
        assertNull(newLocation.getPlaceName());
    }

    @Test (expected = GPSNotAvailableException.class)
    public void registerLocationByCurrentPosition_GPSNotAvailable_GPSNotAvailableException()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            GPSNotAvailableException, UnrecognizedPlaceNameException {
        locationManager.addLocation("Castelló de la Plana");
        locationManager.addLocation("Valencia");
        locationManager.addLocation(GPSManager.getMyCoords().toString());
    }

}
