package com.example.cinemaapp.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cinemaapp.R;
import com.example.cinemaapp.fragments.FragmentSlide;
import com.example.cinemaapp.models.Genre;
import com.example.cinemaapp.models.GenreModel;
import com.example.cinemaapp.models.MovieModel;
import com.example.cinemaapp.models.Result;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class GenreAdapter extends RecyclerView.Adapter<GenreAdapter.ViewHolder> {
    GenreModel genres;
    Context context;
    int currentGenre;

    public GenreAdapter(GenreModel genres, Context context, int currentGenre) {
        this.genres = genres;
        this.context = context;
        this.currentGenre = currentGenre;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.genre_button, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Genre genre = genres.getGenres().get(position);

        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setDuration(100);
        animation.setRepeatCount(0);
        holder.genreText.startAnimation(animation);

        holder.genreText.setText(StringUtils.capitalize(genre.getName()));

        if (position == currentGenre) {
            holder.genreText.setBackgroundTintList(ColorStateList.valueOf(context.getColor(R.color.colorPrimary)));
            holder.genreText.setTextColor(context.getColor(R.color.colorWhite));
        } else {
            holder.genreText.setBackgroundTintList(null);
            holder.genreText.setTextColor(context.getColor(R.color.colorEnabled));
        }

        holder.genreText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Result> movieResult = new ArrayList<>(FragmentSlide.movie.getResults());
                FragmentSlide.slideRecycler.setAdapter(null);

                if (genre.getId() != 0) {
                    for (Result i : FragmentSlide.movie.getResults()) {
                        if (!i.getGenreIds().contains(genre.getId())) {
                            movieResult.remove(i);
                        }
                    }
                }

                FragmentSlide.genresRecycler.setAdapter(new GenreAdapter(genres, context, position));
                FragmentSlide.genresRecycler.scrollToPosition(position);

                if (FragmentSlide.slideGoBack != null && FragmentSlide.slideGoBack.getVisibility() == View.VISIBLE)
                    FragmentSlide.slideGoBack.setVisibility(View.GONE);

                if (genre.getId() == 0)
                    FragmentSlide.slideRecycler.setAdapter(new SlideAdapter(new MovieModel(FragmentSlide.movie.getResults()), context, true));
                else
                    FragmentSlide.slideRecycler.setAdapter(new SlideAdapter(new MovieModel(movieResult), context, true));
            }
        });
    }

    @Override
    public int getItemCount() {
        return genres.getGenres().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView genreText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            genreText = itemView.findViewById(R.id.genreText);
        }
    }
}
