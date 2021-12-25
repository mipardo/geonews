package es.uji.geonews.model.data;

public class DailyWeather extends Weather {
    private long moonrise;
    private long moonset;
    private double moonPhase;
    private double tempMin;
    private double tempMax;
    private double tempNight;
    private double tempEvening;
    private double tempMorning;
    private double feelsLikeNight;
    private double feelsLikeEvening;
    private double feelsLikeMorning;
    private double pop;

    public DailyWeather() {}

    public long getMoonrise() {
        return moonrise;
    }

    public void setMoonrise(long moonrise) {
        this.moonrise = moonrise;
    }

    public long getMoonset() {
        return moonset;
    }

    public void setMoonset(long moonset) {
        this.moonset = moonset;
    }

    public double getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(double moonPhase) {
        this.moonPhase = moonPhase;
    }

    public double getTempMin() {
        return tempMin;
    }

    public void setTempMin(double tempMin) {
        this.tempMin = tempMin;
    }

    public double getTempMax() {
        return tempMax;
    }

    public void setTempMax(double tempMax) {
        this.tempMax = tempMax;
    }

    public double getTempNight() {
        return tempNight;
    }

    public void setTempNight(double tempNight) {
        this.tempNight = tempNight;
    }

    public double getTempEvening() {
        return tempEvening;
    }

    public void setTempEvening(double tempEvening) {
        this.tempEvening = tempEvening;
    }

    public double getTempMorning() {
        return tempMorning;
    }

    public void setTempMorning(double tempMorning) {
        this.tempMorning = tempMorning;
    }

    public double getFeelsLikeNight() {
        return feelsLikeNight;
    }

    public void setFeelsLikeNight(double feelsLikeNight) {
        this.feelsLikeNight = feelsLikeNight;
    }

    public double getFeelsLikeEvening() {
        return feelsLikeEvening;
    }

    public void setFeelsLikeEvening(double feelsLikeEvening) {
        this.feelsLikeEvening = feelsLikeEvening;
    }

    public double getFeelsLikeMorning() {
        return feelsLikeMorning;
    }

    public void setFeelsLikeMorning(double feelsLikeMorning) {
        this.feelsLikeMorning = feelsLikeMorning;
    }

    public double getPop() {
        return pop;
    }

    public void setPop(double pop) {
        this.pop = pop;
    }
}
