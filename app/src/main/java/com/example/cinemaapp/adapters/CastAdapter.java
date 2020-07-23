package com.example.cinemaapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.example.cinemaapp.R;
import com.example.cinemaapp.api.RetrofitInstance;
import com.example.cinemaapp.models.Cast;
import com.example.cinemaapp.models.CastModel;
import com.example.cinemaapp.models.Person;
import com.example.cinemaapp.models.PersonModel;

import org.apache.commons.lang3.StringUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.ViewHolder> {
    CastModel cast;
    Context context;

    public CastAdapter(CastModel cast, Context context) {
        this.cast = cast;
        this.context = context;
    }

    @NonNull
    @Override
    public CastAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.cast_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final CastAdapter.ViewHolder holder, final int position) {
        Cast currentCast = cast.getCast().get(position);

        RetrofitInstance.getRetrofitInstance().getPerson(currentCast.getCreditId()).enqueue(new Callback<PersonModel>() {
            @Override
            public void onResponse(final Call<PersonModel> call, final Response<PersonModel> response) {
                if (response.body() != null && !((Activity) context).isDestroyed()) {
                    final Person person = response.body().getPerson();

                    Glide.with(context)
                            .load("https://image.tmdb.org/t/p/w1280" + person.getProfilePath())
                            .error(R.drawable.ic_person)
                            .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                            .into(holder.castImage);

                    holder.castCard.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://themoviedb.org/person/" + person.getId())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PersonModel> call, Throwable t) {
                t.printStackTrace();
            }
        });

        holder.castName.setText(StringUtils.capitalize(currentCast.getName()));
        holder.castCharacter.setText(StringUtils.capitalize(currentCast.getCharacter()));
    }

    @Override
    public int getItemCount() {
        return cast.getCast().size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout castCard;
        ImageView castImage;
        TextView castName;
        TextView castCharacter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            castCard = itemView.findViewById(R.id.castCard);
            castImage = itemView.findViewById(R.id.castImage);
            castName = itemView.findViewById(R.id.castName);
            castCharacter = itemView.findViewById(R.id.castCharacter);
        }
    }
}
