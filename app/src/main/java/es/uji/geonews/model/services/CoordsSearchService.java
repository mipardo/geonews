package es.uji.geonews.model.services;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;

import es.uji.geonews.model.GeographCoords;

public class CoordsSearchService extends Service  {

    private static final  String GEOCODE_API_KEY = "739559811684314511027x58957";
    private final OkHttpClient client;

    public CoordsSearchService() {
        super("Geocode", "Coordinates Search Service", LocalDate.now());
        client = new OkHttpClient();
    }

    public boolean isAvailable(){
        return true;
    }

    public GeographCoords getCoordsFrom(String placeName){
        String url = "https://geocode.xyz/"+ placeName +"?json=1&auth=" + GEOCODE_API_KEY;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;
        GeographCoords geographCoords = new GeographCoords();

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            double longt = jsonObject.getDouble("longt");
            double latt = jsonObject.getDouble("latt");
            geographCoords.setPrint(jsonObject.toString());
            geographCoords.setLatitude(latt);
            geographCoords.setLongitude(longt);
        } catch (IOException | JSONException exception){
            exception.printStackTrace();
        }
        return geographCoords;
    }
}
