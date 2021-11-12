package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU06_1 {
    private LocationManager locationManager;

    @Before
    public void init(){
        GeocodeService geocode = new GeocodeService();
        Service airVisualService = new AirVisualService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        serviceManager.addService(airVisualService);
        locationManager = new LocationManager(geocode);
    }

    @Test
    public void getNonActiveLocations_allLocationNotActive_LocationsList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        locationManager.addLocation("Castelló de la Plana");
        locationManager.addLocation("Valencia");
        locationManager.addLocation("Alicante");

        // When
        List<Location> locationList = locationManager.getNonActiveLocations();

        // Then
        assertEquals(3, locationList.size());
    }

    @Test
    public void getNonActiveLocations_oneLocationNotActive_LocationsList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        locationManager.addLocation("Valencia");
        locationManager.activateLocation(castellon.getId());

        // When
        List<Location> locationList = locationManager.getNonActiveLocations();

        // Then
        assertEquals(1, locationList.size());
    }

    @Test
    public void getNonActiveLocations_AnyLocationNotActive_LocationsList()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location valencia = locationManager.addLocation("Valencia");
        locationManager.activateLocation(castellon.getId());
        locationManager.activateLocation(valencia.getId());

        // When
        List<Location> locationList = locationManager.getNonActiveLocations();

        // Then
        assertEquals(0, locationList.size());
    }

    @Test (expected = NoLocationRegisteredException.class)
    public void getNonActiveLocations_anyLocationRegistered_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // When
        locationManager.getNonActiveLocations();
    }
}
