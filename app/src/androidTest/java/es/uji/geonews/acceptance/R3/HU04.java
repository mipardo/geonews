package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU04 {
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
    public void deactivateService_availableServices_true() {
        // Given
        geoNewsManager.deactivateService(ServiceName.CURRENTS);
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        geoNewsManager.deactivateService(ServiceName.GEOCODE);
        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        // Then
        assertTrue(result);
        assertEquals(0, geoNewsManager.getActiveServices().size());
    }

    @Test
    public void deactivateService_nonAvailableServices_false() {
        // Given
        geoNewsManager.deactivateService(ServiceName.CURRENTS);
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        geoNewsManager.deactivateService(ServiceName.GEOCODE);
        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        // Then
        assertFalse(result);
        assertEquals(1, geoNewsManager.getActiveServices().size());
    }
}
