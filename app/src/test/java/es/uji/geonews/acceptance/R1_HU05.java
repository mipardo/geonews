package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU05 {
    private static LocationManager locationManager;

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithTwoActiveServices()
            throws ServiceNotAvailableException, NotValidCoordinatesException, GPSNotAvailableException {
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        Service OpenWeather = new OpenWeatherService();
        Service AirVisual = new AirVisualService();
        serviceManager.addService(GeoCode);
        serviceManager.addService(OpenWeather);
        serviceManager.addService(AirVisual);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(39.98001, -0.049900);
        Location newLocation = locationManager.addLocationByCoords(coords);
        List<String> services = locationManager.validateLocation(newLocation.getId());
        // Then
        assertEquals(2, services.size());
        assertTrue(services.contains("OpenWeather"));
        assertTrue(services.contains("AirVisual"));
    }

    @Test
    public void validateCoords_NoApiAvailable_EmptyList()
            throws ServiceNotAvailableException, NotValidCoordinatesException, GPSNotAvailableException {
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = new GeographCoords(39.98001, -0.049900);
        Location newLocation = locationManager.addLocationByCoords(coords);
        List<String> services = locationManager.validateLocation(newLocation.getId());
        // Then
        assertEquals(0, services.size());
    }
}
