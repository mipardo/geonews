package es.uji.geonews.controller.template;

import android.widget.ImageView;
import android.widget.TextView;

public class AirTemplate {
    private TextView tempertaureOutput;
    private TextView preassureOutput;
    private TextView humidityOutput;
    private TextView windSpeedOutput;
    private ImageView windDirectionOuptut;
    private TextView aqiUsOutput;
    private TextView aqiCnOutput;
    private TextView mainPollutantUsOutput;
    private TextView mainPollutantCnOutput;
    private TextView aqiMainTextOutput;
    private TextView aqiTextOutput;
    private ImageView aqiImageOutput;

    public AirTemplate() {}

    public TextView getTempertaureOutput() {
        return tempertaureOutput;
    }

    public void setTempertaureOutput(TextView tempertaureOutput) {
        this.tempertaureOutput = tempertaureOutput;
    }

    public TextView getPreassureOutput() {
        return preassureOutput;
    }

    public void setPreassureOutput(TextView preassureOutput) {
        this.preassureOutput = preassureOutput;
    }

    public TextView getHumidityOutput() {
        return humidityOutput;
    }

    public void setHumidityOutput(TextView humidityOutput) {
        this.humidityOutput = humidityOutput;
    }

    public TextView getWindSpeedOutput() {
        return windSpeedOutput;
    }

    public void setWindSpeedOutput(TextView windSpeedOutput) {
        this.windSpeedOutput = windSpeedOutput;
    }

    public ImageView getWindDirectionOuptut() {
        return windDirectionOuptut;
    }

    public void setWindDirectionOuptut(ImageView windDirectionOuptut) {
        this.windDirectionOuptut = windDirectionOuptut;
    }

    public TextView getAqiUsOutput() {
        return aqiUsOutput;
    }

    public void setAqiUsOutput(TextView aqiUsOutput) {
        this.aqiUsOutput = aqiUsOutput;
    }

    public TextView getAqiCnOutput() {
        return aqiCnOutput;
    }

    public void setAqiCnOutput(TextView aqiCnOutput) {
        this.aqiCnOutput = aqiCnOutput;
    }

    public TextView getMainPollutantUsOutput() {
        return mainPollutantUsOutput;
    }

    public void setMainPollutantUsOutput(TextView mainPollutantUsOutput) {
        this.mainPollutantUsOutput = mainPollutantUsOutput;
    }

    public TextView getMainPollutantCnOutput() {
        return mainPollutantCnOutput;
    }

    public void setMainPollutantCnOutput(TextView getMainPollutantCnOutput) {
        this.mainPollutantCnOutput = mainPollutantCnOutput;
    }

    public TextView getAqiMainTextOutput() {
        return aqiMainTextOutput;
    }

    public void setAqiMainTextOutput(TextView aqiMainTextOutput) {
        this.aqiMainTextOutput = aqiMainTextOutput;
    }

    public TextView getAqiTextOutput() {
        return aqiTextOutput;
    }

    public void setAqiTextOutput(TextView aqiTextOutput) {
        this.aqiTextOutput = aqiTextOutput;
    }

    public ImageView getAqiImageOutput() {
        return aqiImageOutput;
    }

    public void setAqiImageOutput(ImageView aqiImageOutput) {
        this.aqiImageOutput = aqiImageOutput;
    }
}
