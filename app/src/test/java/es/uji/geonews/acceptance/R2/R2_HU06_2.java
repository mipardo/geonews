package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU06_2 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;

    @Before
    public void init(){
        Service coordsSearchSrv = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
    }

    @Test
    public void reactivateLocation_availableServices_ServiceNameList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        locationManager = new LocationManager(serviceManager);
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
        serviceManager.addService(new OpenWeatherService());
        serviceManager.addService(new AirVisualService());
        locationManager = new LocationManager(serviceManager);
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        locationManager.activateLocation(castellon.getId());

        locationManager.deactivateService("OpenWeather");
        locationManager.deactivateService("AirVisual");
        Location valencia = locationManager.addLocation("Valencia");

        locationManager.activateLocation(valencia.getId());
        locationManager.deactivateLocation(valencia.getId());

        // When
        boolean result = locationManager.activateLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, locationManager.getActiveLocations().size());
    }
}
