package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU02_1 {
    private ServiceManager serviceManager;
    private OpenWeatherService openWeatherServiceMocked;
    private Location castellon;

    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException, NotValidCoordinatesException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));

        openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);

        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);

        castellon = locationManager.addLocation("Castelló de la Plana");
        serviceManager.initLocationServices(castellon);
    }

    @Test
    public void activateServiceInLocation_ServiceNotActiveYet_True()
            throws ServiceNotAvailableException {
        //Arrange
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        // Act
        boolean confirmation = serviceManager.addServiceToLocation(ServiceName.OPEN_WEATHER,castellon);
        // Assert
        assertEquals(1, serviceManager.getServicesOfLocation(castellon.getId()).size());
        assertTrue(confirmation);
    }

    @Test
    public void activateServiceInLocation_ServiceAlreadyActive_False() throws ServiceNotAvailableException {
        // Arrange
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        serviceManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        // Act
        boolean confirmation = serviceManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        // Assert
        assertEquals(1, serviceManager.getServicesOfLocation(castellon.getId()).size());
        assertFalse(confirmation);
    }

    @Test(expected = ServiceNotAvailableException.class)
    public void activateServiceInLocation_ServiceNotActiveYet_ServiceNotAvailableException()
            throws ServiceNotAvailableException {
        // Arrange
        when(openWeatherServiceMocked.isAvailable()).thenReturn(false);
        // Act
        serviceManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
    }
}