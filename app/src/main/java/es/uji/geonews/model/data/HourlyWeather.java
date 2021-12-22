package es.uji.geonews.model.data;

public class HourlyWeather extends Weather {
    private int pop;

    public HourlyWeather() {}

    public int getPop() {
        return pop;
    }

    public void setPop(int pop) {
        this.pop = pop;
    }
}
