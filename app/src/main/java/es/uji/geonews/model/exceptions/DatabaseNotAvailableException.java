package es.uji.geonews.model.exceptions;

public class DatabaseNotAvailableException extends Exception {
    public DatabaseNotAvailableException() {
        super("Base de datos no disponible");
    }
}
