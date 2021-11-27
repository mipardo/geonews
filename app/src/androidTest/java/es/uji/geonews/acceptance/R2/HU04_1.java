package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;

public class HU04_1 {

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
    public void getFavoriteLocations_AllAreFavorite_ListWithThreeLocations()
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
        List<Location> favouriteLocations = geoNewsManager.getFavouriteLocations();
        // Then
        assertEquals(3, favouriteLocations.size());
    }

    @Test
    public void getFavoriteLocations_SomeAreFavorite_ListWithOneLocation()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        Location castellon = geoNewsManager.addLocation("39.99207, -0.03621");
        Location alicante = geoNewsManager.addLocation("38.53996, -0.50579");
        geoNewsManager.addToFavorites(castellon.getId());
        // When
        List<Location> favouriteLocations = geoNewsManager.getFavouriteLocations();
        // Then
        assertEquals(1, favouriteLocations.size());
        assertEquals(castellon.getPlaceName(), favouriteLocations.get(0).getPlaceName());
    }

    @Test
    public void getFavoriteLocations_AnyAreFavorite_EmtpyList()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        geoNewsManager.addLocation("39.99207, -0.03621");
        // When
        List<Location> favouriteLocations = geoNewsManager.getFavouriteLocations();
        // Then
        assertEquals(0, favouriteLocations.size());
    }

    @Test
    public void getFavoriteLocations_NoLocations_NoLocationRegisteredException() {
        // When
        List<Location> favouriteLocations =  geoNewsManager.getFavouriteLocations();

        // Then
        assertEquals(0, favouriteLocations.size());
    }
}
