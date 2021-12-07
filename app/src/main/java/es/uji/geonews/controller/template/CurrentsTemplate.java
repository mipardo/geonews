package es.uji.geonews.controller.template;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CurrentsTemplate {
    //private TextView dateTextview;
    private TextView lugerTextview;
    private TextView tituloTextview;
    private TextView categoryTextview;
    private TextView descripcionTextview;
    private TextView authorTextview;
    private TextView dateTextview;
    private ImageView imageView;
    private ProgressBar progressBar;

    public CurrentsTemplate() {}

    public TextView getLugerTextview() {
        return lugerTextview;
    }

    public void setLugerTextview(TextView lugerTextview) {
        this.lugerTextview = lugerTextview;
    }

    public TextView getTituloTextview() {
        return tituloTextview;
    }

    public void setTituloTextview(TextView tituloTextview) {
        this.tituloTextview = tituloTextview;
    }

    public TextView getCategoryTextview() {
        return categoryTextview;
    }

    public void setCategoryTextview(TextView categoryTextview) {
        this.categoryTextview = categoryTextview;
    }

    public TextView getDescripcionTextview() {
        return descripcionTextview;
    }

    public void setDescripcionTextview(TextView descripcionTextview) {
        this.descripcionTextview = descripcionTextview;
    }

    public TextView getAuthorTextview() {
        return authorTextview;
    }

    public void setAuthorTextview(TextView authorTextview) {
        this.authorTextview = authorTextview;
    }

    public TextView getDateTextview() {
        return dateTextview;
    }

    public void setDateTextview(TextView dateTextview) {
        this.dateTextview = dateTextview;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }
}
