package es.uji.geonews.model;

public class OpenWeatherLocationData implements ServiceLocationData {
    private double maxTemp;
    private double minTemp;
    private double actTemp;
    private String main;
    private String description;
    private String icon;

    public double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public double getActTemp() {
        return actTemp;
    }

    public void setActTemp(double actTemp) {
        this.actTemp = actTemp;
    }

    public String getMain() {
        return main;
    }

    public void setMain(String main) {
        this.main = main;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
