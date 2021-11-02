package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.BeforeClass;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU01 {

    @Test
    public void registerLocationByPlaceName_knownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        // Arrange
        CoordsSearchService mockCoordsSearchSrv = mock(CoordsSearchService.class);
        when(mockCoordsSearchSrv.isAvailable()).thenReturn(true);
        when(mockCoordsSearchSrv.getCoordsFrom(anyString())).thenReturn(new GeographCoords(39.98920, -0.03621));
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchSrv);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        Location location = locationManager.addLocationByPlaceName("Castellon de la Plana");
        // Assert
        verify(mockCoordsSearchSrv, times(1)).isAvailable();
        verify(mockCoordsSearchSrv, times(1)).getCoordsFrom("Castellon de la Plana");
    }


    @Test(expected=UnrecognizedPlaceNameException.class)
    public void registerLocationByPlaceName_unknownPlaceName_UnrecognizedPlaceNameException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        // Arrange
        CoordsSearchService mockCoordsSearchSrv = mock(CoordsSearchService.class);
        when(mockCoordsSearchSrv.isAvailable()).thenReturn(true);
        when(mockCoordsSearchSrv.getCoordsFrom(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchSrv);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        locationManager.addLocationByPlaceName("asddf");
        // Assert
        //verify()

    }

    @Test(expected= ServiceNotAvailableException.class)
    public void registerLocationByPlaceName_withoutConnection_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException {
        // Arrange
        CoordsSearchService mockCoordsSearchSrv = mock(CoordsSearchService.class);
        when(mockCoordsSearchSrv.isAvailable()).thenReturn(true);
        when(mockCoordsSearchSrv.getCoordsFrom(anyString())).thenThrow(new ServiceNotAvailableException());
        ServiceManager mockServiceManager = mock(ServiceManager.class);
        when(mockServiceManager.getService("Geocode")).thenReturn(mockCoordsSearchSrv);
        LocationManager locationManager = new LocationManager(mockServiceManager);
        // Act
        locationManager.addLocationByPlaceName("Bilbao");
        // Assert
        //verify()
    }

}
