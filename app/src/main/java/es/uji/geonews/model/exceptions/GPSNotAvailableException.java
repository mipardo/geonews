package es.uji.geonews.model.exceptions;

public class GPSNotAvailableException extends Exception {
    public GPSNotAvailableException() {
        super("El servicio GPS no se encuentra disponible en este momento.");
    }
}
