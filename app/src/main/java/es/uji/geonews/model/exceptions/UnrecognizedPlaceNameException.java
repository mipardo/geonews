package es.uji.geonews.model.exceptions;

public class UnrecognizedPlaceNameException extends Exception {
    public UnrecognizedPlaceNameException(){
        super("El topónimo introducido no se reconoce");
    }
}
