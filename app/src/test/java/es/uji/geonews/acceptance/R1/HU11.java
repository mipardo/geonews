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
import es.uji.geonews.model.services.GeocodeService;

public class HU11 {
    private LocationManager locationManager;
    private Location castellon;
    private Location valencia;

    @Before
    public void init() throws ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NotValidCoordinatesException {
        //Given
        GeocodeService geocode = new GeocodeService();
        locationManager = new LocationManager(geocode);
        castellon = locationManager.addLocation("Castello de la Plana");
        valencia = locationManager.addLocation("Valencia");
    }

    @Test
    public void removeLocation_LocationNotInActiveLocations_True() throws NoLocationRegisteredException {
        // When
        boolean result = locationManager.removeLocation(castellon.getId());

        // Then
        assertTrue(result);
        assertEquals(1, locationManager.getNonActiveLocations().size());
    }

    @Test
    public void removeLocation_LocationInActiveLocations_False() throws NoLocationRegisteredException {
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
