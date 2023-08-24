package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class Testing1 extends AppCompatActivity {

    EditText etid,etval;
    Button btn;

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Connection");

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing1);
        etid=findViewById(R.id.etid);
        etval=findViewById(R.id.etVal);
        btn = findViewById(R.id.button2);



        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<>();
                map.put(etid.getText().toString(),etval.getText().toString());
                myRef.child(String.valueOf(fAuth.getUid())).updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(),etid.getText().toString(),Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"Check your network connection",Toast.LENGTH_LONG).show();

                    }
                });

            }
        });


    }
}