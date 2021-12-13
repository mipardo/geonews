package es.uji.geonews.controller.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import es.uji.geonews.R;
import es.uji.geonews.model.data.News;

public class CurrentsViewHolder extends RecyclerView.ViewHolder {
    private final TextView publishedDate;
    private final TextView title;
    private final TextView category;
    private final ImageView picture;
    private final TextView author;
    private final TextView description;

    public CurrentsViewHolder(@NonNull View itemView) {
        super(itemView);
        this.publishedDate = itemView.findViewById(R.id.dateTextview);
        this.title = itemView.findViewById(R.id.textTitular1);
        this.category = itemView.findViewById(R.id.textviewCategory1);
        this.picture = itemView.findViewById(R.id.imageView1);
        this.author = itemView.findViewById(R.id.textAuthor1);
        this.description = itemView.findViewById(R.id.textDescripcion1);
    }

    public void bind(News news) {
        publishedDate.setText(news.getPublished());
        title.setText(news.getTitle());
        category.setText(String.join(", ", news.getCategory()));
        author.setText(news.getAuthor());
        description.setText(news.getDescription());
        Picasso.get()
                .load(news.getImage())
                .into(picture);
    }
}
