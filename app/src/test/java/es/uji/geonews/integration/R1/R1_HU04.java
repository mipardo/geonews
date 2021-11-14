package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
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
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class R1_HU04 {
    private CurrentsService currentsServiceMocked;
    private ServiceManager serviceManager;
    private LocationManager locationManager;


    @Before
    public void init() throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        GeocodeService geocodeServiceMocked = mock(GeocodeService.class);
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords(any()))
                .thenReturn(new GeographCoords(39.46975, -0.3739));

        currentsServiceMocked = mock(CurrentsService.class);
        when(currentsServiceMocked.getServiceName()).thenReturn(ServiceName.CURRENTS);

        serviceManager = new ServiceManager();
        serviceManager.addService(currentsServiceMocked);

        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithOneServiceActive()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(currentsServiceMocked.isAvailable()).thenReturn(true);
        when(currentsServiceMocked.validateLocation(any())).thenReturn(true);
        Location location = locationManager.addLocation("Valencia");
        serviceManager.initLocationServices(location);
        // Act
        List<ServiceName> activeServices = serviceManager.validateLocation(location);
        // Assert
        verify(currentsServiceMocked, times(1)).isAvailable();
        verify(currentsServiceMocked, times(1)).validateLocation(any());
        assertEquals(1, activeServices.size());
        assertTrue(activeServices.contains(ServiceName.CURRENTS));
    }

    @Test
    public void validatePlaceName_NoApiAvailable_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(currentsServiceMocked.validateLocation(any())).thenReturn(false);
        Location location = locationManager.addLocation("Valencia");
        serviceManager.initLocationServices(location);
        // Act
        List<ServiceName> activeServices = serviceManager.validateLocation(location);
        // Assert
        verify(currentsServiceMocked, times(1)).isAvailable();
        assertEquals(0, activeServices.size());
    }
}
