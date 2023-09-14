package com.test.friendship.Collaboration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.test.friendship.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CollabDetails1 extends AppCompatActivity {

    LottieAnimationView frog1,frog2,frog3;
    Chip chip1,chip2,chip3,chip4,chip5,chip6,chip7,chip8,chip9,chip10,chip11,chip12;

    EditText projectName,projectRequirement,etProjectType;

    ImageView tvNext;
    String projectType,name,requirement;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collab_details1);
        projectType = "";
        frog1 = findViewById(R.id.frog1);
        frog2= findViewById(R.id.frog2);
        frog3 = findViewById(R.id.frog3);

        chip1 = findViewById(R.id.chip1);
        chip2 = findViewById(R.id.chip2);
        chip3 = findViewById(R.id.chip3);
        chip4 = findViewById(R.id.chip4);
        chip5 = findViewById(R.id.chip5);
        chip6 = findViewById(R.id.chip6);
        chip7 = findViewById(R.id.chip7);
        chip8 = findViewById(R.id.chip8);
        chip9 = findViewById(R.id.chip9);
        chip10 = findViewById(R.id.chip10);
        chip11 = findViewById(R.id.chip11);
        chip12 = findViewById(R.id.chip12);

        projectName = findViewById(R.id.etProjectName);
        projectRequirement = findViewById(R.id.etRequirement);
        etProjectType = findViewById(R.id.etProjectType);


        tvNext = findViewById(R.id.tvNext);


        ArrayList<Chip> arrayChip = new ArrayList<>();
        arrayChip.add(chip1);
        arrayChip.add(chip2);
        arrayChip.add(chip3);
        arrayChip.add(chip4);
        arrayChip.add(chip5);
        arrayChip.add(chip6);
        arrayChip.add(chip7);
        arrayChip.add(chip8);
        arrayChip.add(chip9);
        arrayChip.add(chip10);
        arrayChip.add(chip11);

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

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0;i<arrayChip.size();i++){
                    Chip chip = arrayChip.get(i);
                    if(chip.isChecked()){
                        if(projectType == null || projectType.isEmpty()){
                            projectType=chip.getText().toString()+"";
                        }else {
                            projectType +=","+chip.getText().toString();
                        }
                    }

                }

                projectType+= ","+etProjectType.getText().toString();
                name = projectName.getText().toString();
                requirement= projectRequirement.getText().toString();


                if (TextUtils.isEmpty(projectType)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter Project Type!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter Project Name!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }if (TextUtils.isEmpty(requirement)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter Your Requirement for this project!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                Map<String,Object>  map = new HashMap<>();
                map.put("projectname",name);
                map.put("requirement",requirement);
                map.put("description","");
                map.put("projecttype",projectType);
                map.put("purl","");
                map.put("date","");

                FirebaseAuth fAuth = FirebaseAuth.getInstance();
                DatabaseReference db = FirebaseDatabase.getInstance().getReference("preCollab");
                try {
                    db.child(fAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent i = new Intent(CollabDetails1.this, CollabDetails2.class);
                            startActivity(i);
                            finish();
                        }
                    });

                }catch (Exception ignored){}




            }
        });
    }
}