package es.uji.geonews.model.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.uji.geonews.model.data.CurrentsData;
import es.uji.geonews.model.Location;
import es.uji.geonews.model.data.News;
import es.uji.geonews.model.data.ServiceData;
import es.uji.geonews.model.exceptions.ServiceNotAvailableException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CurrentsService extends ServiceHttp implements DataGetterStrategy {
    public CurrentsService() {
        super(ServiceName.CURRENTS, "News service");
        apiKey = "uVh9kGUA3ZArfrYzCaLkX4iW6nR1vy2LMHwesz40aEY4OHaj";
        url = "api.currentsapi.services";
    }

    @Override
    public void initializeClient() {
        this.client = new OkHttpClient().newBuilder()
                .connectTimeout(50, TimeUnit.SECONDS)
                .writeTimeout(50, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();
    }

    public boolean validateLocation(Location location){
        String url = "https://api.currentsapi.services/v1/search?language=es&"
                + "keywords=" + location.getPlaceName()
                + "&apiKey=" + apiKey;

        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            return jsonObject.getString("status").equals("ok");

        } catch (IOException | JSONException exception){
            return false;
        }
    }

    @Override
    public ServiceData getData(Location location) throws ServiceNotAvailableException {
        String url = "https://api.currentsapi.services/v1/search?language=es&"
                + "keywords=" + location.getPlaceName()
                + "&apiKey=" + apiKey;
        Request request = new Request.Builder().url(url).build();
        final JSONObject jsonObject;

        try (Response response = client.newCall(request).execute()) {
            jsonObject = new JSONObject(response.body().string());
            if (jsonObject.getString("status").equals("ok")){
                return convertToCurrentsData(jsonObject);
            }
            return null;

        } catch (IOException | JSONException exception){
            throw new ServiceNotAvailableException();
        }
    }

    @Override
    public List<ServiceData> getFutureData(Location location) {
        return null;
    }

    private CurrentsData convertToCurrentsData(JSONObject jsonObject) throws JSONException {
        CurrentsData currentsData = new CurrentsData();
        JSONArray locationNews = jsonObject.getJSONArray("news");
        if (locationNews.length() > 0) {
            List<News> newsList = new ArrayList<>();
            for (int i = 0; i < locationNews.length(); i++) {
                JSONObject actualNews = locationNews.getJSONObject(i);
                News news = new News();
                news.setId(actualNews.getString("id"));
                news.setTitle(actualNews.getString("title"));
                news.setDescription(actualNews.getString("description"));
                news.setUrl(actualNews.getString("url"));
                news.setAuthor(actualNews.getString("author"));
                news.setImage(actualNews.getString("image"));

                List<String> categories = new ArrayList<>();
                JSONArray serviceCategories = actualNews.getJSONArray("category");
                for (int j = 0; j < serviceCategories.length(); j++) {
                    categories.add(serviceCategories.getString(j));
                }
                news.setCategory(categories);
                news.setPublished(actualNews.getString("published"));
                newsList.add(news);
            }
            currentsData.setNewsList(newsList);
        }
        return currentsData;
    }

}


