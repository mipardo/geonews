package es.uji.geonews.controller.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

import es.uji.geonews.R;
import es.uji.geonews.model.data.News;

public class CurrentsViewHolder extends RecyclerView.ViewHolder {
    private final TextView publishedDate;
    private final TextView title;
    private final TextView category;
    private final ImageView picture;
    private final TextView author;
    private final TextView description;
    private final TextView newsUrl;
    private final Context context;

    public CurrentsViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context = context;
        this.publishedDate = itemView.findViewById(R.id.dateTextview);
        this.title = itemView.findViewById(R.id.textTitular1);
        this.category = itemView.findViewById(R.id.textviewCategory1);
        this.picture = itemView.findViewById(R.id.imageView1);
        this.author = itemView.findViewById(R.id.textAuthor1);
        this.description = itemView.findViewById(R.id.textDescripcion1);
        this.newsUrl = itemView.findViewById(R.id.newsUrl);
    }

    public void bind(News news) {
        publishedDate.setText(parseDate(news.getPublished()));
        title.setText(news.getTitle());
        category.setText("Categorías: " + String.join(", ", news.getCategory()));
        author.setText(news.getAuthor());
        description.setText(news.getDescription());
        Picasso.get()
                .load(news.getImage())
                .placeholder(R.drawable.progress_animation)
                .into(picture, new Callback() {
                    @Override
                    public void onSuccess() {
                        picture.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onError(Exception e) {
                        picture.setVisibility(View.GONE);
                    }
                });
        newsUrl.setText(news.getUrl());
        newsUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                context.startActivity(browserIntent);
            }
        });
    }

    public String parseDate(String date) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss +0000");
        LocalDateTime dateTime = LocalDateTime.parse(date, inputFormatter);
        DateTimeFormatter outputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("dd MMMM HH:mm")
                .toFormatter(new Locale("es", "ES"));
        return dateTime.format(outputFormatter);
    }
}
