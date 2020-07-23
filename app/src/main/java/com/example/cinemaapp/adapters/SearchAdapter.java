package com.example.cinemaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.cinemaapp.R;
import com.example.cinemaapp.activity.InfoActivity;
import com.example.cinemaapp.activity.SearchActivity;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.Result;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    MovieModel searchList;
    Context context;

    public SearchAdapter(MovieModel searchList, Context context) {
        this.searchList = searchList;
        this.context = context;
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.search_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Result result = searchList.getResults().get(position);
        final TextView searchImageNotFound = holder.searchImageNotFound;

        searchImageNotFound.setVisibility(View.GONE);

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w1280" + result.getPosterPath())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        searchImageNotFound.setVisibility(View.VISIBLE);
                        holder.searchImageProgressBar.setVisibility(View.GONE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        searchImageNotFound.setVisibility(View.GONE);

                        return false;
                    }
                })
                .into(holder.searchImage);

        holder.searchTitle.setText(result.getTitle());
        holder.searchYear.setText(result.getReleaseDate());
        holder.searchRating.setRating(result.getVoteAverage().floatValue() / 2);

        holder.searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InfoActivity.class);

                i.putExtra("currentMovie", position);
                i.putExtra("isSearch", false);
                i.putExtra("isSimilarList", false);
                i.putExtra("isFavorite", false);

                if (context.getClass().getName().equals("com.example.cinemaapp.activity.FavoriteActivity"))
                    i.putExtra("isFavorite", true);
                else if (context.getClass().getName().equals("com.example.cinemaapp.activity.InfoActivity")) {
                    i.putExtra("isSimilarList", true);
                    ((Activity) context).finish();
                } else
                    i.putExtra("isSearch", true);

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (SearchActivity.searchInput != null && SearchActivity.searchInput.getText().length() == 0)
            return 0;

        return searchList.getResults().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView searchCard;
        ProgressBar searchImageProgressBar;
        ImageView searchImage;
        TextView searchImageNotFound;
        TextView searchTitle;
        TextView searchYear;
        RatingBar searchRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            searchCard = itemView.findViewById(R.id.searchCard);
            searchImageProgressBar = itemView.findViewById(R.id.searchImageProgressBar);
            searchImage = itemView.findViewById(R.id.searchImage);
            searchImageNotFound = itemView.findViewById(R.id.searchImageNotFound);
            searchTitle = itemView.findViewById(R.id.searchTitle);
            searchYear = itemView.findViewById(R.id.searchYear);
            searchRating = itemView.findViewById(R.id.searchRating);
        }
    }
}
