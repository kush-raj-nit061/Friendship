package com.test.friendship;

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


import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
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
import com.test.friendship.BasicDetails.DetailsActivity2;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.test.friendship.R;

import soup.neumorphism.NeumorphButton;

public class ProfileActivity extends AppCompatActivity {

    ImageView imgButton,ivProfile;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    LottieAnimationView progress;
    NeumorphButton btnEditMore;

    ImageView ivEditInfo,ivEditShortBio,ivEditHobbies,btBack;
    Button saveChanges;


    String userID;
    String purl;
    LinearLayout llnametv,llnameet,lllocationtv,lllocationet,llyeartv,llyearet,llshortbiotv,llshortbioet,llHobbieset,llHobbiestv;

    TextView tvShortBio,tvLocation,tvName,tvYear,userid,tvHobbies;
    EditText etShortBio,etLocation,etName,etYear,etHobbies;



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
        tvHobbies = findViewById(R.id.tvHobbies);
        tvLocation = findViewById(R.id.tvLocation);
        tvName = findViewById(R.id.tvName);
        tvYear = findViewById(R.id.tvYear);

        etYear = findViewById(R.id.etYear);
        etShortBio = findViewById(R.id.etShortBio);
        etHobbies =findViewById(R.id.etHobbies);
        etLocation = findViewById(R.id.etLocation);
        etName = findViewById(R.id.etName);

        imgButton = findViewById(R.id.ivAvatar);
        lllocationtv = findViewById(R.id.lllocationtv);
        llnametv = findViewById(R.id.llnametv);
        llyeartv = findViewById(R.id.llyeartv);
        progress = findViewById(R.id.progress);
        btnEditMore = findViewById(R.id.btnExtraDetails);


        llshortbiotv = findViewById(R.id.llshortbiotv);
        llHobbieset= findViewById(R.id.llHobbieset);
        llHobbiestv = findViewById(R.id.llHobbiestv);
        lllocationet = findViewById(R.id.lllocationet);
        llnameet = findViewById(R.id.llnameet);
        llyearet = findViewById(R.id.llyearet);
        llshortbioet = findViewById(R.id.llshortbioet);
        btBack = findViewById(R.id.bt_back);

        Intent i = getIntent();
        Glide.with(getApplicationContext()).load(i.getStringExtra("purl")).into(imgButton);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnEditMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, DetailsActivity2.class);
                startActivity(i);
            }
        });

        ivEditInfo = findViewById(R.id.ivEditInfo);
        ivEditShortBio = findViewById(R.id.ivEditShortDet);
        ivEditHobbies = findViewById(R.id.ivEditHobbies);
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
                    tvHobbies.setText(model.getHobbies());
                    etName.setText(model.getName());
                    etLocation.setText(model.getLocation());
                    etShortBio.setText(model.getShortBio());
                    etYear.setText(model.getYear());
                    etHobbies.setText(model.getHobbies());



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
        llHobbieset.setVisibility(View.GONE);


        ivEditShortBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llshortbiotv.setVisibility(View.GONE);
                llshortbioet.setVisibility(View.VISIBLE);

            }
        });
        ivEditHobbies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llHobbieset.setVisibility(View.VISIBLE);
                llHobbiestv.setVisibility(View.GONE);
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
                map.put("hobbies",etHobbies.getText().toString());

                myRef.child(fAuth.getUid()).updateChildren(map);
                llshortbioet.setVisibility(View.GONE);
                llnameet.setVisibility(View.GONE);
                lllocationet.setVisibility(View.GONE);
                llyearet.setVisibility(View.GONE);
                llshortbiotv.setVisibility(View.VISIBLE);
                llnametv.setVisibility(View.VISIBLE);
                lllocationtv.setVisibility(View.VISIBLE);
                llyeartv.setVisibility(View.VISIBLE);
                llHobbieset.setVisibility(View.GONE);
                llHobbiestv.setVisibility(View.VISIBLE);
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

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent openGallInt= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                startActivityForResult(openGallInt,1000);


                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfileActivity.this);


            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                progress.setVisibility(View.VISIBLE);
                Uri resultUri = result.getUri();
                uploadImageToFirebase(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
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
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos); // Adjust the quality here (50 in this example)

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
                                myRef.child(fAuth.getCurrentUser().getUid().toString()).updateChildren(users);
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(users2);
                            }

                        }catch (Exception e){}



                        Picasso.get().load(uri).into(imgButton);
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload
                progress.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, "Failed.", Toast.LENGTH_LONG).show();
            }});
    }

}