package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.services.ServiceName;

public class HU02 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void activateServices_deActiveAndAvailable_True(){
        // Given
        geoNewsManager.deactivateService(ServiceName.AIR_VISUAL);
        // When
        boolean result = geoNewsManager.activateService(ServiceName.AIR_VISUAL);

        // Then
        assertTrue(result);
        assertTrue(geoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }

    @Test
    public void activateServices_activeAndAvailable_False() {
        // Given
        // When
        boolean result = geoNewsManager.activateService(ServiceName.AIR_VISUAL);

        // Then
        assertFalse(result);
        assertTrue(geoNewsManager.getService(ServiceName.AIR_VISUAL).isActive());
    }

}
