package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.OpenWeatherLocationData;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU03 {
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
    public void checkListActiveLocations_threeActiveLocations_()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location valencia =locationManager.addLocation("Valencia");
        Location alicante =locationManager.addLocation("Alicante");
        Location castellon = locationManager.addLocation("Castelló de la Plana");

        valencia.activate();
        alicante.activate();
        castellon.activate();

        // When
        activeList = locationManager.getActiveLocations();

        // Then
        assertEquals(3, activeList.size());
        assertTrue( activeList.contains(valencia));
        assertTrue( activeList.contains(alicante));
        assertTrue( activeList.contains(castellon));
    }
    @Test
    public void checkListActiveLocations_oneActiveLocations()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location alicante =locationManager.addLocation("Alicante");
        Location castellon = locationManager.addLocation("Castelló de la Plana");

        castellon.activate();

        // When
        activeList = locationManager.getActiveLocations();

        // Then
        assertEquals(1, activeList.size());
        assertFalse( activeList.contains(alicante));
        assertTrue( activeList.contains(castellon));
    }

    @Test
    public void checkListActiveLocations_noneActiveLocations()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        // When
        activeList = locationManager.getActiveLocations();

        // Then
        assertEquals(0, activeList.size());
        assertFalse( activeList.contains(castellon));

    }
    @Test(expected = NoLocationRegisteredException.class)
    public void checkListActiveLocations_noneLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // Given

        // When
        nonActiveList =locationManager.getNonActiveLocations();

        // Then



    }
}
