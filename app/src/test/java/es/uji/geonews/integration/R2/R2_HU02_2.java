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
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU02_2 {
    private CoordsSearchService coordsSearchServiceMocked;
    private CurrentsService currentsServiceMocked;
    private ArrayList<ServiceHttp> services;
    private ServiceManager serviceManagerMocked;
    private LocationManager locationManager;
    private LocationManager locationManagerMocked;
    private OpenWeatherService openWeatherServiceMocked;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        currentsServiceMocked = mock(CurrentsService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
        locationManagerMocked = mock(LocationManager.class);
        services = new ArrayList<>();
        openWeatherServiceMocked = mock(OpenWeatherService.class);
    }

    @Test
    public void checkService_OneActivation()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        locationManager.addServiceToLocation(openWeatherServiceMocked.getServiceName(),castellon.getId());
        // Act

        boolean confirmation =locationManager.removeServiceFromLocation(openWeatherServiceMocked.getServiceName(),castellon.getId());
        // Assert
        assertEquals(locationManager.getLocationService(castellon.getId()).size(),0);
        assertTrue(confirmation);


    }
    @Test
    public void checkService_OneActivation_alreadyActive()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        // Act

        boolean confirmation =locationManager.removeServiceFromLocation(openWeatherServiceMocked.getServiceName(),castellon.getId());
        // Assert
        assertEquals(locationManager.getLocationService(castellon.getId()).size(),0);
        assertFalse(confirmation);



    }
    @Test(expected = ServiceNotAvailableException.class)
    public void checkService_OneActivation_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenThrow(new ServiceNotAvailableException());

        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        //locationManager.addLocationService(openWeatherServiceMocked.getServiceName(),castellon.getId());
        // Act
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        boolean confirmation =locationManager.removeServiceFromLocation(openWeatherServiceMocked.getServiceName(),castellon.getId());
        // Assert




    }
}
