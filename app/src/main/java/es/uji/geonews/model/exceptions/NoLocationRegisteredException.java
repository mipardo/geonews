package es.uji.geonews.model.exceptions;

public class NoLocationRegisteredException extends Exception {
    public NoLocationRegisteredException() {
        super("No existe ninguna ubicacion dada de alta");
    }
}
