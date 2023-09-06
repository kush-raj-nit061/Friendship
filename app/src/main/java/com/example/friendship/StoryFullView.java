package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StoryFullView extends AppCompatActivity {
    ImageView image,next,prev,cross,liked;
    LottieAnimationView likes;
    CircleImageView profile;
    TextView tvName, tvDate,likesCount;
    DatabaseReference reference;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("StatusLiked");
    FirebaseAuth fAuth = FirebaseAuth.getInstance();


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_full_view);
        image = findViewById(R.id.image);
        next = findViewById(R.id.next);
        prev = findViewById(R.id.prev);
        likes = findViewById(R.id.likes);

        profile = findViewById(R.id.profile);
        tvDate = findViewById(R.id.date);
        tvName = findViewById(R.id.name);
        likesCount = findViewById(R.id.likesCount);
        cross = findViewById(R.id.cross);
        liked = findViewById(R.id.liked);

        likes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likes.playAnimation();
                liked.setVisibility(View.INVISIBLE);

                Intent in = getIntent();
                String ids = in.getStringExtra("id");

                Map<String,Object> map = new HashMap<>();
                map.put("status","liked");
                dbRef.child(ids).child(fAuth.getCurrentUser().getUid()).updateChildren(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(),"Post Liked",Toast.LENGTH_SHORT).show();
                    }
                });

                Handler handler = new Handler();



                handler.postDelayed(new Runnable() {
                    public void run() {
                        liked.setVisibility(View.VISIBLE);

                    }
                }, 10000);

            }
        });
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = getIntent();
        String url = i.getStringExtra("posturl");
        String purl = i.getStringExtra("purl");
        String dateStr = i.getStringExtra("date");
        String name = i.getStringExtra("name");
        String id = i.getStringExtra("id");
        String likes = i.getStringExtra("likes");
        String position = i.getStringExtra("position");

        long timestamp = Long.parseLong(dateStr);
        Date date = new Date(timestamp);
        Date currentDate = new Date();
            // Calculate the time difference in milliseconds
        long timeDifferenceMillis = currentDate.getTime() - date.getTime();
            // Convert milliseconds to hours
        long hoursAgo = timeDifferenceMillis / (1000 * 60 * 60);
            // Set the calculated time difference as text
        tvDate.setText(String.valueOf(hoursAgo) + " hours ago");
        likesCount.setText(likes);

        tvName.setText(name);
        Glide.with(getApplicationContext()).load(url).into(image);
        Glide.with(getApplicationContext()).load(purl).into(profile);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("StatusLiked").child(id);
        Query query = reference.orderByChild("status").equalTo("liked");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    long count = dataSnapshot.getChildrenCount();
                    likesCount.setText(String.valueOf(count));
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });


    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {
        super.onResume();
        status("online");

    }

    @Override
    protected void onPause() {
        super.onPause();
//        reference.removeEventListener(seenListener);
        status("offline");

    }
}