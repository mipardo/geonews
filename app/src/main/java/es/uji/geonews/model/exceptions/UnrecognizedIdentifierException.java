package es.uji.geonews.model.exceptions;

public class UnrecognizedIdentifierException extends Exception {
    public UnrecognizedIdentifierException() {
        super("El identificador introducido no se ha reconocido");
    }
}
