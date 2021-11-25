package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {
    GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void getAvailableServices_areAvailableServices_ListWithTwoServices() {
        // Given
        geoNewsManager.deactivateService(ServiceName.CURRENTS);
        geoNewsManager.deactivateService(ServiceName.GEOCODE);
        // When
        List<ServiceName> services = geoNewsManager.getAvailableServices();
        // Then
        assertEquals(2, services.size());
        assertTrue(services.contains(ServiceName.OPEN_WEATHER));
        assertTrue(services.contains(ServiceName.AIR_VISUAL));
    }

    @Test
    public void getAvailableServices_noAvailableServices_EmtpyList() {
        // Given
        // When
        List<ServiceName> services = geoNewsManager.getAvailableServices();

        // Then
        assertEquals(0, services.size());
    }
}