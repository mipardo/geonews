package es.uji.geonews.controller.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import es.uji.geonews.R;
import es.uji.geonews.model.data.News;

public class CurrentsAdapter extends RecyclerView.Adapter<CurrentsViewHolder> {
    private List<News> news;

    public CurrentsAdapter(List<News> news) {
        this.news = news;
    }

    public void updateNews(List<News> news) {
        this.news = news;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CurrentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.currents_card,
                parent,
                false
        );
        return new CurrentsViewHolder(view, parent.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull CurrentsViewHolder holder, int position) {
        News n = (News) news.get(position);
        holder.bind(n);
    }

    @Override
    public int getItemCount() {
        return news.size();
    }
}
