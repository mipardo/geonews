package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;
import static org.junit.Assert.assertNull;

public class R1_HU08 {
    private ServiceManager serviceManager;

    @Before
    public void init(){
        // Given
        serviceManager = new ServiceManager();
        GeocodeService geocode = new GeocodeService();
        serviceManager.addService(geocode);
    }

    @Test
    public void getPlaceName_KnownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        GeographCoords coords = new GeographCoords(39.98920,-0.03621);
        String placeName = ((GeocodeService) serviceManager.getService("Geocode")).
                getPlaceName(coords);

        // Then
        assertEquals("Castelló de la Plana", placeName);
    }

    @Test
    public void getPlaceName_UnknownCoords_nearestPlaceName()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        GeographCoords coords = new GeographCoords(33.6500,-41.1900);
        String placeName = ((GeocodeService) serviceManager.
                getService("Geocode")).getPlaceName(coords);

        // Then
        assertNull(placeName);
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void getPlaceName_InvalidCoords_NotValidCoordinatesException()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        GeographCoords coords = new GeographCoords(100.0000,-41.1900);

        ((GeocodeService) serviceManager.
                getService("Geocode")).getPlaceName(coords);
    }
}
