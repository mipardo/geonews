package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU02 {

    @Test
    public void registerLocationByCoords_E1KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getPlaceNameFromCoords(any())).thenReturn("Castellon de la Plana");
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchService);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        locationManager.addLocation("39.98920, -0.03621");
        // Assert
        verify(mockCoordsSearchService, times(1)).isAvailable();
        verify(mockCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
    }


    @Test
    public void registerLocationByCoords_E2UnknownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getPlaceNameFromCoords(any())).thenReturn(null);
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchService);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        locationManager.addLocation("33.65001, -41.19001"); //TODO: Si lat o long acaban en 0 se trunca y da problemas
        // Assert
        verify(mockCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
        verify(mockCoordsSearchService, times(1)).isAvailable();
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertNull(locationManager.getNonActiveLocations().get(0).getPlaceName());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByCoords_E5WithoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        CoordsSearchService mockCoordsSearchService = mock(CoordsSearchService.class);
        when(mockCoordsSearchService.isAvailable()).thenReturn(true);
        when(mockCoordsSearchService.getPlaceNameFromCoords(any())).thenThrow(new ServiceNotAvailableException());
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchService);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        locationManager.addLocation("39.98920, -0.03621");
        // Assert
        verify(mockCoordsSearchService, times(1)).getPlaceNameFromCoords(any());
        verify(mockCoordsSearchService, times(1)).isAvailable();
        assertEquals(0, locationManager.getNonActiveLocations().size());
        assertEquals(0, locationManager.getActiveLocations().size());
    }

}
