package es.uji.geonews.model.database;

import es.uji.geonews.model.dao.UserDao;

public interface Callback {
    void onSuccess(UserDao userDao);
    void onFailure(Exception e);
}
