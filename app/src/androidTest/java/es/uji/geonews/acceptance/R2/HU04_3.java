package es.uji.geonews.acceptance.R2;

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

public class HU04_3 {

    private GeoNewsManager geoNewsManager;

    @Before
    public void init() {
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void addFavoriteLocation_NoFavoriteLocation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = geoNewsManager.addLocation("39.99207, -0.03621");
        Location valencia = geoNewsManager.addLocation("39.50337, -0.40466");
        Location alicante = geoNewsManager.addLocation("38.53996, -0.50579");
        geoNewsManager.addToFavorites(valencia.getId());
        geoNewsManager.addToFavorites(alicante.getId());

        // When
        boolean castellonToFavorites = geoNewsManager.addToFavorites(castellon.getId());

        // Then
        assertEquals(3, geoNewsManager.getFavouriteLocations().size());
        assertTrue(castellonToFavorites);
    }

    @Test
    public void addFavoriteLocation_FavoriteLocation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = geoNewsManager.addLocation("39.99207, -0.03621");
        Location valencia = geoNewsManager.addLocation("38.53996, -0.50579");
        geoNewsManager.addToFavorites(valencia.getId());
        geoNewsManager.addToFavorites(castellon.getId());
        // When
        boolean castellonToFavorites = geoNewsManager.addToFavorites(castellon.getId());
        // Then
        assertFalse(castellonToFavorites);
        assertEquals(2, geoNewsManager.getFavouriteLocations().size());
    }

}
