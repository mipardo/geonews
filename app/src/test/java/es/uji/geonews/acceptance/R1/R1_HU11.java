package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU11 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private Location castellon;
    private Location valencia;

    @Before
    public void init() throws ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NotValidCoordinatesException {
        //Given
        GeocodeService geocode = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
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
        serviceManager.addService(new AirVisualService());
        locationManager.activateLocation(valencia.getId());
        // When
        boolean result = locationManager.removeLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, locationManager.getNonActiveLocations().size());
        assertEquals(1, locationManager.getActiveLocations().size());
    }
}
