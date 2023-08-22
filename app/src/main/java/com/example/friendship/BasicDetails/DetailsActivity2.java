package com.example.friendship.BasicDetails;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.friendship.R;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity2 extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ImageView tvCal;
    EditText etCal;
    GifImageView frog1,frog2,frog3;
    GifImageView tvNext;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details2);
        tvCal = findViewById(R.id.calender);
        etCal = findViewById(R.id.etCal);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvNext = findViewById(R.id.tvNext);

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            public void run() {


                frog1.setVisibility(View.INVISIBLE);
                frog2.setVisibility(View.VISIBLE);
                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog2.setVisibility(View.VISIBLE);
                    }
                }, 3000);

            }
        }, 3000);
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity2.this,DetailsActivity3.class);
                startActivity(i);
            }
        });

        tvCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(),"date picker");
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int date) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,month);
        c.set(Calendar.DATE,date);
        String birthDateString = DateFormat.getDateInstance().format(c.getTime());
        etCal.setText(birthDateString);


    }
}