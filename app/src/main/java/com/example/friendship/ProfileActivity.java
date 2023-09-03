package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgButton,ivProfile;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();

    ImageView ivEditInfo,ivEditShortBio;
    Button saveChanges;


    String userID;
    String purl;
    LinearLayout llnametv,llnameet,lllocationtv,lllocationet,llyeartv,llyearet,llshortbiotv,llshortbioet;

    TextView tvShortBio,tvLocation,tvName,tvYear,userid;
    EditText etShortBio,etLocation,etName,etYear;



    StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userid = findViewById(R.id.userId);
        userid.setText(fAuth.getUid().substring(0,8));

        tvShortBio= findViewById(R.id.tvShortBio);
        tvLocation = findViewById(R.id.tvLocation);
        tvName = findViewById(R.id.tvName);
        tvYear = findViewById(R.id.tvYear);

        etYear = findViewById(R.id.etYear);
        etShortBio = findViewById(R.id.etShortBio);
        etLocation = findViewById(R.id.etLocation);
        etName = findViewById(R.id.etName);

        imgButton = findViewById(R.id.ivAvatar);
        lllocationtv = findViewById(R.id.lllocationtv);
        llnametv = findViewById(R.id.llnametv);
        llyeartv = findViewById(R.id.llyeartv);


        llshortbiotv = findViewById(R.id.llshortbiotv);
        lllocationet = findViewById(R.id.lllocationet);
        llnameet = findViewById(R.id.llnameet);
        llyearet = findViewById(R.id.llyearet);
        llshortbioet = findViewById(R.id.llshortbioet);




        ivEditInfo = findViewById(R.id.ivEditInfo);
        ivEditShortBio = findViewById(R.id.ivEditShortDet);
        saveChanges = findViewById(R.id.btnSaveChanges);





        myRef.child(fAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    UserModel model = (UserModel) snapshot.getValue(UserModel.class);
                    tvName.setText(model.getName());
                    tvLocation.setText(model.getLocation());
                    tvShortBio.setText(model.getShortBio());
                    tvYear.setText(model.getYear());

                    etName.setText(model.getName());
                    etLocation.setText(model.getLocation());
                    etShortBio.setText(model.getShortBio());
                    etYear.setText(model.getYear());


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        llshortbioet.setVisibility(View.GONE);
        llnameet.setVisibility(View.GONE);
        lllocationet.setVisibility(View.GONE);
        llyearet.setVisibility(View.GONE);


        ivEditShortBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llshortbiotv.setVisibility(View.GONE);
                llshortbioet.setVisibility(View.VISIBLE);

            }
        });
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String,Object> map = new HashMap<>();
                map.put("name",etName.getText().toString());
                map.put("year",etYear.getText().toString());
                map.put("location",etLocation.getText().toString());
                map.put("shortBio",etShortBio.getText().toString());

                myRef.child(fAuth.getUid()).updateChildren(map);
                llshortbioet.setVisibility(View.GONE);
                llnameet.setVisibility(View.GONE);
                lllocationet.setVisibility(View.GONE);
                llyearet.setVisibility(View.GONE);
                llshortbiotv.setVisibility(View.VISIBLE);
                llnametv.setVisibility(View.VISIBLE);
                lllocationtv.setVisibility(View.VISIBLE);
                llyeartv.setVisibility(View.VISIBLE);
                Toast.makeText(getApplicationContext(),"Changes Saved",Toast.LENGTH_SHORT).show();

            }
        });

        ivEditInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llnametv.setVisibility(View.GONE);
                lllocationtv.setVisibility(View.GONE);
                llyeartv.setVisibility(View.GONE);

                llnameet.setVisibility(View.VISIBLE);
                lllocationet.setVisibility(View.VISIBLE);
                llyearet.setVisibility(View.VISIBLE);
            }
        });

        ivProfile = findViewById(R.id.ivEditProfile);
        userID = fAuth.getCurrentUser().getUid();

        fStore.collection("users").document(userID)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {

                            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(imgButton);

                                }
                            });
                        }
                    } else {
                        // Handle the error
                        Exception exception = task.getException();
                        // Log or display an error message
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
        final StorageReference fileRef = storageReference.child("users/" + fAuth.getCurrentUser().getUid() + "profile.jpg");

        // Load the image into a Bitmap
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Compress the image with reduced quality (adjust quality as needed)
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos); // Adjust the quality here (50 in this example)

        // Convert the compressed Bitmap to bytes
        byte[] data = baos.toByteArray();

        // Upload the compressed image to Firebase Storage
        UploadTask uploadTask = fileRef.putBytes(data);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle the successful upload
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        purl = String.valueOf(uri);
                        Map<String, Object> users = new HashMap<>();

                        Map<String, Object> users2 = new HashMap<>();
                        users2.put("imageURL",purl);
                        users.put("purl", purl);

                        try {
                            if (purl != null) {
                                myRef.child(fAuth.getUid().toString()).updateChildren(users);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(users2);
                            }

                        }catch (Exception e){}



                        Picasso.get().load(uri).into(imgButton);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload
                Toast.makeText(ProfileActivity.this, "Failed.", Toast.LENGTH_LONG).show();
            }});
    }

}