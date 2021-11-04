package es.uji.geonews.integration;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationFactory;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU05 {

    @Test
    public void validateLocation_E1PlaceNameRecognized_ListWithTwoActiveServices()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getCoordsFrom(any())).thenReturn(new GeographCoords(39.98920, -0.03621));
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchService);
        ArrayList<Service> services = new ArrayList<>();
        OpenWeatherService mockOpenWeatherService = mock(OpenWeatherService.class);
        AirVisualService mockAirVisualService = mock(AirVisualService.class);
        services.add(mockOpenWeatherService); services.add(mockAirVisualService);
        when(mockServiceManager.getServices()).thenReturn(services);
        when(mockAirVisualService.getServiceName()).thenReturn("AirVisual");
        when(mockOpenWeatherService.getServiceName()).thenReturn("OpenWeather");
        when(mockAirVisualService.validateLocation(any())).thenReturn(true);
        when(mockOpenWeatherService.validateLocation(any())).thenReturn(true);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        Location location = locationManager.addLocation("Castellon de la Plana");
        locationManager.validateLocation(location.getId());
        // Assert
        verify(mockServiceManager, times(1)).getServices();
        verify(mockAirVisualService, times(2)).getServiceName();
        verify(mockOpenWeatherService, times(2)).getServiceName();
        verify(mockAirVisualService, times(1)).validateLocation(any());
        verify(mockOpenWeatherService, times(1)).validateLocation(any());
    }




}
