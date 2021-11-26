package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.acceptance.AuxiliaryTestClass;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {
    ServiceManager serviceManager;
    GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        LocationManager locationManager = new LocationManager(new GeocodeService());
        serviceManager = new ServiceManager();
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, null, null);
    }

    @Test
    public void getPublicServices_areAvailableServices_ListWithTwoServices() {
        //Given
        serviceManager.addService(new CurrentsService());
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        // When
        List<ServiceName> services = geoNewsManager.getPublicServices();
        // Then
        assertEquals(3, services.size());
        assertTrue(services.contains(ServiceName.CURRENTS));
        assertTrue(services.contains(ServiceName.OPEN_WEATHER));
        assertTrue(services.contains(ServiceName.AIR_VISUAL));
    }

    @Test
    public void getPublicServices_noAvailableServices_EmtpyList() {
        // When
        List<ServiceName> services = geoNewsManager.getPublicServices();

        // Then
        assertEquals(0, services.size());
    }
}