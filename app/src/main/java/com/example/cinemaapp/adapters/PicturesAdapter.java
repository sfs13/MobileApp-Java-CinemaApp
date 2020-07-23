package com.example.cinemaapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.cinemaapp.models.PictureModel;

public class PicturesAdapter extends PagerAdapter {
    Context context;
    PictureModel pictures;

    public PicturesAdapter(Context context, PictureModel pictures) {
        this.context = context;
        this.pictures = pictures;
    }

    @Override
    public int getCount() {
        return pictures.getBackdrops().size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull final ViewGroup container, int position) {
        final ImageView picture = new ImageView(context);

        Glide.with(context)
                .load("https://image.tmdb.org/t/p/w1280" + pictures.getBackdrops().get(position).getFilePath())
                .into(picture);

        picture.setScaleType(ImageView.ScaleType.FIT_XY);

        container.addView(picture);

        return picture;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ImageView) object);
    }
}
