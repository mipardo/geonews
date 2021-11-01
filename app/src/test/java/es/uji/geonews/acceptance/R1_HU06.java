package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU06 {
    private static LocationManager locationManager;

    @BeforeClass
    public static void init() {
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        Service OpenWeather = new OpenWeatherService();
        serviceManager.addService(GeoCode);
        serviceManager.addService(OpenWeather);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void activateLocation_knownPlaceName_true()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {

        Location newLocation = locationManager.addLocationByPlaceName("Castelló de la Plana");
        locationManager.validateLocation(newLocation.getId());
        // When
        boolean result = locationManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(locationManager.hasActiveLocation(newLocation.getId()));
        assertFalse(locationManager.hasNonActiveLocation(newLocation.getId()));
    }

    @Test
    public void activateLocation_unKnownPlaceName_true()
            throws ServiceNotAvailableException, GPSNotAvailableException, NotValidCoordinatesException {
        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        Location newLocation = locationManager.addLocationByCoords(coords);
        locationManager.validateLocation(newLocation.getId());
        // When
        boolean result = locationManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(locationManager.hasActiveLocation(newLocation.getId()));
        assertFalse(locationManager.hasNonActiveLocation(newLocation.getId()));
    }

    @Test
    public void activateLocation_locationAlreadyActive_false() {
        // Given
        Location location = locationManager.getActiveLocations().get(0);
        // When
        boolean result = locationManager.activateLocation(location.getId());
        // Then
        assertFalse(result);
        assertEquals(2, locationManager.getActiveLocations().size());
    }
}
