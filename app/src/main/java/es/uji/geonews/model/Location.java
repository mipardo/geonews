package es.uji.geonews.model;
import java.time.LocalDate;

public class Location {
    private int id;
    private String alias;
    private String placeName;
    private GeographCoords geographCoords;
    private String registrationDate;
    private boolean active;
    private boolean favourite;

    public Location() {}

    public Location(int id, String placeName, GeographCoords geographCoords, LocalDate registrationDate ) {
        this.id = id;
        this.placeName = normalizePlaceName(placeName);
        this.geographCoords = geographCoords;
        this.registrationDate = registrationDate.toString();
        this.alias = "";
        this.active = false;
        this.favourite = false;
    }

    private String normalizePlaceName(String placeName) {
        if(placeName != null){
            String capitalizedLetter = placeName.substring(0,1).toUpperCase();
            return capitalizedLetter + placeName.substring(1).toLowerCase();
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public GeographCoords getGeographCoords() {
        return geographCoords;
    }

    public void setGeographCoords(GeographCoords geographCoords) {
        this.geographCoords = geographCoords;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setIsActive(boolean active) {
        this.active = active;
    }

    public boolean isFavourite(){
        return favourite;
    }

    public void setIsFavorite(boolean isFavorite){
        this.favourite = isFavorite;
    }

    public String getMainName() {
        if (!alias.equals("")) return alias;
        if (placeName != null) return placeName;
        return getGeographCoords().toString();
    }

    public String getSubName(){
        if(!alias.equals("")){
            if (placeName != null) return placeName;
            return geographCoords.toString();
        }
        if(placeName != null){
            return  geographCoords.toString();
        }
        return "Topónimo desconocido";
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", alias='" + alias + '\'' +
                ", placeName='" + placeName + '\'' +
                ", geographCoords=" + geographCoords +
                ", registrationDate=" + registrationDate +
                ", isActive=" + active +
                '}';
    }
}
