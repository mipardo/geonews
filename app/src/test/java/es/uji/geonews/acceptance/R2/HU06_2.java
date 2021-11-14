package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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

public class HU06_2 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private GeocodeService geocode;

    @Before
    public void init(){
        geocode = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
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
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        serviceManager.initLocationServices(castellon);
        locationManager.activateLocation(castellon.getId());

        Location valencia = locationManager.addLocation("Valencia");
        serviceManager.initLocationServices(valencia);
        locationManager.activateLocation(valencia.getId());
        locationManager.deactivateLocation(valencia.getId());

        // When
        boolean result = locationManager.activateLocation(valencia.getId());

        // Then
        assertFalse(result);
        assertEquals(1, locationManager.getActiveLocations().size());
    }
}
