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
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU04 {
    private CoordsSearchService coordsSearchServiceMocked;
    private CurrentsService currentsServiceMocked;
    private ArrayList<ServiceHttp> services;
    private ServiceManager serviceManagerMocked;
    private LocationManager locationManager;


    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        currentsServiceMocked = mock(CurrentsService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
        services = new ArrayList<>();
    }

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithOneServiceActive()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(any()))
                .thenReturn(new GeographCoords(39.46975, -0.3739));
        services.add(currentsServiceMocked);
        when(serviceManagerMocked.getHttpServices()).thenReturn(services);
        when(currentsServiceMocked.getServiceName()).thenReturn("Currents");
        when(currentsServiceMocked.validateLocation(any())).thenReturn(true);

        // Act
        Location location = locationManager.addLocation("Valencia");
        List<String> activeServices = locationManager.validateLocation(location.getId());
        // Assert
        verify(serviceManagerMocked, times(1)).getHttpServices();
        verify(currentsServiceMocked, times(2)).getServiceName();
        verify(currentsServiceMocked, times(1)).validateLocation(any());
        assertEquals(1, activeServices.size());
        assertTrue(activeServices.contains("Currents"));

    }

    @Test
    public void validatePlaceName_NoApiAvailable_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(any()))
                .thenReturn(new GeographCoords(39.46975, -0.3739));
        services.add(currentsServiceMocked);
        when(serviceManagerMocked.getHttpServices()).thenReturn(services);
        when(currentsServiceMocked.getServiceName()).thenReturn("Currents");
        when(currentsServiceMocked.validateLocation(any())).thenReturn(false);
        // Act
        Location location = locationManager.addLocation("Valencia");
        List<String> activeServices = locationManager.validateLocation(location.getId());
        // Assert
        verify(serviceManagerMocked, times(1)).getHttpServices();
        verify(currentsServiceMocked, times(1)).getServiceName();
        verify(currentsServiceMocked, times(1)).validateLocation(any());
        assertEquals(0, activeServices.size());
    }


}
