package com.example.friendship.BasicDetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.BasicDetailsActivity;
import com.example.friendship.MainActivity;
import com.example.friendship.R;
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
import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class DetailsActivity5 extends AppCompatActivity {

    ImageView tvNext,tvPrevious;
    String purl;
    ImageView imgButton,ivProfile;
    LottieAnimationView lottieAnimationView ;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("students");

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

                Map<String,Object> users = new HashMap<>();
                if(purl == null|| purl.isEmpty() ){
                    purl = "https://firebasestorage.googleapis.com/v0/b/tesla-members-record.appspot.com/o/friends-low-resolution-logo-color-on-transparent-background.png?alt=media&token=507e6418-5807-4439-b148-9015755a2213";
                }
                users.put("purl",purl);

                myRef.child(fAuth.getCurrentUser().getUid()).updateChildren(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Map <String,Object> map = new HashMap<>();
                        map.put("detailsGiven","1");
                        FirebaseFirestore fStore= FirebaseFirestore.getInstance();

                        fStore.collection("students").document(fAuth.getCurrentUser().getUid()).update(map);
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
                Toast.makeText(DetailsActivity5.this,"Failed.",Toast.LENGTH_LONG).show();
            }
        });


    }
}