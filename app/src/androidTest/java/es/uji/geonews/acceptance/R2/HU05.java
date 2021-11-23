package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;

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

public class HU05 {
    private Location valencia;
    private GeoNewsManager geoNewsManager;

    @Before
    public void init() {
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void getLocationData_thereAreLocations_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        geoNewsManager.addLocation("Castelló de la plana");
        valencia = geoNewsManager.addLocation("Valencia");
        geoNewsManager.addLocation("Alicante");
        geoNewsManager.setAliasToLocation("Casa", valencia.getId());

        // When
        Location result = geoNewsManager.getLocation(valencia.getId());

        // Then
        assertEquals(result, valencia);
        assertEquals("Casa", result.getAlias());
    }

    @Test (expected = NoLocationRegisteredException.class)
    public void getLocationData_thereAreNoLocations_NoLocationRegisteredException() throws NoLocationRegisteredException {
        // When
        geoNewsManager.getLocation(1);
    }
}
