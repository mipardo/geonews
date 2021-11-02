package es.uji.geonews.integration;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU01 {

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
