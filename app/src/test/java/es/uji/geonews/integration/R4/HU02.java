package es.uji.geonews.integration.R4;

import org.junit.Test;

import es.uji.geonews.model.exceptions.DatabaseNotAvailableException;

public class HU02 {

    @Test
    public void loadData_remoteDbAvailable_userDao() {

    }

    @Test(expected = DatabaseNotAvailableException.class)
    public void loadData_noDbAvailable_DataBaseNotAvailableException() {

    }
}
