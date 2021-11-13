package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.managers.ServiceManager;

public class R2_HU02_1 {
    private ServiceManager serviceManager;
    private OpenWeatherService openWeatherServiceMocked;
    private Location castellon;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException, NotValidCoordinatesException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));

        openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");

        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);

        castellon = locationManager.addLocation("Castelló de la Plana");
    }

    @Test
    public void activateServiceInLocation_ServiceNotActiveYet_True() {

        // Act
        boolean confirmation = serviceManager.addServiceToLocation("OpenWeather",castellon);

        // Assert
        assertEquals(serviceManager.getServicesOfLocation(castellon.getId()).size(),1);
        assertTrue(confirmation);
    }

    @Test
    public void activateServiceInLocation_ServiceAlreadyActive_False(){
        // Arrange
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        serviceManager.addServiceToLocation(openWeatherServiceMocked.getServiceName(), castellon);
        // Act
        boolean confirmation = serviceManager.addServiceToLocation("OpenWeather", castellon);
        // Assert
        assertEquals(serviceManager.getServicesOfLocation(castellon.getId()).size(),1);
        assertFalse(confirmation);
    }

    @Test(expected = ServiceNotAvailableException.class)
    public void activateServiceInLocation_ServiceNotActiveYet_ServiceNotAvailableException() {
        // Act
        boolean confirmation =serviceManager.addServiceToLocation("OpenWeather", castellon);
    }
}
