package es.uji.geonews.model.services;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.AirVisualData;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.Request;
import okhttp3.Response;

public class AirVisualService extends ServiceHttp implements DataGetterStrategy {

    public static final String description = "AirVisual ofrece a cualquier persona acceso gratuito a" +
            " la mayor base de datos de calidad del aire del mundo, que abarca más de 10000 lugares en" +
            " todo el mundo, una lista que crece cada día. La aplicación y el sitio web de AirVisual " +
            "fueron los primeros en ofrecer una previsión de la contaminación para 7 días, desarrollada" +
            " internamente mediante aprendizaje automático e inteligencia artificial, lo que le permite " +
            "planificar con antelación y asegurarse de que sus actividades semanales tienen lugar cuando " +
            "la calidad del aire es más saludable.";

    public AirVisualService() {
        super(ServiceName.AIR_VISUAL, description);
        apiKey = "7f9615df-32fd-42fe-8c79-6fcaf18a2960";
        url = "api.airvisual.org";
    }

    @Override
    public boolean validateLocation(Location location) {
        String url = "https://api.airvisual.com/v2/nearest_city?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&key=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString("status").equals("success");

        } catch (IOException | JSONException exception) {
            return false;
        }
    }

    @Override
    public ServiceData getData(Location location) throws ServiceNotAvailableException {
        String url = "https://api.airvisual.com/v2/nearest_city?"
                + "lat=" + location.getGeographCoords().getLatitude()
                + "&lon=" + location.getGeographCoords().getLongitude()
                + "&key=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString("status").equals("success")) {
                return convertToAirVisualData(jsonObject);
            }
            return null;

        } catch (IOException | JSONException exception) {
            throw new ServiceNotAvailableException();
        }
    }


    private AirVisualData convertToAirVisualData(JSONObject jsonObject) throws JSONException {
        AirVisualData airVisualData = new AirVisualData();
        airVisualData.setTemperature(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("tp"));
        airVisualData.setPressure(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("pr"));
        airVisualData.setHumidity(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("hu"));
        airVisualData.setWindSpeed(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getDouble("ws"));
        airVisualData.setWindDirection(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getInt("wd"));
        airVisualData.setWeatherIcon(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("weather").getString("ic"));
        airVisualData.setAqiUs(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getInt("aqius"));
        airVisualData.setMainUs(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getString("mainus"));
        airVisualData.setAqiCn(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getInt("aqicn"));
        airVisualData.setMainCn(jsonObject.getJSONObject("data").getJSONObject("current").getJSONObject("pollution").getString("maincn"));
        return airVisualData;
    }
}
