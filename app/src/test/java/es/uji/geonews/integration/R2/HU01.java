package es.uji.geonews.integration.R2;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU01 {
    GeocodeService geocodeServiceMocked;
    OpenWeatherService openWeatherServiceMocked;
    ServiceManager serviceManager;
    LocationManager locationManager;

    @Before
    public void init() {
        geocodeServiceMocked = mock(GeocodeService.class);
        openWeatherServiceMocked = mock(OpenWeatherService.class);
        when(openWeatherServiceMocked.getServiceName()).thenReturn(ServiceName.OPEN_WEATHER);
        serviceManager = new ServiceManager();
        serviceManager.addService(openWeatherServiceMocked);
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void getData_ServiceNotAvailabe_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location alicante = locationManager.addLocation("Alicante");
        serviceManager.initLocationServices(alicante);
        Location valencia = locationManager.addLocation("Valencia");
        serviceManager.initLocationServices(valencia);
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        serviceManager.initLocationServices(castellon);
        serviceManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        when(openWeatherServiceMocked.getData(any(Location.class))).thenThrow(new ServiceNotAvailableException());
        // Act
        serviceManager.getData(ServiceName.OPEN_WEATHER, castellon);
    }
}