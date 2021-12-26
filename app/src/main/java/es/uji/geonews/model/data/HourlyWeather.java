package es.uji.geonews.model.data;

public class HourlyWeather extends Weather {
    private double pop;

    public HourlyWeather() {}

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }
}
