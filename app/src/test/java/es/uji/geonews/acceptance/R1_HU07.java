package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R1_HU07 {
    private static LocationManager locationManager;

    @Test
    public void getCoords_E1KnownPlaceName_validCoords()
    throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        // When
        GeographCoords coords = ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castelló de la Plana");
        // Then
        assertEquals(39.98920, coords.getLatitude(), 0.01);
        assertEquals(-0.03621, coords.getLongitude(), 0.01);
    }

    @Test (expected = UnrecognizedPlaceNameException.class)
    public void getCoords_E2UnknownPlaceName_UnrecognizedPlaceNameException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        // When
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("asdfxxrtg");
    }

}
