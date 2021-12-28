package es.uji.geonews.model;

import android.util.Log;

import java.util.Locale;

public class Country {
    private String name;
    private String code;

    public Country() {}

    public Country(String code) {
        this.code = code;
        Locale apiCountry = new Locale.Builder().setRegion(code).build();
        Locale langSpanish  = new Locale.Builder().setLanguage("es").build();
        this.name = apiCountry.getDisplayCountry(langSpanish);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
