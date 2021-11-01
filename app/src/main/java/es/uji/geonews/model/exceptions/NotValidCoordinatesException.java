package es.uji.geonews.model.exceptions;

public class NotValidCoordinatesException extends Exception {
    public NotValidCoordinatesException(){
        super("Las coordenadas introducidas no son validas.");
    }
}
