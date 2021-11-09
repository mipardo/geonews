package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU01 {
    CoordsSearchService coordsSearchServiceMocked;
    ServiceManager serviceManagerSpied;
    LocationManager locationManager;

    @Before
    public void init() {
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        ServiceManager serviceManager = new ServiceManager();
        serviceManagerSpied = spy(serviceManager);
        doReturn(coordsSearchServiceMocked).when(serviceManagerSpied).getService("Geocode");
        locationManager = new LocationManager(serviceManagerSpied);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void checkServiceData_notAvailabe_ServiceNotAvailableException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(coordsSearchServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        locationManager.addLocation("Alicante");
        locationManager.addLocation("Valencia");
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        OpenWeatherService mockedOpenWeatherService = mock(OpenWeatherService.class);
        when(mockedOpenWeatherService.getData(any(Location.class))).thenThrow(new ServiceNotAvailableException());
        doReturn(mockedOpenWeatherService).when(serviceManagerSpied).getService("OpenWeather");
        locationManager.addServiceToLocation("OpenWeather", castellon.getId());
        // Act
        locationManager.getData("OpenWeather", castellon.getId());
    }
}
