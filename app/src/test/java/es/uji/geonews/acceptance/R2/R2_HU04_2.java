package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU04_2 {

    private LocationManager locationManager;

    @Before
    public void init(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        locationManager = new LocationManager(serviceManager);
    }

    @Test
    public void removeFavoriteLocation_FavoriteLocation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = locationManager.addLocation("39.99207, -0.03621");
        Location valencia = locationManager.addLocation("39.50337, -0.40466");
        Location alicante = locationManager.addLocation("38.53996, -0.50579");
        locationManager.addToFavorites(castellon.getId());
        locationManager.addToFavorites(valencia.getId());
        locationManager.addToFavorites(alicante.getId());
        // When
        boolean removedCastellonFromFavorites = locationManager.removeFromFavorites(castellon.getId());
        // Then
        assertEquals(2, locationManager.getFavouriteLocations().size());
        assertTrue(removedCastellonFromFavorites);
    }

    @Test
    public void removeFavoriteLocation_NoFavoriteLocation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        Location castellon = locationManager.addLocation("39.99207, -0.03621");
        Location valencia = locationManager.addLocation("39.50337, -0.40466");
        // When
        boolean removedCastellonFromFavorites = locationManager.removeFromFavorites(castellon.getId());
        // Then
        assertEquals(0, locationManager.getFavouriteLocations().size());
        assertFalse(removedCastellonFromFavorites);
    }
}
