package es.uji.geonews.acceptance.R1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import es.uji.geonews.model.GeographCoords;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.CurrentsService;
import es.uji.geonews.model.services.Service;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU04 {
    private LocationManager locationManager;
    private ServiceManager serviceManager;
    private Location valencia;

    @Before
    public void init(){
        // Given
        serviceManager = new ServiceManager();
        GeocodeService geocode = new GeocodeService();
        serviceManager.addService(geocode);
        locationManager = new LocationManager(geocode);
        valencia = new Location(2, "Valencia",
                new GeographCoords(39.50337, -0.40466), LocalDate.now());

    }

    @Test
    public void validatePlaceName_PlaceNameRecognized_ListWithOneServiceActive() {
        // Given
        serviceManager.addService(new CurrentsService());
        locationManager.addLocation(valencia);
        // When
        List<ServiceName> services = serviceManager.validateLocation(valencia);

        // Then
        assertEquals(1, services.size());
        assertTrue(services.contains(ServiceName.CURRENTS));
    }

    @Test
    public void validatePlaceName_NoApiAvailable_EmptyList() {
        // When
        locationManager.addLocation(valencia);
        List<ServiceName> services = serviceManager.validateLocation(valencia);
        // Then
        assertEquals(0, services.size());
    }

}
