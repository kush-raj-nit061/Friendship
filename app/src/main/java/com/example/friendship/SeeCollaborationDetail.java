package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class SeeCollaborationDetail extends AppCompatActivity {
    ImageView image;
    TextView tvName,tvCategory,tvRequirement,tvDescription,tvDate;
    Button btnCollab,delete;
    SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_collaboration_detail);
        tvDate = findViewById(R.id.date);
        tvName = findViewById(R.id.projectName);
        tvCategory = findViewById(R.id.projectCategory);
        tvRequirement = findViewById(R.id.tvReq);
        tvDescription = findViewById(R.id.tvDescr);
        image = findViewById(R.id.image);
        btnCollab = findViewById(R.id.btnCollab);
        delete = findViewById(R.id.delete);

        Intent i = getIntent();
        String date = i.getStringExtra("date");
        long dat =Long.parseLong(date);

        tvDate.setText(String.valueOf(datePatternFormat.format(dat)).substring(0,11));
        tvName.setText(i.getStringExtra("projectname"));
        tvCategory.setText(i.getStringExtra("projecttype"));
        tvDescription.setText(i.getStringExtra("description"));
        tvRequirement.setText(i.getStringExtra("requirement"));
        String id = i.getStringExtra("id");

        Glide.with(getApplicationContext()).load(i.getStringExtra("purl")).into(image);

        if(id.equals(FirebaseAuth.getInstance().getUid())){
            delete.setVisibility(View.VISIBLE);
            btnCollab.setVisibility(View.GONE);
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase.getInstance().getReference("Collab").child(id).removeValue();
                Toast.makeText(v.getContext(), "Deleted", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

        btnCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Collab").child(id);
                Map<String,Object> map = new HashMap<>();
                map.put("id",FirebaseAuth.getInstance().getUid());
                if(!id.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {

                    dbRef.child("members").child(FirebaseAuth.getInstance().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "Request For Collab added", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }else{

                    Toast.makeText(getApplicationContext(), "It's Your Project", Toast.LENGTH_SHORT).show();
                }


            }
        });


    }
}