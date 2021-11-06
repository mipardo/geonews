package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;
import static org.junit.Assert.assertNull;

public class R1_HU08 {

    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service GeoCode = new CoordsSearchService();
        serviceManager.addService(GeoCode);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void getPlaceName_KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        GeographCoords coords = new GeographCoords(39.98920,-0.03621);
        String placeName = ((CoordsSearchService) locationManager.getService("Geocode")).
                getPlaceNameFromCoords(coords);

        // Then
        assertEquals("Castelló de la Plana", placeName);
    }

    @Test
    public void getPlaceName_UnknownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        GeographCoords coords = new GeographCoords(33.6500,-41.1900);
        String placeName = ((CoordsSearchService) locationManager.
                getService("Geocode")).getPlaceNameFromCoords(coords);

        // Then
        assertNull(placeName);
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void getPlaceName_InvalidCoords_NotValidCoordinatesException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        GeographCoords coords = new GeographCoords(100.0000,-41.1900);

        ((CoordsSearchService) locationManager.
                getService("Geocode")).getPlaceNameFromCoords(coords);
    }



}
