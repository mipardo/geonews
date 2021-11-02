package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.BeforeClass;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU07 {
    private static LocationManager locationManager;

    @Test
    public void getCoords_knownPlaceName_validCoords()
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
    public void getCoords_unknownPlaceName_UnrecognizedPlaceNameException()
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

    @Test (expected = ServiceNotAvailableException.class)
    public void getCoords_geocodeNotAvailable_ServiceNotAvailableException()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        // When
        ((CoordsSearchService) locationManager.getService("Geocode"))
                .getCoordsFrom("Castellón de la Plana");
    }
}
