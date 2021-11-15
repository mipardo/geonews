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

import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;

public class HU02 {
    private GeocodeService geocodeServiceMocked;
    private LocationManager locationManager;

    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void registerLocationByCoords_KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn("Castellon de la Plana");
        // Act
        locationManager.addLocation("39.98920, -0.03621");
        // Assert
        verify(geocodeServiceMocked, times(1)).isAvailable();
        verify(geocodeServiceMocked, times(1)).getPlaceName(any());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test
    public void registerLocationByCoords_UnknownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenReturn(null);
        // Act
        locationManager.addLocation("33.6500, -41.1900");
        // Assert
        verify(geocodeServiceMocked, times(1)).getPlaceName(any());
        verify(geocodeServiceMocked, times(1)).isAvailable();
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertNull(locationManager.getNonActiveLocations().get(0).getPlaceName());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByCoords_WithoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getPlaceName(any())).thenThrow(new ServiceNotAvailableException());
        // Act
        locationManager.addLocation("39.98920, -0.03621");
    }

}
