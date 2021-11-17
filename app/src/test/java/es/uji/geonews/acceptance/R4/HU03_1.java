package es.uji.geonews.acceptance.R4;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.exceptions.NotValidCoordinatesException;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import es.uji.geonews.model.exceptions.UnrecognizedPlaceNameException;
import es.uji.geonews.model.managers.GeoNewsManager;

public class HU03_1 {
    private GeoNewsManager geoNewsManager;

    @Before
    public void init(){
        // Given
        geoNewsManager = new GeoNewsManager();
    }

    @Test
    public void registerLocationByPlaceName_KnownPlaceName_Location()
            throws UnrecognizedPlaceNameException, ServiceNotAvailableException,
            NotValidCoordinatesException {
        // When
        Location newLocation = geoNewsManager.addLocation("Castello de la Plana");
    }

}
