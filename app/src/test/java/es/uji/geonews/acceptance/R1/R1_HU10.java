package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.managers.ServiceManager;

public class R1_HU10 {
    private LocationManager locationManager;
    private Location location;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        //Given
        GeocodeService geocode = new GeocodeService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        serviceManager.addService(new AirVisualService());
        locationManager = new LocationManager(geocode);
        location = locationManager.addLocation("Castello de la Plana");
        locationManager.activateLocation(location.getId());
    }

    @Test
    public void deactivateLocation_ActiveLocation_True()
            throws NoLocationRegisteredException {
        // When
        boolean result = locationManager.deactivateLocation(location.getId());

        // Then
        assertTrue(result);
        assertEquals(0, locationManager.getActiveLocations().size());
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }

    @Test
    public void deactivateLocation_NonActiveLocation_False() {
        // Given
        locationManager.deactivateLocation(location.getId());

        // When
        boolean result = locationManager.deactivateLocation(location.getId());

        // Then
        assertFalse(result);
    }
}
