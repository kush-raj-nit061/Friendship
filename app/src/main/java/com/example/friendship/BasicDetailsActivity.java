package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class BasicDetailsActivity extends AppCompatActivity {

    ImageView imgButton,ivProfile;
    String purl;
    Button btnSaveDetails;
    EditText etShortDetails,etYOS,etHobbies,etBirthday,etBooks,etFoods,etTravelLike,etQualityLike,etQualityDislike,etLocation;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_details);
        ivProfile = findViewById(R.id.ivProfile);
        imgButton = findViewById(R.id.ivProfilee);
        btnSaveDetails = findViewById(R.id.btnSaveDetails);

        etLocation = findViewById(R.id.etLocation);
        etBirthday = findViewById(R.id.etBirthday);
        etBooks = findViewById(R.id.etBooks);
        etFoods = findViewById(R.id.etFoods);
        etHobbies = findViewById(R.id.etHobbies);
        etQualityDislike = findViewById(R.id.etQualityDisike);
        etQualityLike = findViewById(R.id.etQualityLike);
        etShortDetails = findViewById(R.id.etShortDetails);
        etTravelLike = findViewById(R.id.etTravel);
        etYOS = findViewById(R.id.etYOS);











        btnSaveDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> users = new HashMap<>();
                if(purl.isEmpty()){
                    purl = "https://firebasestorage.googleapis.com/v0/b/tesla-members-record.appspot.com/o/friends-low-resolution-logo-color-on-transparent-background.png?alt=media&token=507e6418-5807-4439-b148-9015755a2213";
                }
                users.put("purl",purl);
                users.put("shortBio",etShortDetails.getText().toString());
                users.put("location",etLocation.getText().toString());
                users.put("hobbies",etHobbies.getText().toString());
                users.put("books",etBooks.getText().toString());
                users.put("foods",etFoods.getText().toString());
                users.put("year",etYOS.getText().toString());
                users.put("travellike",etTravelLike.getText().toString());
                users.put("qualitylike",etQualityLike.getText().toString());
                users.put("qualitydislike",etQualityDislike.getText().toString());
                users.put("birthday",etBirthday.getText().toString());
                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Map <String,Object> map = new HashMap<>();
                        map.put("detailsGiven","1");
                        FirebaseFirestore fStore= FirebaseFirestore.getInstance();

                        fStore.collection("students").document(fAuth.getCurrentUser().getUid()).update(map);
                    }
                });



                Intent i = new Intent(BasicDetailsActivity.this, MainActivity.class);
                startActivity(i);
            }
        });



        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallInt= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallInt,1000);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallInt= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallInt,1000);
            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                Uri imageUri =data.getData();

//                imgButton.setImageURI(imageUri);
//                progressBar.setVisibility(View.VISIBLE);

                uploadImageToFirebase(imageUri);
            }
        }


    }

    private void uploadImageToFirebase(Uri imageUri) {

        final StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        fileRef.putFile((imageUri)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {



                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        purl = String.valueOf(uri);

                        Picasso.get().load(uri).into(ivProfile);
//                        progressBar.setVisibility(View.GONE);
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BasicDetailsActivity.this,"Failed.",Toast.LENGTH_LONG).show();
            }
        });


    }


}