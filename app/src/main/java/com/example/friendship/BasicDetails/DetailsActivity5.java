package com.example.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.example.friendship.MainActivity;
import com.example.friendship.R;
import com.example.friendship.UserModel;
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
    DatabaseReference myRef = database.getReference("unregistered");
    DatabaseReference registered= database.getReference("students");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details5);
        ivProfile = findViewById(R.id.ivProfile);
        imgButton = findViewById(R.id.ivProfilee);
        tvPrevious = findViewById(R.id.tvPrevious);

        tvNext = findViewById(R.id.tvNext);

        lottieAnimationView = findViewById(R.id.lottie_animation);
        lottieAnimationView.playAnimation();

        tvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Map<String,Object> data = new HashMap<>();

                Map<String,Object> users = new HashMap<>();

                if(purl == null|| purl.isEmpty() ){
                    purl = "https://firebasestorage.googleapis.com/v0/b/friendship-c4818.appspot.com/o/friends-low-resolution-logo-color-on-transparent-background.png?alt=media&token=a97a58a1-21e2-4e3d-8256-8c95b3a894a3";
                    Toast.makeText(getApplicationContext(),"You have set your profile image to default",Toast.LENGTH_SHORT).show();
                }
                users.put("purl",purl);
                data.put("imageURL",purl);


                reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getUid());
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
                registered.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
//                            myRef.child(fAuth.getCurrentUser().getUid()).removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                Intent i = new Intent(DetailsActivity5.this, MainActivity.class);
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
        tvPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DetailsActivity5.this,DetailsActivity4.class);
                startActivity(i);
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
                        users.put("purl", purl);

                        if (purl != null) {
                            myRef.child(fAuth.getUid().toString()).updateChildren(users);
                        }

                        Picasso.get().load(uri).into(ivProfile);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload
                Toast.makeText(DetailsActivity5.this, "Failed.", Toast.LENGTH_LONG).show();
            }});
    }
}