package es.uji.geonews.controller.template;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;

public class WeatherTemplate {
    private TextView weatherTitleOutput;
    private ViewGroup loadingLayout;
    private TextView currentTempOutput;
    private TextView minTempOutput;
    private TextView maxTempOutput;
    private ImageView iconOutput;
    private TextView descriptionOutput;
    private TextView sunriseOutput;
    private TextView sunsetOuptut;
    private TextView uvOutput;
    private TextView visibilityOutput;
    private TextView moonsetOutput;
    private TextView moonriseOutput;
    private TextView cloudsPercentageOutput;
    private TextView feelsLikeOutput;
    private RecyclerView precipitationsOutput;
    private LineChart lineChart;


    public WeatherTemplate(){ }

    public TextView getWeatherTitleOutput() {
        return weatherTitleOutput;
    }

    public void setWeatherTitleOutput(TextView weatherTitleOutput) {
        this.weatherTitleOutput = weatherTitleOutput;
    }

    public ViewGroup getLoadingLayout() {
        return loadingLayout;
    }

    public void setLoadingLayout(ViewGroup loadingLayout) {
        this.loadingLayout = loadingLayout;
    }

    public TextView getCurrentTempOutput() {
        return currentTempOutput;
    }

    public void setCurrentTempOutput(TextView currentTempOutput) {
        this.currentTempOutput = currentTempOutput;
    }

    public TextView getMinTempOutput() {
        return minTempOutput;
    }

    public void setMinTempOutput(TextView minTempOutput) {
        this.minTempOutput = minTempOutput;
    }

    public TextView getMaxTempOutput() {
        return maxTempOutput;
    }

    public void setMaxTempOutput(TextView maxTempOutput) {
        this.maxTempOutput = maxTempOutput;
    }

    public ImageView getIconOutput() {
        return iconOutput;
    }

    public void setIconOutput(ImageView iconOutput) {
        this.iconOutput = iconOutput;
    }

    public TextView getDescriptionOutput() {
        return descriptionOutput;
    }

    public void setDescriptionOutput(TextView descriptionOutput) {
        this.descriptionOutput = descriptionOutput;
    }

    public TextView getSunriseOutput() {
        return sunriseOutput;
    }

    public void setSunriseOutput(TextView sunriseOutput) {
        this.sunriseOutput = sunriseOutput;
    }

    public TextView getSunsetOuptut() {
        return sunsetOuptut;
    }

    public void setSunsetOuptut(TextView sunsetOuptut) {
        this.sunsetOuptut = sunsetOuptut;
    }

    public TextView getUvOutput() {
        return uvOutput;
    }

    public void setUvOutput(TextView uvOutput) {
        this.uvOutput = uvOutput;
    }

    public TextView getVisibilityOutput() {
        return visibilityOutput;
    }

    public void setVisibilityOutput(TextView visibilityOutput) {
        this.visibilityOutput = visibilityOutput;
    }

    public TextView getMoonsetOutput() {
        return moonsetOutput;
    }

    public void setMoonsetOutput(TextView moonsetOutput) {
        this.moonsetOutput = moonsetOutput;
    }

    public TextView getMoonriseOutput() {
        return moonriseOutput;
    }

    public void setMoonriseOutput(TextView moonriseOutput) {
        this.moonriseOutput = moonriseOutput;
    }

    public TextView getCloudsPercentageOutput() {
        return cloudsPercentageOutput;
    }

    public void setCloudsPercentageOutput(TextView cloudsPercentageOutput) {
        this.cloudsPercentageOutput = cloudsPercentageOutput;
    }

    public TextView getFeelsLikeOutput() {
        return feelsLikeOutput;
    }

    public void setFeelsLikeOutput(TextView feelsLikeOutput) {
        this.feelsLikeOutput = feelsLikeOutput;
    }

    public RecyclerView getPrecipitationsOutput() {
        return precipitationsOutput;
    }

    public void setPrecipitationsOutput(RecyclerView precipitationsOutput) {
        this.precipitationsOutput = precipitationsOutput;
    }

    public LineChart getLineChart() {
        return lineChart;
    }

    public void setLineChart(LineChart lineChart) {
        this.lineChart = lineChart;
    }
}
