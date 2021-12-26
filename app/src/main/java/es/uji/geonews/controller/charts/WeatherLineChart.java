package es.uji.geonews.controller.charts;

import android.graphics.Color;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import es.uji.geonews.model.data.DailyWeather;
import es.uji.geonews.model.data.OpenWeatherData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.data.Weather;

public class WeatherLineChart {
    private final LineChart lineChart;

    public WeatherLineChart(LineChart lineChart){
        this.lineChart = lineChart;
    }

    public void fillChart(OpenWeatherData openWeatherData){
        List<DailyWeather> forecast = openWeatherData.getDailyWeatherList();
        ArrayList<Entry> dataValues = new ArrayList<>();
        ArrayList<String> xLabels = new ArrayList<>();
        LocalDateTime today = LocalDateTime.now();
        for (int i = 0; i < 7; i++) {
            dataValues.add(new Entry(i, (float) (forecast.get(i).getPop() * 100)));
            xLabels.add(today.plusDays(i).getDayOfMonth() + "/" +  today.plusDays(i).getMonthValue()+ "");
        }

        LineDataSet lineDataSet = new LineDataSet(dataValues, "");
        lineDataSet.setDrawFilled(true);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setDrawValues(false);
        lineDataSet.setFillColor(Color.argb(230, 11, 174, 255));
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);

        LineData data = new LineData(dataSets);
        lineChart.setData(data);
        Description description = new Description();
        description.setPosition(980, 25);
        description.setTextSize(12);
        description.setText("Probabilidad de precipitación de hoy y los próximos días");
        lineChart.setDescription(description);
        lineChart.setTouchEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setDrawBorders(true);
        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(true);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setDrawGridLines(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setAxisMaximum(100);
        lineChart.getAxis(YAxis.AxisDependency.LEFT).setAxisMinimum(0);
        lineChart.getAxis(YAxis.AxisDependency.RIGHT).setEnabled(false);
        lineChart.getXAxis().setLabelRotationAngle(0f);
        lineChart.getXAxis().setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return xLabels.get((int)value);
            }
        });
        lineChart.animateY(2000);
        lineChart.invalidate();
    }


}
