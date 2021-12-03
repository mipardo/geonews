package es.uji.geonews.integration.R2;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.database.DatabaseManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceName;

public class HU05_2 {
    GeocodeService geocodeServiceMocked;
    OpenWeatherService openWeatherServiceMocked;
    GeoNewsManager geoNewsManager;

    @Before
    public void init() {
        DatabaseManager databaseManagerMocked = mock(DatabaseManager.class);
        geocodeServiceMocked = mock(GeocodeService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);
        ServiceManager serviceManager = new ServiceManager();
        serviceManager.addService(openWeatherServiceMocked);
        LocationManager locationManager = new LocationManager(geocodeServiceMocked);
        geoNewsManager = new GeoNewsManager(locationManager, serviceManager, databaseManagerMocked, null);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getWeatherData_ServiceNotAvailabe_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        geoNewsManager.addLocation("Alicante");
        geoNewsManager.addLocation("Valencia");
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon.getId());
        when(openWeatherServiceMocked.getData(any(Location.class))).thenThrow(new ServiceNotAvailableException());
        // Act
        geoNewsManager.getData(ServiceName.OPEN_WEATHER, castellon.getId());
    }
}
