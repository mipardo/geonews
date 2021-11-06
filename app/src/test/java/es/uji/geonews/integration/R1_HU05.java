package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU05 {
    private CoordsSearchService coordsSearchServiceMocked;
    private OpenWeatherService openWeatherServiceMocked;
    private AirVisualService airVisualServiceMocked;
    private ArrayList<ServiceHttp> services;
    private ServiceManager serviceManagerMocked;
    private LocationManager locationManager;


    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
        services = new ArrayList<>();
    }

    @Test
    public void validateLocation_PlaceNameRecognized_ListWithTwoActiveServices()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(any()))
                .thenReturn(new GeographCoords(39.98920, -0.03621));
        services.add(openWeatherServiceMocked);
        services.add(airVisualServiceMocked);
        when(serviceManagerMocked.getHttpServices()).thenReturn(services);
        when(airVisualServiceMocked.getServiceName()).thenReturn("AirVisual");
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        // Act
        Location location = locationManager.addLocation("Castellon de la Plana");
        List<String> activeServices = locationManager.validateLocation(location.getId());
        // Assert
        verify(serviceManagerMocked, times(1)).getHttpServices();
        verify(airVisualServiceMocked, times(2)).getServiceName();
        verify(openWeatherServiceMocked, times(2)).getServiceName();
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
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(any()))
                .thenReturn(new GeographCoords(39.98920, -0.03621));
        services.add(openWeatherServiceMocked);
        services.add(airVisualServiceMocked);
        when(serviceManagerMocked.getHttpServices()).thenReturn(services);
        when(airVisualServiceMocked.getServiceName()).thenReturn("AirVisual");
        when(openWeatherServiceMocked.getServiceName()).thenReturn("OpenWeather");
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(false);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(false);
        // Act
        Location location = locationManager.addLocation("Castellon de la Plana");
        List<String> activeServices = locationManager.validateLocation(location.getId());
        // Assert
        verify(serviceManagerMocked, times(1)).getHttpServices();
        verify(airVisualServiceMocked, times(1)).getServiceName();
        verify(openWeatherServiceMocked, times(1)).getServiceName();
        verify(airVisualServiceMocked, times(1)).validateLocation(any());
        verify(openWeatherServiceMocked, times(1)).validateLocation(any());
        assertEquals(0, activeServices.size());
    }


}
