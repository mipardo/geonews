package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU05 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private Location location;


    @Before
    public void init(){
        serviceManager = new ServiceManager();
        GeocodeService geocode = new GeocodeService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
        location = new Location(2, null,
                new GeographCoords(39.97990, -0.03304), LocalDate.now());
    }

    @Test
    public void validateLocation_PlaceNameRecognized_ListWithTwoActiveServices() {
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        locationManager.addLocation(location);
        // When
        List<ServiceName> services = serviceManager.validateLocation(location);
        // Then
        assertEquals(2, services.size());
        assertTrue(services.contains(ServiceName.OPEN_WEATHER));
        assertTrue(services.contains(ServiceName.AIR_VISUAL));
    }

    @Test
    public void validateLocation_NoApiAvailable_EmptyList() {
        locationManager.addLocation(location);
        // When
        List<ServiceName> services = serviceManager.validateLocation(location);
        // Then
        assertEquals(0, services.size());
    }
}
