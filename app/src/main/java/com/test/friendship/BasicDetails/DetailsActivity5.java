package com.test.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.DocumentSnapshot;
import com.test.friendship.MainActivity;
import com.test.friendship.ProfileActivity;
import com.test.friendship.R;
import com.test.friendship.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DetailsActivity5 extends AppCompatActivity {

    ImageView tvNext,tvPrevious;
    String purl;
    ImageView imgButton,ivProfile;
    LottieAnimationView lottieAnimationView ;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference reference;
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String detailsGiven,purls;
    DatabaseReference myRef = database.getReference("unregistered");
    DatabaseReference registered= database.getReference("students");
    LottieAnimationView progress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details5);
        ivProfile = findViewById(R.id.ivProfile);
        imgButton = findViewById(R.id.ivProfilee);
        tvPrevious = findViewById(R.id.tvPrevious);

        tvNext = findViewById(R.id.tvNext);
        progress = findViewById(R.id.progress);

        lottieAnimationView = findViewById(R.id.lottie_animation);
        lottieAnimationView.playAnimation();


        try {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("students");
            db.child(fAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                       purls = (String) snapshot.child("purl").getValue();
                        try {
                            Glide.with(getApplicationContext()).load(purls).into(ivProfile);
                        }catch (Exception e){}


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            FirebaseFirestore fStore = FirebaseFirestore.getInstance();
            FirebaseAuth fAuth = FirebaseAuth.getInstance();
            fStore.collection("users").document(fAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if(document.exists()){
                            detailsGiven = document.getString("detailsGiven");

                        }
                    }

                }
            });
        }catch (Exception e){}

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String,Object> data = new HashMap<>();
                Map<String,Object> users = new HashMap<>();

                if(detailsGiven.equals("1")){
                    Intent i = new Intent(DetailsActivity5.this,ProfileActivity.class);
                    i.putExtra("purl",purls);
                    startActivity(i);
                    finish();

                }else {
                    if(purl == null|| purl.isEmpty() ){
                        purl = "https://firebasestorage.googleapis.com/v0/b/friendship-c4818.appspot.com/o/friends-low-resolution-logo-color-on-transparent-background.png?alt=media&token=a97a58a1-21e2-4e3d-8256-8c95b3a894a3";
                        Toast.makeText(getApplicationContext(),"You have set your profile image to default",Toast.LENGTH_SHORT).show();
                    }
                    users.put("purl",purl);
                    data.put("imageURL",purl);


                    reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    reference.updateChildren(data);

                    myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Map <String,Object> map = new HashMap<>();
                            map.put("detailsGiven","1");
                            FirebaseFirestore fStore= FirebaseFirestore.getInstance();

                            fStore.collection("users").document(fAuth.getCurrentUser().getUid()).update(map);
                        }
                    });

                    myRef.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            UserModel model = (UserModel) snapshot.getValue(UserModel.class);
                            registered.child(fAuth.getCurrentUser().getUid()).setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Intent i = new Intent(DetailsActivity5.this, MainActivity.class);
                                    startActivity(i);
                                    finish();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }

            }
        });

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(DetailsActivity5.this);
            }
        });

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(DetailsActivity5.this);
            }
        });
        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity5.this,DetailsActivity4.class);
                startActivity(i);
                finish();
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
                        users.put("purl", purl);

                        if(detailsGiven.equals("1")){
                            registered.child(fAuth.getCurrentUser().getUid()).updateChildren(users);

                        }else {
                            if (purl != null) {
                                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users);
                            }
                        }



                        Picasso.get().load(uri).into(ivProfile);
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload
                Toast.makeText(DetailsActivity5.this, "Failed.", Toast.LENGTH_LONG).show();
                progress.setVisibility(View.GONE);
            }});
    }
}