package com.example.friendship.BasicDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.MainActivity;
import com.example.friendship.R;
import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity5 extends AppCompatActivity {

    LottieAnimationView tvNext;
    LottieAnimationView lottieAnimationView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details5);

        tvNext = findViewById(R.id.tvNext);

        lottieAnimationView = findViewById(R.id.lottie_animation);
        lottieAnimationView.playAnimation();

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity5.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}