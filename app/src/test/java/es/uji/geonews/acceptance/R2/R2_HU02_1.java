package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU02_1 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private List<Location> activeList;
    private List<Location> nonActiveList;

    @Before
    public void init(){
        // Given
        Service coordsSearchSrv = new CoordsSearchService();
        serviceManager = new ServiceManager();
        serviceManager.addService(coordsSearchSrv);
        OpenWeatherService openWeatherService = new OpenWeatherService();
        serviceManager.addService(openWeatherService);
        locationManager = new LocationManager(serviceManager);
        activeList = new ArrayList<Location>();
    }

    @Test
    public void checkService_OneActivation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        // When
        boolean confirmation = locationManager.addLocationService("OpenWeather", id);
        // Then
        assertEquals(1, locationManager.getLocationService(id).size());
        assertTrue( locationManager.getLocationService(id).contains("OpenWeather"));
        assertTrue(confirmation);

    }
    @Test
    public void checkService_OneActivation_alreadyActive_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        locationManager.addLocationService("OpenWeather", id);
        // When
        boolean confirmation = locationManager.addLocationService("OpenWeather", id);
        // Then
        assertEquals(1, locationManager.getLocationService(id).size());
        assertTrue( locationManager.getLocationService(id).contains("OpenWeather"));
        assertFalse(confirmation);
    }


}
