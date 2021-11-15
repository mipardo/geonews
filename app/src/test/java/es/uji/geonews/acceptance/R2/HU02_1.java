package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.managers.LocationManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.GeocodeService;
import es.uji.geonews.model.services.OpenWeatherService;
import es.uji.geonews.model.managers.ServiceManager;
import es.uji.geonews.model.services.ServiceName;

public class HU02_1 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        geoNewsManager = new GeoNewsManager();
    }

    @Test
    public void checkService_OneActivation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        // When
        boolean confirmation = geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // Then
        assertEquals(1, geoNewsManager.getServicesOfLocation(castellon.getId()).size());
        assertTrue( geoNewsManager.getServicesOfLocation(castellon.getId()).contains(ServiceName.OPEN_WEATHER));
        assertTrue(confirmation);

    }
    @Test
    public void checkService_OneActivation_alreadyActive_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // When
        boolean confirmation = geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);
        // Then
        assertEquals(1, geoNewsManager.getServicesOfLocation(id).size());
        assertTrue( geoNewsManager.getServicesOfLocation(id).contains(ServiceName.OPEN_WEATHER));
        assertFalse(confirmation);
    }


}
