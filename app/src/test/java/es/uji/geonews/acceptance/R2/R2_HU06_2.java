package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;

public class R2_HU06_2 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private GeocodeService geocode;

    @Before
    public void init(){
        geocode = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
    }

    @Test
    public void reactivateLocation_availableServices_ServiceNameList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        locationManager = new LocationManager(geocode);
        Location valencia = locationManager.addLocation("Valencia");
        locationManager.activateLocation(valencia.getId());
        locationManager.deactivateLocation(valencia.getId());

        // When
        boolean result = locationManager.activateLocation(valencia.getId());

        // Then
        assertTrue(result);
    }

    @Test
    public void reactivateLocation_anyServices_EmptyServiceNameList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        GeoNewsManager manager = new GeoNewsManager();
        manager.addService(new OpenWeatherService());
        manager.addService(new AirVisualService());
        Location castellon = manager.addLocation("Castelló de la Plana");
        manager.activateLocation(castellon.getId());

        Location valencia = manager.addLocation("Valencia");
        manager.activateLocation(valencia.getId());
        manager.deactivateLocation(valencia.getId());

        manager.deactivateService("OpenWeather");
        manager.deactivateService("AirVisual");


        // When
        boolean result = manager.activateLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, manager.getActiveLocations().size());
    }
}
