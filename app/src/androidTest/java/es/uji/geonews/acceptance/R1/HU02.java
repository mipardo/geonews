package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.managers.ServiceManager;

public class HU02 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);

    }

    @Test
    public void registerLocationByCoords_KnownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException
            , UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        GeographCoords coords = new GeographCoords(39.98920, -0.03621);
        // When
        Location newLocation = geoNewsManager.addLocation(coords.toString());
        // Then
        assertEquals(1, geoNewsManager.getNonActiveLocations().size());
        assertEquals("Castelló de la plana", newLocation.getPlaceName());
    }
    @Test
    public void registerLocationByCoords_UnknownPlaceName_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        //Given
        geoNewsManager.addLocation("Castello de la plana");
        GeographCoords coords = new GeographCoords(33.65001, -41.19001);
        // When
        Location newLocation = geoNewsManager.addLocation(coords.toString());
        // Then
        assertEquals(2, geoNewsManager.getNonActiveLocations().size());
        assertEquals(33.65, newLocation.getGeographCoords().getLatitude(), 0.01);
        assertEquals(-41.19, newLocation.getGeographCoords().getLongitude(),0.01);
        assertNull(newLocation.getPlaceName());
    }

    @Test(expected = NotValidCoordinatesException.class)
    public void registerLocationByCoords_InvalidCoords_NotValidCoordinatesException()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException {
        //Given
        geoNewsManager.addLocation("Castello de la plana");
        geoNewsManager.addLocation("Valencia");
        GeographCoords coords = new GeographCoords(100.00001, -41.19001);
        // When
        geoNewsManager.addLocation(coords.toString());
        // Then
    }
}
