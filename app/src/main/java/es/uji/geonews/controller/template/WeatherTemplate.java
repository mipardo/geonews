package es.uji.geonews.controller.template;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class WeatherTemplate {
    private TextView dateTextview;
    private TextView maxTempTextview;
    private TextView minTempTextview;
    private TextView actualTempTextview;
    private TextView weatherDescriptionTextview;
    private ImageView weatherIcon;
    private ProgressBar progressBar;

    public WeatherTemplate() {}

    public TextView getDateTextview() {
        return dateTextview;
    }

    public void setDateTextview(TextView dateTextview) {
        this.dateTextview = dateTextview;
    }

    public TextView getMaxTempTextview() {
        return maxTempTextview;
    }

    public void setMaxTempTextview(TextView maxTempTextview) {
        this.maxTempTextview = maxTempTextview;
    }

    public TextView getMinTempTextview() {
        return minTempTextview;
    }

    public void setMinTempTextview(TextView minTempTextview) {
        this.minTempTextview = minTempTextview;
    }

    public TextView getActualTempTextview() {
        return actualTempTextview;
    }

    public void setActualTempTextview(TextView actualTempTextview) {
        this.actualTempTextview = actualTempTextview;
    }

    public TextView getWeatherDescriptionTextview() {
        return weatherDescriptionTextview;
    }

    public void setWeatherDescriptionTextview(TextView weatherDescriptionTextview) {
        this.weatherDescriptionTextview = weatherDescriptionTextview;
    }

    public ImageView getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(ImageView weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}