package com.example.friendship.BasicDetails;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendship.MainActivity;
import com.example.friendship.R;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity3 extends AppCompatActivity {

    Spinner spinner ;
    String yearSelected;
    GifImageView frog1,frog2,frog3;
    TextView tvSkip;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details3);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvSkip = findViewById(R.id.tvSkip);

        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected =parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"selected"+yearSelected,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Vegetarian");
        arrayList.add("NonVegetarian");
        arrayList.add("Flexiterian");
        arrayList.add("Vegan");
        arrayList.add("Pollotarians");
        arrayList.add("Pescetarians");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {


                frog1.setVisibility(View.INVISIBLE);
                frog2.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog2.setVisibility(View.VISIBLE);

                        handler.postDelayed(new Runnable() {
                            public void run() {

                                frog2.setVisibility(View.INVISIBLE);
                                frog3.setVisibility(View.VISIBLE);
                            }
                        }, 0);

                    }
                }, 3000);

            }
        }, 3000);


        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity3.this, MainActivity.class);
                startActivity(i);
            }
        });


    }
}