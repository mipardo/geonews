package es.uji.geonews.integration.R4;

import org.junit.Test;

import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;

public class HU01 {



    @Test(expected = DatabaseNotAvailableException.class)
    public void loadRemoteState_dataBaseNotAvailable_DatabaseNotAvailableException() {

    }
}
