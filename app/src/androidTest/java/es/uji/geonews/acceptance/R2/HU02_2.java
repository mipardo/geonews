package es.uji.geonews.acceptance.R2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.managers.GeoNewsManager;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.services.ServiceName;

public class HU02_2 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        geoNewsManager = new GeoNewsManager();
    }

    @Test
    public void checkService_OneDesactivation_True()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        geoNewsManager.addServiceToLocation(ServiceName.OPEN_WEATHER, castellon);

        // When
        boolean confirmation = geoNewsManager.removeServiceFromLocation(ServiceName.OPEN_WEATHER, castellon);
        // Then
        assertEquals(0, geoNewsManager.getServicesOfLocation(id).size());
        assertFalse( geoNewsManager.getServicesOfLocation(id).contains(ServiceName.OPEN_WEATHER));
        assertTrue(confirmation);

    }
    @Test
    public void checkService_OneDesactivation_False()
            throws NotValidCoordinatesException, ServiceNotAvailableException, UnrecognizedPlaceNameException {
        // Given
        Location castellon = geoNewsManager.addLocation("Castelló de la Plana");
        int id = castellon.getId();
        // When
        boolean confirmation = geoNewsManager.removeServiceFromLocation(ServiceName.OPEN_WEATHER, castellon);
        // Then
        assertEquals(0, geoNewsManager.getServicesOfLocation(id).size());
        assertFalse( geoNewsManager.getServicesOfLocation(id).contains(ServiceName.OPEN_WEATHER));
        assertFalse(confirmation);
    }


}
