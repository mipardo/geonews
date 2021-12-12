package es.uji.geonews.controller.template;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

public class CurrentsTemplate {
    private CardView cardview;
    private TextView lugerTextview;
    private TextView tituloTextview;
    private TextView categoryTextview;
    private TextView descripcionTextview;
    private TextView authorTextview;
    private TextView dateTextview;
    private ImageView imageView;
    private TextView aviso;

    public CurrentsTemplate() {}

    public TextView getLugerTextview() {
        return lugerTextview;
    }

    public CardView getCardview() {
        return cardview;
    }

    public void setCardview(CardView cardview) {
        this.cardview = cardview;
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

    public TextView getAviso() {
        return aviso;
    }

    public void setAviso(TextView aviso) {
        this.aviso = aviso;
    }
}
