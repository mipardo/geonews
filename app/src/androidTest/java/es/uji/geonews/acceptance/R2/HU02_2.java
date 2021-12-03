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
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.ServiceName;

public class HU02_2 {
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
    public void checkService_OneDesactivation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon.getId());

        // When
        boolean confirmation = geoNewsManager.removeServiceFromLocation(ServiceName.OPEN_WEATHER, castellon.getId());
        // Then
        assertEquals(0, geoNewsManager.getServicesOfLocation(castellon.getId()).size());
        assertFalse( geoNewsManager.getServicesOfLocation(castellon.getId()).contains(ServiceName.OPEN_WEATHER));
        assertTrue(confirmation);

    }
    @Test
    public void checkService_OneDesactivation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la plana");
        int id = castellon.getId();
        // When
        boolean confirmation = geoNewsManager.removeServiceFromLocation(ServiceName.OPEN_WEATHER, castellon.getId());
        // Then
        assertEquals(0, geoNewsManager.getServicesOfLocation(id).size());
        assertFalse( geoNewsManager.getServicesOfLocation(id).contains(ServiceName.OPEN_WEATHER));
        assertFalse(confirmation);
    }


}
