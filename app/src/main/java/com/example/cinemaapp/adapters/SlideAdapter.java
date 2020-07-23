package com.example.cinemaapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.cinemaapp.R;
import com.example.cinemaapp.activity.InfoActivity;
import com.example.cinemaapp.activity.MainActivity;
import com.example.cinemaapp.fragments.FragmentSlide;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.Result;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.ViewHolder> {
    public static MovieModel movie;
    Context context;
    boolean callFromGenres;

    public SlideAdapter(MovieModel movie, Context context, boolean callFromGenres) {
        SlideAdapter.movie = movie;
        this.context = context;
        this.callFromGenres = callFromGenres;
    }

    @NonNull
    @Override
    public SlideAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.movie_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final TextView cardImageNotFound = holder.cardImageNotFound;
        final ProgressBar cardProgressBar = holder.cardProgressBar;

        Result result = movie.getResults().get(position);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(100);
        animation.setRepeatCount(0);
        holder.cardMain.startAnimation(animation);

        cardImageNotFound.setVisibility(View.GONE);

        Glide.with(context)
                .load(String.format("https://image.tmdb.org/t/p/w1280%s", result.getPosterPath()))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        cardProgressBar.setVisibility(View.GONE);
                        cardImageNotFound.setVisibility(View.VISIBLE);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        cardProgressBar.setVisibility(View.GONE);
                        cardImageNotFound.setVisibility(View.GONE);

                        return false;
                    }
                })
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(holder.cardImage);

        holder.cardMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, InfoActivity.class);

                i.putExtra("currentMovie", position);
                i.putExtra("isSearch", false);
                i.putExtra("callFromGenres", callFromGenres);

                context.startActivity(i);
            }
        });

        holder.cardName.setText(result.getTitle());

        if (MainActivity.slides[FragmentSlide.currentSlide - 1].equals("Скоро")) {
            holder.cardRatingBar.setVisibility(View.GONE);

            holder.cardRating.setText(result.getReleaseDate());
        } else {
            holder.cardRating.setVisibility(View.GONE);

            holder.cardRatingBar.setRating((float) (result.getVoteAverage() / 2));
        }
    }

    @Override
    public int getItemCount() {
        return movie.getResults().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ProgressBar cardProgressBar;
        LinearLayout cardMain;
        ImageView cardImage;
        TextView cardImageNotFound;
        TextView cardName;
        TextView cardRating;
        RatingBar cardRatingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardProgressBar = itemView.findViewById(R.id.cardProgressBar);
            cardProgressBar.setVisibility(View.VISIBLE);

            cardMain = itemView.findViewById(R.id.cardMain);
            cardImageNotFound = itemView.findViewById(R.id.cardImageNotFound);
            cardImage = itemView.findViewById(R.id.cardImage);
            cardName = itemView.findViewById(R.id.cardName);
            cardRating = itemView.findViewById(R.id.cardRating);
            cardRatingBar = itemView.findViewById(R.id.cardRatingBar);
        }
    }
}
