package es.uji.geonews.model.data;

import java.util.List;

public class CurrentsData extends ServiceData {
    private List<News> newsList;

    public CurrentsData(){}

    public List<News> getNewsList() {
        return newsList;
    }

    public void setNewsList(List<News> newsList) {
        this.newsList = newsList;
    }

}
