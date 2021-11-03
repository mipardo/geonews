package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.List;

import es.uji.geonews.model.GPSManager;
import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R1_HU04 {
    private static LocationManager locationManager;

    @Test
    public void validatePlaceName_E1PlaceNameRecognized_ListWithTwoActiveServices()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, GPSNotAvailableException {
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        Service openWeather = new OpenWeatherService();
        Service airVisual = new AirVisualService();
        serviceManager.addService(geocode);
        serviceManager.addService(openWeather);
        serviceManager.addService(airVisual);
        locationManager = new LocationManager(serviceManager);
        // When
        Location newLocation = locationManager.addLocation("Castellon de la Plana");
        List<String> services = locationManager.validateLocation(newLocation.getId());
        // Then
        assertEquals(2, services.size());
        assertTrue(services.contains("OpenWeather"));
        assertTrue(services.contains("AirVisual"));
    }

    @Test
    public void validatePlaceName_E2NoApiAvailable_EmptyList()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, GPSNotAvailableException {
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        // When
        Location newLocation = locationManager.addLocation("Castellon de la Plana");
        List<String> services = locationManager.validateLocation(newLocation.getId());
        // Then
        assertEquals(0, services.size());
    }

}
