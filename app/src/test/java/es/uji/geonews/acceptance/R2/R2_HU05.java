package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU05 {
    private LocationManager locationManager;
    private Location valencia;

    @Before
    public void init(){
        GeocodeService geocode = new GeocodeService();
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
    }

    @Test
    public void getLocationData_thereAreLocations_Location()
            throws NotValidCoordinatesException, ServiceNotAvailableException,
            UnrecognizedPlaceNameException, NoLocationRegisteredException {
        // Given
        locationManager.addLocation("Castelló de la Plana");
        valencia = locationManager.addLocation("Valencia");
        locationManager.addLocation("Alicante");
        locationManager.setAliasToLocation("Casa", valencia.getId());

        // When
        Location result = locationManager.getLocation(valencia.getId());

        // Then
        assertEquals(result, valencia);
        assertEquals("Casa", result.getAlias());
    }

    @Test (expected = NoLocationRegisteredException.class)
    public void getLocationData_thereAreNoLocations_NoLocationRegisteredException() throws NoLocationRegisteredException {
        // When
        locationManager.getLocation(1);
    }
}
