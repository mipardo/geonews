package es.uji.geonews.acceptance.R2;

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
import es.uji.geonews.model.managers.ServiceManager;

public class HU04_3 {

    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        GeocodeService geocode = new GeocodeService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
    }

    @Test
    public void addFavoriteLocation_NoFavoriteLocation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = locationManager.addLocation("39.99207, -0.03621");
        Location valencia = locationManager.addLocation("39.50337, -0.40466");
        Location alicante = locationManager.addLocation("38.53996, -0.50579");
        locationManager.addToFavorites(valencia.getId());
        locationManager.addToFavorites(alicante.getId());

        // When
        boolean castellonToFavorites = locationManager.addToFavorites(castellon.getId());

        // Then
        assertEquals(3, locationManager.getFavouriteLocations().size());
        assertTrue(castellonToFavorites);
    }

    @Test
    public void addFavoriteLocation_FavoriteLocation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = locationManager.addLocation("39.99207, -0.03621");
        Location valencia = locationManager.addLocation("38.53996, -0.50579");
        locationManager.addToFavorites(valencia.getId());
        locationManager.addToFavorites(castellon.getId());
        // When
        boolean castellonToFavorites = locationManager.addToFavorites(castellon.getId());
        // Then
        assertFalse(castellonToFavorites);
        assertEquals(2, locationManager.getFavouriteLocations().size());
    }

}
