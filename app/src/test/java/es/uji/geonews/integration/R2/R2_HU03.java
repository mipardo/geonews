package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU03 {
    private GeocodeService geocodeServiceMocked;
    private ServiceManager serviceManagerSpied;
    private LocationManager locationManager;
    private AirVisualService airVisualServiceMocked;


    @Before
    public void init(){
        geocodeServiceMocked = mock(GeocodeService.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        when(airVisualServiceMocked.getServiceName()).thenReturn("AirVisual");
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        ServiceManager serviceManager = new ServiceManager();
        serviceManagerSpied = spy(serviceManager);
        doReturn(geocodeServiceMocked).when(serviceManagerSpied).getService("Geocode");
        locationManager = new LocationManager(geocodeServiceMocked);
    }

    @Test
    public void checkListActiveLocations_threeActiveLocations()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location valencia = locationManager.addLocation("Valencia");
        Location alicante = locationManager.addLocation("Alicante");
        serviceManagerSpied.addService(airVisualServiceMocked);
        locationManager.activateLocation(castellon.getId());
        locationManager.activateLocation(valencia.getId());
        locationManager.activateLocation(alicante.getId());
        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(3, activeLocations.size());
    }
    @Test
    public void checkListActiveLocations_oneActiveLocations_OneLocationList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(geocodeServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location alicante = locationManager.addLocation("Alicante");
        serviceManagerSpied.addService(airVisualServiceMocked);
        locationManager.activateLocation(castellon.getId());

        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(1, activeLocations.size());
    }

    @Test
    public void checkListActiveLocations_noneActiveLocations_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(geocodeServiceMocked.isAvailable()).thenReturn(true);
        when(geocodeServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(0, activeLocations.size());
    }

    @Test(expected = NoLocationRegisteredException.class)
    public void checkListActiveLocations_noneLocations_NoLocationRegisteredException()
            throws NoLocationRegisteredException {
        // Arrange
        // Act
        List<Location> activeLocations = locationManager.getNonActiveLocations();
    }
}
