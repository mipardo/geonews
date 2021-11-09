package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.LocationManager;
import es.uji.geonews.model.exceptions.NoLocationRegisteredException;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.AirVisualService;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU03 {
    private CoordsSearchService coordsSearchServiceMocked;
    private ArrayList<ServiceHttp> services;
    private ServiceManager serviceManagerSpied;
    private LocationManager locationManager;
    private AirVisualService airVisualServiceMocked;


    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        airVisualServiceMocked = mock(AirVisualService.class);
        when(airVisualServiceMocked.getServiceName()).thenReturn("AirVisual");
        when(airVisualServiceMocked.validateLocation(any())).thenReturn(true);
        ServiceManager serviceManager = new ServiceManager();
        serviceManagerSpied = spy(serviceManager);
        doReturn(coordsSearchServiceMocked).when(serviceManagerSpied).getService("Geocode");
        locationManager = new LocationManager(serviceManagerSpied);
        services = new ArrayList<>();
    }

    @Test
    public void checkListActiveLocations_threeActiveLocations()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.getCoords("Valencia")).thenReturn(new GeographCoords(39.50337, -0.40466));
        when(coordsSearchServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
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

        // Assert


    }
    @Test
    public void checkListActiveLocations_oneActiveLocations_OneLocationList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        when(coordsSearchServiceMocked.getCoords("Alicante")).thenReturn(new GeographCoords(38.53996, -0.50579));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        Location alicante = locationManager.addLocation("Alicante");
        serviceManagerSpied.addService(airVisualServiceMocked);
        locationManager.activateLocation(castellon.getId());

        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(1, activeLocations.size());

        // Assert


    }
    @Test
    public void checkListActiveLocations_noneActiveLocations_EmptyList()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoords("Castelló de la Plana")).thenReturn(new GeographCoords(39.98920, -0.03621));
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        // Act
        List<Location> activeLocations = locationManager.getActiveLocations();
        // Assert
        assertEquals(0, activeLocations.size());

        // Assert


    }
    @Test(expected = NoLocationRegisteredException.class)
    public void checkListActiveLocations_noneLocations_NoLocationRegisteredException()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException, NoLocationRegisteredException {
        // Arrange
        // Act
        List<Location> activeLocations = locationManager.getNonActiveLocations();
        // Assert
;

        // Assert


    }
}
