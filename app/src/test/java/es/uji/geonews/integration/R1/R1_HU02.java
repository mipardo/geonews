package es.uji.geonews.integration.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU02 {
    private CoordsSearchService coordsSearchServiceMocked;
    private ServiceManager serviceManagerMocked;
    private LocationManager locationManager;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
    }

    @Test
    public void registerLocationByCoords_KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenReturn("Castellon de la Plana");
        // Act
        locationManager.addLocation("39.98920, -0.03621");
        // Assert
        verify(coordsSearchServiceMocked, times(1)).isAvailable();
        verify(coordsSearchServiceMocked, times(1)).getPlaceNameFromCoords(any());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test
    public void registerLocationByCoords_UnknownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenReturn(null);
        // Act
        locationManager.addLocation("33.6500, -41.1900");
        // Assert
        verify(coordsSearchServiceMocked, times(1)).getPlaceNameFromCoords(any());
        verify(coordsSearchServiceMocked, times(1)).isAvailable();
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertNull(locationManager.getNonActiveLocations().get(0).getPlaceName());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByCoords_WithoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getPlaceNameFromCoords(any())).thenThrow(new ServiceNotAvailableException());
        // Act
        locationManager.addLocation("39.98920, -0.03621");
    }

}
