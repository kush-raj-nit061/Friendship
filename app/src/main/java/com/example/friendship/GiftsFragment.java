package com.example.friendship;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.BasicDetails.DetailsActivity5;
import com.example.friendship.Model.Status;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GiftsFragment extends Fragment {

    RecyclerView recFeatured1;
    RecyclerView recFeatured2;
    RecyclerView recStatus;
    FeaturedAdapter userAdapter;
    FeaturedAdapter userAdapter2;
    StatusAdapter userAdapter3;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students");
    StorageReference storageReference = storage.getReference();
    DatabaseReference dbsl = FirebaseDatabase.getInstance().getReference("StatusLiked");


    String postUrl="";

    LinearLayoutManager manager;
    LinearLayoutManager manager2;
    LinearLayoutManager manager3;
    LottieAnimationView addStatus;

    public GiftsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_gifts_fragment, container, false);


        recFeatured1  = view.findViewById(R.id.recFeatured1);
        recFeatured2  = view.findViewById(R.id.recFeatured2);
        recStatus  = view.findViewById(R.id.recStatus);
        addStatus = view.findViewById(R.id.animation);

        manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        manager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        manager3 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        recFeatured1.setLayoutManager(manager);
        recFeatured2.setLayoutManager(manager2);
        recStatus.setLayoutManager(manager3);
//        recFeatured.setHasFixedSize(true);
        recFeatured1.setItemAnimator(null);
        recFeatured2.setItemAnimator(null);
        recStatus.setItemAnimator(null);



        try {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference("Status");
            db.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if(snapshot.exists()){
                        String dateStr = (String) snapshot.child("date").getValue();
                        long timestamp = Long.parseLong(dateStr);
                        Date date = new Date(timestamp);
                        Date currentDate = new Date();
                        // Calculate the time difference in milliseconds
                        long timeDifferenceMillis = currentDate.getTime() - date.getTime();
                        // Convert milliseconds to hours
                        long hoursAgo = timeDifferenceMillis / (1000 * 60 * 60);
                        // Set the calculated time difference as text

                        if(hoursAgo>24){
                            DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("Status");
                            String key = snapshot.getKey();
                            dbs.child(key).removeValue();
                            dbsl.child(key).removeValue();

                        }

                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        }catch (Exception e){
            Toast.makeText(getContext(),"Check Your Network Connections",Toast.LENGTH_SHORT).show();

        }


        DatabaseReference dbre = FirebaseDatabase.getInstance().getReference("Status");
        dbre.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long count = snapshot.getChildrenCount();

                Query q = dbre.orderByChild("date"); // Assuming "date" is the key in each child containing the timestamp
                FirebaseRecyclerOptions<Status> options3 =
                        new FirebaseRecyclerOptions.Builder<Status>()
                                .setQuery(q, Status.class)
                                .build();
                userAdapter3 = new StatusAdapter(options3);
                recStatus.setAdapter(userAdapter3);
                userAdapter3.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });


//        FirebaseRecyclerOptions<Status> options3 =
//                new FirebaseRecyclerOptions.Builder<Status>()
//                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Status"), Status.class)
//                        .build();
//        userAdapter3=new StatusAdapter(options3);
//        recStatus.setAdapter(userAdapter3);
//        userAdapter3.startListening();




        FirebaseRecyclerOptions<FeaturedModel> options =
                new FirebaseRecyclerOptions.Builder<FeaturedModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Featured"), FeaturedModel.class)
                        .build();
        userAdapter=new FeaturedAdapter(options);
        recFeatured1.setAdapter(userAdapter);

        userAdapter.startListening();

        FirebaseRecyclerOptions<FeaturedModel> options2 =
                new FirebaseRecyclerOptions.Builder<FeaturedModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CoolFeatured"), FeaturedModel.class)
                        .build();
        userAdapter2=new FeaturedAdapter(options2);
        recFeatured2.setAdapter(userAdapter2);
        userAdapter2.startListening();



        addStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent openGallInt= new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallInt,1000);







            }
        });

        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        final StorageReference fileRef = storageReference.child("Posts/" + fAuth.getCurrentUser().getUid() + "profile.jpg");

        // Load the image into a Bitmap
        Bitmap bitmap;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
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
                        postUrl = String.valueOf(uri);
                        if(!(postUrl.isEmpty())){

                            reference.child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("StatusLiked");
                                        dbs.child(fAuth.getCurrentUser().getUid()).removeValue();
                                        String purl = (String) snapshot.child("purl").getValue();
                                        String name = (String) snapshot.child("name").getValue();
                                        Map<String,Object> map = new HashMap<>();
                                        map.put("purl",purl);
                                        map.put("postUrl",postUrl);
                                        Date date = new Date();
                                        map.put("date",String.valueOf(date.getTime()));
                                        map.put("id",fAuth.getCurrentUser().getUid());
                                        map.put("name",name);
                                        map.put("postlikes","0");

                                        DatabaseReference db = FirebaseDatabase.getInstance().getReference("Status").child(fAuth.getCurrentUser().getUid());
                                        db.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                try {
                                                    Toast.makeText(getContext(),"Memo Added",Toast.LENGTH_SHORT).show();
                                                }catch (Exception e){}

                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });



                        }


                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle the failure to upload
                Toast.makeText(getContext(), "Failed.", Toast.LENGTH_LONG).show();
            }});
    }
}