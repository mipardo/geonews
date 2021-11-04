package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU07 {
    private static LocationManager locationManager;

    @Test
    public void getCoords_E1KnownPlaceName_validCoords()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        CoordsSearchService geocode = mock(CoordsSearchService.class);
        when(geocode.getCoordsFrom("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        ServiceManager serviceManager = mock(ServiceManager.class);
        when(serviceManager.getService("Geocode")).thenReturn(geocode);
        locationManager = new LocationManager(serviceManager);
        // Act
        GeographCoords coords = ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castelló de la Plana");
        // Assert
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
        // verify
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_E2UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        CoordsSearchService geocode = mock(CoordsSearchService.class);
        when(geocode.getCoordsFrom(anyString())).thenThrow(new UnrecognizedPlaceNameException());
        ServiceManager serviceManager = mock(ServiceManager.class);
        when(serviceManager.getService("Geocode")).thenReturn(geocode);
        locationManager = new LocationManager(serviceManager);
        // Act
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("asdfxxrtg");
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getCoords_E3GeocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Arrange
        CoordsSearchService geocode = mock(CoordsSearchService.class);
        when(geocode.getCoordsFrom(anyString())).thenThrow(new ServiceNotAvailableException());
        ServiceManager serviceManager = mock(ServiceManager.class);
        when(serviceManager.getService("Geocode")).thenReturn(geocode);
        locationManager = new LocationManager(serviceManager);
        // Act
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castellón de la Plana");
    }
}
