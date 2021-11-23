package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;


import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.managers.ServiceManager;

public class HU09 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init()
            throws ServiceNotAvailableException, UnrecognizedPlaceNameException,
            NotValidCoordinatesException {
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);

    }

    @Test
    public void assignAlias_ValidNewAlias_True() throws NoLocationRegisteredException,
            NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {

        Location castellon = geoNewsManager.addLocation("Castello de la plana");
        // When
        boolean result = geoNewsManager.setAliasToLocation("Casa", castellon.getId());

        // Then
        assertTrue(result);
        assertEquals("Casa", geoNewsManager.getLocation(castellon.getId()).getAlias());
    }

    @Test
    public void assignAlias_InvalidNewAlias_False() throws NotValidCoordinatesException,
            ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castello de la plana");
        Location valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.setAliasToLocation("Casa", castellon.getId());

        // When
        boolean result = geoNewsManager.setAliasToLocation("Casa", valencia.getId());

        // Then
        assertFalse(result);
    }

    @Test
    public void modifyAlias_ValidAlias_True() throws NoLocationRegisteredException,
            NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.setAliasToLocation("Casa", castellon.getId());

        // When
        boolean result = geoNewsManager.setAliasToLocation("Mi casa", castellon.getId());

        // Then
        assertTrue(result);
        assertEquals("Mi casa", geoNewsManager.getLocation(castellon.getId()).getAlias());

    }

    @Test
    public void modifyAlias_InvalidAlias_False() throws NoLocationRegisteredException,
            NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.setAliasToLocation("Mi casa", castellon.getId());

        // When
        boolean result = geoNewsManager.setAliasToLocation("Mi casa", castellon.getId());

        // Then
        assertFalse(result);
    }
}
