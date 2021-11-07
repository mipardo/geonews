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

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU04 {
    private CoordsSearchService coordsSearchServiceMocked;
    private CurrentsService currentsServiceMocked;
    private ServiceManager serviceManagerSpied;
    private LocationManager locationManager;


    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        currentsServiceMocked = mock(CurrentsService.class);
        when(currentsServiceMocked.getServiceName()).thenReturn("Currents");
        ServiceManager serviceManager = new ServiceManager();
        serviceManagerSpied = spy(serviceManager);
        doReturn(coordsSearchServiceMocked).when(serviceManagerSpied).getService("Geocode");
        locationManager = new LocationManager(serviceManagerSpied);
    }

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithOneServiceActive()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(any()))
                .thenReturn(new GeographCoords(39.46975, -0.3739));
        serviceManagerSpied.addService(currentsServiceMocked);
        when(currentsServiceMocked.validateLocation(any())).thenReturn(true);

        // Act
        Location location = locationManager.addLocation("Valencia");
        List<String> activeServices = locationManager.validateLocation(location.getId());
        // Assert
        verify(serviceManagerSpied, times(1)).getHttpServices();
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
        serviceManagerSpied.addService(currentsServiceMocked);
        when(currentsServiceMocked.validateLocation(any())).thenReturn(false);
        // Act
        Location location = locationManager.addLocation("Valencia");
        List<String> activeServices = locationManager.validateLocation(location.getId());
        // Assert
        verify(serviceManagerSpied, times(1)).getHttpServices();
        verify(currentsServiceMocked, times(1)).validateLocation(any());
        assertEquals(0, activeServices.size());
    }
}
