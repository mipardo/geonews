package es.uji.geonews.integration.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
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
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.CoordsSearchService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.ServiceHttp;
import es.uji.geonews.model.services.ServiceManager;

public class R2_HU02_1 {
    private CoordsSearchService coordsSearchServiceMocked;
    private CurrentsService currentsServiceMocked;
    private ArrayList<ServiceHttp> services;
    private ServiceManager serviceManagerMocked;
    //private LocationManager locationManager;
    private LocationManager locationManagerMocked;

    @Before
    public void init(){
        coordsSearchServiceMocked = mock(CoordsSearchService.class);
        currentsServiceMocked = mock(CurrentsService.class);
        serviceManagerMocked = mock(ServiceManager.class);
        when(serviceManagerMocked.getService("Geocode")).thenReturn(coordsSearchServiceMocked);
        //locationManager = new LocationManager(serviceManagerMocked);
        locationManagerMocked = mock(LocationManager.class);
        services = new ArrayList<>();
    }

    @Test
    public void checkListActiveLocations_threeActiveLocations()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // Arrange
        when(coordsSearchServiceMocked.isAvailable()).thenReturn(true);
        when(coordsSearchServiceMocked.getCoordsFrom(any()))
                .thenReturn(new GeographCoords(39.46975, -0.3739));
        services.add(currentsServiceMocked);
        when(serviceManagerMocked.getHttpServices()).thenReturn(services);
        when(currentsServiceMocked.getServiceName()).thenReturn("Currents");
        when(currentsServiceMocked.validateLocation(any())).thenReturn(true);


        // Act


        //Location location = locationManager.addLocation("Valencia");
        //location.activate();
        //boolean confirmation = locationManager.addLocationService("OpenWeather", location.getId());

        // Assert
        verify(serviceManagerMocked, times(1)).getHttpServices();
        verify(currentsServiceMocked, times(2)).getServiceName();
        verify(currentsServiceMocked, times(1)).validateLocation(any());


    }
}
