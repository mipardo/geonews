package es.uji.geonews.model.data;

import java.util.List;

public class CurrentsData implements Data {
    private List<News> newsList;

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }
}
