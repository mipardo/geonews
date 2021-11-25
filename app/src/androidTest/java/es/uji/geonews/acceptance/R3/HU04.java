package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU04 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void deactivateService_availableServices_true() {
        // Given
        geoNewsManager.addService(new OpenWeatherService());
        geoNewsManager.addService(new AirVisualService());
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.OPEN_WEATHER);
        // Then
        assertTrue(result);
        assertEquals(0, geoNewsManager.getActiveServices().size());
    }

    @Test
    public void deactivateService_nonAvailableServices_false() {
        // Given
        geoNewsManager.addService(new OpenWeatherService());
        geoNewsManager.addService(new AirVisualService());
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        // When
        boolean result = geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        // Then
        assertFalse(result);
        assertEquals(1, geoNewsManager.getActiveServices().size());
    }
}
