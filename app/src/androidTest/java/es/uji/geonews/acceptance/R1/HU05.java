package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU05 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;

    @Before
    public void init(){
        serviceManager = new ServiceManager();
        GeocodeService geocode = new GeocodeService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
    }

    @Test
    public void validateLocation_PlaceNameRecognized_ListWithTwoActiveServices()
            throws ServiceNotAvailableException, NotValidCoordinatesException,
            UnrecognizedPlaceNameException {
        Service OpenWeather = new OpenWeatherService();
        Service AirVisual = new AirVisualService();
        serviceManager.addService(OpenWeather);
        serviceManager.addService(AirVisual);
        // When
        GeographCoords coords = new GeographCoords(39.98001, -0.049900);
        Location newLocation = locationManager.addLocation(coords.toString());
        List<ServiceName> services = serviceManager.validateLocation(newLocation);
        // Then
        assertEquals(2, services.size());
        assertTrue(services.contains(ServiceName.OPEN_WEATHER));
        assertTrue(services.contains(ServiceName.AIR_VISUAL));
    }

    @Test
    public void validateLocation_NoApiAvailable_EmptyList()
            throws ServiceNotAvailableException, NotValidCoordinatesException,
            UnrecognizedPlaceNameException {
        // When
        GeographCoords coords = new GeographCoords(39.98001, -0.049900);
        Location newLocation = locationManager.addLocation(coords.toString());
        List<ServiceName> services = serviceManager.validateLocation(newLocation);
        // Then
        assertEquals(0, services.size());
    }
}
