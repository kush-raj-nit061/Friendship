package com.test.friendship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.devlomi.circularstatusview.CircularStatusView;
import com.test.friendship.Model.Status;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import pl.droidsonroids.gif.GifImageView;
import com.test.friendship.R;

public class StatusAdapter extends FirebaseRecyclerAdapter<Status,StatusAdapter.userAdapterHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    DatabaseReference dbr = FirebaseDatabase.getInstance().getReference("StatusLiked");


    public StatusAdapter(@NonNull FirebaseRecyclerOptions<Status> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, @SuppressLint("RecyclerView") int position, @NonNull Status model) {

        Intent i = new Intent(holder.imgprofile.getContext(),StoryFullView.class);
        i.putExtra("purl",model.getPurl());
        i.putExtra("date",model.getDate());
        i.putExtra("posturl",model.getPostUrl());
        i.putExtra("id",model.getId());
        i.putExtra("name",model.getName());
        i.putExtra("likes",model.getPostlikes());
        i.putExtra("position",String.valueOf(position));
        i.putExtra("itemCount",String.valueOf(getItemCount()));

        try {
            dbRef.child("students").child(model.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String prof = (String) snapshot.child("purl").getValue();
                        Glide.with(holder.imgprofile.getContext()).load(prof).into(holder.imgprofile);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


            try {

                dbr.child(model.getId()).child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.child(fAuth.getCurrentUser().getUid()).exists()){
                            holder.cvStatusOut.setVisibility(View.INVISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }catch (Exception e){}


            holder.imgprofile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Map<String,Object> map = new HashMap<>();
                    map.put(fAuth.getCurrentUser().getUid(),"seen");
                    dbr.child(model.getId()).child(fAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("status").exists()){
                                if(snapshot.child("status").getValue().equals("liked")){
                                    map.put("status","liked");
                                    dbr.child(model.getId()).child(fAuth.getCurrentUser().getUid()).setValue(map);
                                }else{
                                    map.put("status","unliked");
                                    dbr.child(model.getId()).child(fAuth.getCurrentUser().getUid()).setValue(map);
                                }


                            }else {
                                map.put("status","unliked");
                                dbr.child(model.getId()).child(fAuth.getCurrentUser().getUid()).setValue(map);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    holder.cvStatusOut.setVisibility(View.INVISIBLE);

                    holder.imgprofile.getContext().startActivity(i);
                }
            });




        }catch (Exception e){
            Toast.makeText(holder.imgprofile.getContext(), "Something wrong in Status",Toast.LENGTH_SHORT).show();
        }



    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.story_single,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {

        CircleImageView imgprofile;
        CircularStatusView cvStatusOut;



        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            imgprofile = itemView.findViewById(R.id.profile);
            cvStatusOut = itemView.findViewById(R.id.circular_status_view);

        }
    }
}
