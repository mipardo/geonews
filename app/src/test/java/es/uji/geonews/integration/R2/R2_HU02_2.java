package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
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
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;

public class R2_HU02_2 {
    private GeocodeService geocodeServiceMocked;
    private CurrentsService currentsServiceMocked;
    private ServiceManager serviceManager;
    private LocationManager locationManager;
    private LocationManager locationManagerMocked;
    private OpenWeatherService openWeatherServiceMocked;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        currentsServiceMocked = mock(CurrentsService.class);
        serviceManager = new ServiceManager();
        serviceManager.addService(geocodeServiceMocked);
        locationManager = new LocationManager(geocodeServiceMocked);
        locationManagerMocked = mock(LocationManager.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
    }

    @Test
    public void checkService_OneDesactivation_True()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        serviceManager.initLocationServices(castellon);
        serviceManager.addServiceToLocation(openWeatherServiceMocked.getServiceName(),castellon);
        // Act

        boolean confirmation =serviceManager.removeServiceFromLocation(openWeatherServiceMocked.getServiceName(),castellon);
        // Assert
        assertEquals(serviceManager.getServicesOfLocation(castellon.getId()).size(),0);
        assertTrue(confirmation);


    }
    @Test
    public void checkService_OneDesactivation_False()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        // Act

        boolean confirmation =serviceManager.removeServiceFromLocation(openWeatherServiceMocked.getServiceName(),castellon);
        // Assert
        assertEquals(serviceManager.getServicesOfLocation(castellon.getId()).size(),0);
        assertFalse(confirmation);



    }
    @Test(expected = ServiceNotAvailableException.class)
    public void checkService_OneActivation_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenThrow(new ServiceNotAvailableException());

        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        //locationManager.addLocationService(openWeatherServiceMocked.getServiceName(),castellon.getId());
        // Act
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        boolean confirmation =serviceManager.removeServiceFromLocation(openWeatherServiceMocked.getServiceName(),castellon);
        // Assert
    }
}
