package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

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

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
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
    public void activateLocation_E1KnownPlaceName_true()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, GPSNotAvailableException {

        Location newLocation = locationManager.addLocation("Castelló de la Plana");
        locationManager.validateLocation(newLocation.getId());
        // When
        boolean result = locationManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(locationManager.getLocaton(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_E2UnKnownPlaceName_true()
            throws ServiceNotAvailableException, GPSNotAvailableException,
            NotValidCoordinatesException, UnrecognizedPlaceNameException {
        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        Location newLocation = locationManager.addLocation(coords.toString());
        locationManager.validateLocation(newLocation.getId());
        // When
        boolean result = locationManager.activateLocation(newLocation.getId());
        // Then
        assertTrue(result);
        assertTrue(locationManager.getLocaton(newLocation.getId()).isActive());
    }

    @Test
    public void activateLocation_E3LocationAlreadyActive_false() {
        // Given
        Location location = locationManager.getActiveLocations().get(0);
        // When
        boolean result = locationManager.activateLocation(location.getId());
        // Then
        assertFalse(result);
        assertEquals(2, locationManager.getActiveLocations().size());
    }
}
