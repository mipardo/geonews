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
public class R1_HU10 {
    private static LocationManager locationManager;
    private static Location location;

    @BeforeClass
    public static void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, GPSNotAvailableException {
        //Given
        Service geocode = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        location = locationManager.addLocation("Castelló de la Plana");
        locationManager.activateLocation(location.getId());
    }

    @Test
    public void deactivateLocation_activeLocation_True()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // When
        boolean result = locationManager.deactivateLocation(location.getId());

        // Then
        assertTrue(result);
        assertEquals(0, locationManager.getActiveLocations().size());
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }

    @Test
    public void deactivateLocation_nonActiveLocation_False() {
        // When
        boolean result = locationManager.deactivateLocation(location.getId());

        // Then
        assertFalse(result);
    }
}