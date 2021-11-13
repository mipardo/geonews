package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import es.uji.geonews.model.managers.ServiceManager;

public class R1_HU05 {
    private OpenWeatherService openWeatherServiceMocked;
    private AirVisualService airVisualServiceMocked;
    private ServiceManager serviceManager;
    private LocationManager locationManager;


    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(any()))
                .thenReturn(new GeographCoords(39.98920, -0.03621));
        airVisualServiceMocked = mock(AirVisualService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(airVisualServiceMocked.getServiceName()).thenReturn("AirVisual");

        serviceManager = new ServiceManager();
        serviceManager.addService(airVisualServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void validateLocation_PlaceNameRecognized_ListWithTwoActiveServices()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        Location location = locationManager.addLocation("Castellon de la Plana");
        serviceManager.initLocationServices(location);
        // Act
        List<String> activeServices = serviceManager.validateLocation(location);
        // Assert
        verify(openWeatherServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(1)).validateLocation(any());
        verify(openWeatherServiceMocked, times(1)).validateLocation(any());
        assertEquals(2, activeServices.size());
        assertTrue(activeServices.contains("AirVisual"));
        assertTrue(activeServices.contains("OpenWeather"));
    }

    @Test
    public void validateLocation_NoApiAvailable_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(airVisualServiceMocked.isAvailable()).thenReturn(false);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(false);
        Location location = locationManager.addLocation("Castellon de la Plana");
        serviceManager.initLocationServices(location);
        // Act
        List<String> activeServices = serviceManager.validateLocation(location);
        // Assert
        verify(openWeatherServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(0)).validateLocation(any());
        verify(openWeatherServiceMocked, times(0)).validateLocation(any());
        assertEquals(0, activeServices.size());
    }
}
