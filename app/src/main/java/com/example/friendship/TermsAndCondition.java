package com.example.friendship;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.friendship.BasicDetails.DetailsActivity1;

public class TermsAndCondition extends AppCompatActivity {

    TextView tvAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);

        tvAccept = findViewById(R.id.tvAccept);

        tvAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TermsAndCondition.this, DetailsActivity1.class);
                startActivity(i);
            }
        });
    }
}