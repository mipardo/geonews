package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU05 {
    private OpenWeatherService openWeatherServiceMocked;
    private AirVisualService airVisualServiceMocked;
    private ServiceManager serviceManager;
    private LocationManager locationManager;
    private Location location;


    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(any()))
                .thenReturn(new GeographCoords(39.98920, -0.03621));
        airVisualServiceMocked = mock(AirVisualService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);
        when(airVisualServiceMocked.getServiceName()).thenReturn(ServiceName.AIR_VISUAL);

        serviceManager = new ServiceManager();
        serviceManager.addService(airVisualServiceMocked);
        serviceManager.addService(openWeatherServiceMocked);
        locationManager = new LocationManager(geocodeServiceMocked);

        location = new Location(2, null,
                new GeographCoords(39.50337, -0.40466), LocalDate.now());
    }

    @Test
    public void validateLocation_PlaceNameRecognized_ListWithTwoActiveServices() {
        // Arrange
        when(airVisualServiceMocked.isAvailable()).thenReturn(true);
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(true);
        when(openWeatherServiceMocked.validateLocation(any())).thenReturn(true);
        locationManager.addLocation(location);
        // Act
        List<ServiceName> activeServices = serviceManager.validateLocation(location);
        // Assert
        verify(openWeatherServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(1)).validateLocation(any());
        verify(openWeatherServiceMocked, times(1)).validateLocation(any());
        assertEquals(2, activeServices.size());
        assertTrue(activeServices.contains(ServiceName.AIR_VISUAL));
        assertTrue(activeServices.contains(ServiceName.OPEN_WEATHER));
    }

    @Test
    public void validateLocation_NoApiAvailable_EmptyList() {
        // Arrange
        when(airVisualServiceMocked.isAvailable()).thenReturn(false);
        when(openWeatherServiceMocked.isAvailable()).thenReturn(false);
        locationManager.addLocation(location);
        // Act
        List<ServiceName> activeServices = serviceManager.validateLocation(location);
        // Assert
        verify(openWeatherServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(1)).isAvailable();
        verify(airVisualServiceMocked, times(0)).validateLocation(any());
        verify(openWeatherServiceMocked, times(0)).validateLocation(any());
        assertEquals(0, activeServices.size());
    }
}
