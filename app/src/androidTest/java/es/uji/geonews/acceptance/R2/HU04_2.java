package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU04_2 {

    private GeoNewsManager geoNewsManager;
    private Context context;

    @Before
    public void init(){
        // Given
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(context);
    }

    @After
    public void clean() throws InterruptedException {
        AuxiliaryTestClass.cleanDB(geoNewsManager, context);
    }


    @Test
    public void removeFavoriteLocation_FavoriteLocation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        Location castellon = geoNewsManager.addLocation("39.99207, -0.03621");
        Location valencia = geoNewsManager.addLocation("39.50337, -0.40466");
        Location alicante = geoNewsManager.addLocation("38.53996, -0.50579");
        geoNewsManager.addToFavorites(castellon.getId());
        geoNewsManager.addToFavorites(valencia.getId());
        geoNewsManager.addToFavorites(alicante.getId());
        // When
        boolean removedCastellonFromFavorites = geoNewsManager.removeFromFavorites(castellon.getId());
        // Then
        assertEquals(2, geoNewsManager.getFavouriteLocations().size());
        assertTrue(removedCastellonFromFavorites);
    }

    @Test
    public void removeFavoriteLocation_NoFavoriteLocation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        Location castellon = geoNewsManager.addLocation("39.99207, -0.03621");
        Location valencia = geoNewsManager.addLocation("39.50337, -0.40466");
        // When
        boolean removedCastellonFromFavorites = geoNewsManager.removeFromFavorites(castellon.getId());
        // Then
        assertEquals(0, geoNewsManager.getFavouriteLocations().size());
        assertFalse(removedCastellonFromFavorites);
    }
}
