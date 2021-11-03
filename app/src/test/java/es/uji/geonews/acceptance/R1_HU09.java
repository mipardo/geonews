package es.uji.geonews.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
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
public class R1_HU09 {

    private static LocationManager locationManager;

    @BeforeClass
    public static void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException, GPSNotAvailableException {
        // Given
        ServiceManager serviceManager = new ServiceManager();
        Service geocode = new CoordsSearchService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(serviceManager);
        locationManager.addLocation("Castelló de la Plana");
    }

    @Test
    public void assignAlias_E1ValidNewAlias_True() {
        // When
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        boolean result = locationManager.setAliasToLocation("Casa", idLocation);

        // Then
        assertTrue(result);
        assertEquals("Casa", locationManager.getLocaton(idLocation).getAlias());
    }

    @Test
    public void assignAlias_E2InvalidNewAlias_False() {
        // When
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        boolean result = locationManager.setAliasToLocation("Casa", idLocation);

        // Then
        assertFalse(result);
    }

    @Test
    public void modifyAlias_E3ValidAlias_True() {
        // When
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        boolean result = locationManager.setAliasToLocation("Mi casa", idLocation);

        // Then
        assertTrue(result);
        assertEquals("Mi casa", locationManager.getLocaton(idLocation).getAlias());

    }

    @Test
    public void modifyAlias_E4InvalidAlias_False() {
        // When
        int idLocation = locationManager.getNonActiveLocations().get(0).getId();
        boolean result = locationManager.setAliasToLocation("Mi casa", idLocation);

        // Then
        assertFalse(result);
    }



}
