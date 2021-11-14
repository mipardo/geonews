package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class R2_HU02_2 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;

    @Before
    public void init(){
        // Given
        GeocodeService geocode = new GeocodeService();
        serviceManager = new ServiceManager();
        serviceManager.addService(geocode);
        OpenWeatherService openWeatherService = new OpenWeatherService();
        serviceManager.addService(openWeatherService);
        locationManager = new LocationManager(geocode);
    }

    @Test
    public void checkService_OneDesactivation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        serviceManager.initLocationServices(castellon);
        int id = castellon.getId();
        serviceManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // When
        boolean confirmation = serviceManager.removeServiceFromLocation(ServiceName.OPEN_WEATHER, castellon);
        // Then
        assertEquals(0, serviceManager.getServicesOfLocation(id).size());
        assertFalse( serviceManager.getServicesOfLocation(id).contains(ServiceName.OPEN_WEATHER));
        assertTrue(confirmation);

    }
    @Test
    public void checkService_OneDesactivation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = locationManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        serviceManager.initLocationServices(castellon);
        // When
        boolean confirmation = serviceManager.removeServiceFromLocation(ServiceName.OPEN_WEATHER, castellon);
        // Then
        assertEquals(0, serviceManager.getServicesOfLocation(id).size());
        assertFalse( serviceManager.getServicesOfLocation(id).contains(ServiceName.OPEN_WEATHER));
        assertFalse(confirmation);
    }


}
