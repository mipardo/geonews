package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU11 {
    private LocationManager locationManager;
    private Location castellon;
    private Location valencia;

    @Before
    public void init() throws ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NotValidCoordinatesException {
        //Given
        Service geocode = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        castellon = locationManager.addLocation("Castello de la Plana");
        valencia = locationManager.addLocation("Valencia");
    }

    @Test
    public void removeLocation_LocationNotInActiveLocations_True() {
        // When
        boolean result = locationManager.removeLocation(castellon.getId());

        // Then
        assertTrue(result);
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }

    @Test
    public void removeLocation_LocationInActiveLocations_False() {
        //Given
        locationManager.activateLocation(valencia.getId());

        // When
        boolean result = locationManager.removeLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(1, locationManager.getActiveLocations().size());
    }
}
