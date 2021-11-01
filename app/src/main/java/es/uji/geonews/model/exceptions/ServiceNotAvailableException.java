package es.uji.geonews.model.exceptions;

public class ServiceNotAvailableException extends Exception {
    public ServiceNotAvailableException() {
        super("No se ha podido conectar con el servicio.");
    }
}
