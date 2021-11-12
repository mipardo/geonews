package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R1_HU09 {

    private LocationManager locationManager;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        GeocodeService geocode = new GeocodeService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
        locationManager.addLocation("Castello de la Plana");
    }

    @Test
    public void assignAlias_ValidNewAlias_True() throws NoLocationRegisteredException {
        // When
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        boolean result = locationManager.setAliasToLocation("Casa", idLocation);

        // Then
        assertTrue(result);
        assertEquals("Casa", locationManager.getLocation(idLocation).getAlias());
    }

    @Test
    public void assignAlias_InvalidNewAlias_False() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        locationManager.addLocation("Valencia");
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        locationManager.setAliasToLocation("Casa", idLocation);

        // When
        idLocation = locationManager.getNonActiveLocations().get(1).getId();
        boolean result = locationManager.setAliasToLocation("Casa", idLocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void modifyAlias_ValidAlias_True() throws NoLocationRegisteredException {
        // Given
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        locationManager.setAliasToLocation("Casa", idLocation);
        // When
        boolean result = locationManager.setAliasToLocation("Mi casa", idLocation);

        // Then
        assertTrue(result);
        assertEquals("Mi casa", locationManager.getLocation(idLocation).getAlias());

    }

    @Test
    public void modifyAlias_InvalidAlias_False() throws NoLocationRegisteredException {
        // Given
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        locationManager.setAliasToLocation("Mi casa", idLocation);

        // When
        boolean result = locationManager.setAliasToLocation("Mi casa", idLocation);

        // Then
        assertFalse(result);
    }
}
