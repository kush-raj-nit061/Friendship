package com.test.friendship.Admins;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.test.friendship.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AnnouncementActivity extends AppCompatActivity {


    EditText eventname,eventsubtitle,eventdate,eventmonth,eventdescription,link1,link2,link3;
    String purl = "";
    ImageView ivPoster;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    Button btnSet;
    LottieAnimationView progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcement);
        eventdate = findViewById(R.id.eventdate);
        eventname = findViewById(R.id.eventname);
        eventsubtitle = findViewById(R.id.eventsubtitle);
        eventmonth = findViewById(R.id.eventmonth);
        eventdescription = findViewById(R.id.eventdiscription);
        link1 = findViewById(R.id.eventlink1);
        link2 = findViewById(R.id.eventlink2);
        link3 = findViewById(R.id.eventlink3);
        btnSet = findViewById(R.id.btnSet);
        ivPoster = findViewById(R.id.ivAnnouncements);
        progress = findViewById(R.id.progress);

        ivPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGallInt= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallInt,1000);
            }
        });


        btnSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date,month,name,title,description;

                date = eventdate.getText().toString().trim();
                month = eventmonth.getText().toString().trim();
                name = eventname.getText().toString().trim();
                title = eventsubtitle.getText().toString().trim();
                description = eventdescription.getText().toString().trim();

                if(purl.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Please Add Poster of Event",Toast.LENGTH_SHORT).show();
                } else if (eventdate.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Add date of Event",Toast.LENGTH_SHORT).show();
                } else if (month.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Add Month of Event",Toast.LENGTH_SHORT).show();
                }else if (name.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Add name of Event",Toast.LENGTH_SHORT).show();
                }else if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Add subtitle of Event",Toast.LENGTH_SHORT).show();
                }else if (description.isEmpty()) {
                    Toast.makeText(getApplicationContext(),"Please Add description of Event",Toast.LENGTH_SHORT).show();
                }else{
                    Map<String,Object> map = new HashMap<>();
                    map.put("eventname",name);
                    map.put("eventtitle",title);
                    map.put("month",month);

                    if(date.trim().length() == 1){
                        date = "0"+date;
                        map.put("date",date);
                    } else if (date.trim().length() == 2) {
                        map.put("date",date);
                    }else{
                        Toast.makeText(getApplicationContext(),"Enter a valid date",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    map.put("eventpurl",purl);
                    map.put("description",description);
                    if(!link1.getText().toString().isEmpty()){
                        map.put("link1",link1.getText().toString());
                    }if(!link2.getText().toString().isEmpty()){
                        map.put("link2",link2.getText().toString());
                    }if(!link3.getText().toString().isEmpty()){
                        map.put("link3",link3.getText().toString());
                    }


                    DatabaseReference notifRef = FirebaseDatabase.getInstance().getReference().child("Events");

                    notifRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getApplicationContext(),"Notification Added",Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Something Wrong",Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1000){
            if(resultCode == Activity.RESULT_OK){
                progress.setVisibility(View.VISIBLE);
                Uri imageUri =data.getData();

//                imgButton.setImageURI(imageUri);
//                progressBar.setVisibility(View.VISIBLE);

                uploadImageToFirebase(imageUri);
            }
        }


    }
    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("Posters/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "profile.jpg");

        // Load the image into a Bitmap
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (IOException e) {

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
                        Picasso.get().load(uri).into(ivPoster);
                        progress.setVisibility(View.GONE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload
                progress.setVisibility(View.GONE);
                Toast.makeText(AnnouncementActivity.this, "Failed.", Toast.LENGTH_LONG).show();
            }});
    }
}


