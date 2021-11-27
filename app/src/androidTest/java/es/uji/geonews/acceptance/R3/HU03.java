package es.uji.geonews.acceptance.R3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU03 {
    ServiceManager serviceManager;
    GeoNewsManager geoNewsManager;

    @Before
    public void init() {
        // Given
        LocationManager locationManager = new LocationManager(new GeocodeService());
        serviceManager = new ServiceManager();
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, null, null);
    }

    @Test
    public void getServicesDescription_availableServices_servicesDescriptionMap()
            throws ServiceNotAvailableException {
        //Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        // When
        Map<String, String> servicesDescription = geoNewsManager.getServicesDescription();
        // Then
        assertEquals(2, servicesDescription.size());
        assertTrue(servicesDescription.containsKey(ServiceName.OPEN_WEATHER.name));
        assertTrue(servicesDescription.containsKey(ServiceName.AIR_VISUAL.name));
        assertTrue(servicesDescription.get(ServiceName.OPEN_WEATHER.name).length() > 0);
        assertTrue(servicesDescription.get(ServiceName.AIR_VISUAL.name).length() > 0);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getServicesDescription_nonAvailableServices_ServiceNotAvailableException()
            throws ServiceNotAvailableException {
        // When
        geoNewsManager.getServicesDescription();
    }
}
