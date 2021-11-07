package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;


import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
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
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        locationManager.addLocation("Castello de la Plana");
    }

    @Test
    public void assignAlias_ValidNewAlias_True() {
        // When
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        boolean result = locationManager.setAliasToLocation("Casa", idLocation);

        // Then
        assertTrue(result);
        assertEquals("Casa", locationManager.getLocaton(idLocation).getAlias());
    }

    @Test
    public void assignAlias_InvalidNewAlias_False() throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
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
    public void modifyAlias_ValidAlias_True() {
        // Given
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        locationManager.setAliasToLocation("Casa", idLocation);
        // When
        boolean result = locationManager.setAliasToLocation("Mi casa", idLocation);

        // Then
        assertTrue(result);
        assertEquals("Mi casa", locationManager.getLocaton(idLocation).getAlias());

    }

    @Test
    public void modifyAlias_InvalidAlias_False() {
        // Given
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        locationManager.setAliasToLocation("Mi casa", idLocation);

        // When
        boolean result = locationManager.setAliasToLocation("Mi casa", idLocation);

        // Then
        assertFalse(result);
    }
}
