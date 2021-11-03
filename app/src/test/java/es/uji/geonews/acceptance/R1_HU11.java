package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.GPSNotAvailableException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class R1_HU11 {
    private static LocationManager locationManager;
    private static Location castellon;
    private static Location valencia;

    @BeforeClass
    public static void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, GPSNotAvailableException {
        //Given
        Service geocode = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        castellon = locationManager.addLocation("Castellon de la Plana");
        valencia = locationManager.addLocation("Valencia");
    }

    @Test
    public void removeLocation_E1LocationNotInActiveLocations_True()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        // When
        boolean result = locationManager.removeLocation(castellon.getId());

        // Then
        assertTrue(result);
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }

    @Test
    public void removeLocation_E2LocationInActiveLocations_False()
            throws ServiceNotAvailableException, NotValidCoordinatesException {
        //Given
        locationManager.activateLocation(valencia.getId());

        // When
        boolean result = locationManager.removeLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(0, locationManager.getNonActiveLocations().size());
        assertEquals(1, locationManager.getActiveLocations().size());
    }

}
