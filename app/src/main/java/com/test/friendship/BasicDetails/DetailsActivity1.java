package com.test.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.test.friendship.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DetailsActivity1 extends AppCompatActivity {

    LottieAnimationView frog1,frog2,frog3;
    ImageView tvNext;
    Spinner spinner ;
    String yearSelected;
    EditText etHobbies,etShortDetails;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("unregistered");
    DatabaseReference reference;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details1);
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);
        tvNext = findViewById(R.id.tvNext);
        spinner = findViewById(R.id.spinner);
        etHobbies =findViewById(R.id.etHobbies);
        etShortDetails = findViewById(R.id.etShortDetails);

        Handler handler = new Handler();


        handler.postDelayed(new Runnable() {
            public void run() {


                frog2.setVisibility(View.VISIBLE);

                handler.postDelayed(new Runnable() {
                    public void run() {

                        frog2.setVisibility(View.VISIBLE);
                    }
                }, 3000);

            }
        }, 3000);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                yearSelected =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(etShortDetails.getText().toString().length()<100){
                    Toast.makeText(getApplicationContext(),"Add Atleast 30 Words",Toast.LENGTH_SHORT).show();
                    return;
                }if(etHobbies.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Enter Your Hobbies",Toast.LENGTH_SHORT).show();
                    return;
                }


                reference = FirebaseDatabase.getInstance().getReference("Users");
                Map<String,Object> data = new HashMap<>();
                data.put("bio",etShortDetails.getText().toString());

                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(data);


                Map<String,Object> users = new HashMap<>();
                users.put("shortBio",etShortDetails.getText().toString());

                users.put("hobbies",etHobbies.getText().toString());

                users.put("year",yearSelected);

                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "updated", Toast.LENGTH_SHORT).show();
                    }
                });




                Intent i = new Intent(DetailsActivity1.this,DetailsActivity2.class);
                startActivity(i);
                finish();
            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("2020-2024");
        arrayList.add("2021-2025");
        arrayList.add("2022-2026");
        arrayList.add("2023-2027");
        arrayList.add("M.Tech");
        arrayList.add("P.H.D");
        arrayList.add("Dual Degree");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);






    }
}