package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU07 {
    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void getCoords_KnownPlaceName_validCoords()
    throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // When
        GeographCoords coords = ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castello de la Plana");
        // Then
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // When
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("asdfxxrtg");
    }

}
