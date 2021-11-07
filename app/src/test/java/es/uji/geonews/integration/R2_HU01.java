package es.uji.geonews.integration;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.OpenWeatherLocationData;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU01 {
    CoordsSearchService coordsSearchServiceMocked;
    ServiceManager serviceManagerMocked;
    LocationManager locationManager;

    @Before
    public void init() {
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        locationManager = new LocationManager(serviceManagerMocked);
    }

    @Test (expected = ServiceNotAvailableException.class)
    public void registerLocationByPlaceName_knownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.getCoordsFrom("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(coordsSearchServiceMocked.getCoordsFrom("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        locationManager.addLocation("Alicante");
        locationManager.addLocation("Valencia");
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        OpenWeatherService mockedOpenWeatherService = mock(OpenWeatherService.class);
        when(mockedOpenWeatherService.getDataFrom(any(Location.class))).thenThrow(new ServiceNotAvailableException());
        when(serviceManagerMocked.getService("OpenWeather")).thenReturn(mockedOpenWeatherService);
        locationManager.addLocationService("OpenWeather", castellon.getId());
        // Act
        locationManager.getServiceData("OpenWeather", castellon.getId());
    }
}
