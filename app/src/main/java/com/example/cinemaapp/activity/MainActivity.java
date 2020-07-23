package com.example.cinemaapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.cinemaapp.R;
import com.example.cinemaapp.fragments.FragmentSlide;

public class MainActivity extends AppCompatActivity {
    public static String[] slides = {
            "В прокате",
            "Популярное",
            "Лучшее",
            "Скоро"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFrame, new FragmentSlide())
                .commit();

        findViewById(R.id.mainFavorite).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));
            }
        });

        findViewById(R.id.mainSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        addSlides(((LinearLayout) findViewById(R.id.slideList)), 0);
    }

    private void addSlides(final LinearLayout slideList, final int currentSlide) {
        slideList.removeAllViews();

        for (int i = 0; i < slides.length; i++) {
            final int finalI = i;

            View view = LayoutInflater.from(this).inflate(R.layout.slide_button, slideList, false);

            final TextView slideText = view.findViewById(R.id.slideText);

            if (i == currentSlide) {
                slideText.setTextColor(getColor(R.color.colorEnabled));
                slideText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.ic_bottom_line);
            } else {
                slideText.setTextColor(getColor(R.color.colorDisabled));
                slideText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
            }

            slideText.setText(slides[i]);
            slideText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (slideText.getCurrentTextColor() != getColor(R.color.colorEnabled) && !FragmentSlide.movie.getResults().isEmpty()) {
                        changeFragment(finalI + 1);

                        addSlides(slideList, finalI);
                    }
                }
            });

            slideList.addView(view);
        }
    }

    private void changeFragment(int slide) {
        Fragment fragmentSlide = new FragmentSlide();

        Bundle bundle = new Bundle();
        bundle.putInt("currentSlide", slide);
        fragmentSlide.setArguments(bundle);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, fragmentSlide)
                .commit();
    }
}