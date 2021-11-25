package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU03 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        geoNewsManager = new GeoNewsManager(appContext);
    }

    @Test
    public void getServicesDescription_availableServices_servicesDescriptionMap()
            throws ServiceNotAvailableException {
        // Given
        geoNewsManager.addService(new OpenWeatherService());
        geoNewsManager.addService(new AirVisualService());

        // When
        Map<String, String> availableServices = geoNewsManager.getServices();
        // Then
        assertEquals(2, availableServices.size());
        assertTrue(availableServices.containsKey(ServiceName.OPEN_WEATHER.name));
        assertTrue(availableServices.containsKey(ServiceName.AIR_VISUAL.name));
        assertTrue(availableServices.get(ServiceName.OPEN_WEATHER.name).length() > 0);
        assertTrue(availableServices.get(ServiceName.AIR_VISUAL.name).length() > 0);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getServicesDescription_nonAvailableServices_ServiceNotAvailableException()
            throws ServiceNotAvailableException {
        // When
        geoNewsManager.getServices();
    }
}
