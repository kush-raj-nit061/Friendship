package com.test.friendship;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
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
import sharelayoutbyamit.example.sharelibrary.ShareLayout;

import com.makeramen.roundedimageview.RoundedImageView;
import com.test.friendship.R;

public class UserAdapter extends FirebaseRecyclerAdapter<UserModel,UserAdapter.userAdapterHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();

    DatabaseReference myRef = database.getReference("Connection");
    String myPurl;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();;


    public UserAdapter(@NonNull FirebaseRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull UserModel model) {

        holder.name.setText(model.getName());
        holder.branch.setText(model.getBranch());
        holder.year.setText(model.getYear());
        holder.shortBio.setText(model.getShortBio());
        holder.branch.setSelected(true);
        holder.name.setSelected(true);
        holder.tvCollege.setSelected(true);


        holder.tvCollege.setText(model.getCollege());
        Glide.with(holder.profileImage.getContext()).load(model.getPurl()).into(holder.profileImage);

        if(model.getUserId().equals(fAuth.getUid())){
            holder.button.setVisibility(View.INVISIBLE);
        }

        try {
            myRef.child(model.getUserId()).child(fAuth.getUid().toString()).child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            try {
                                if(snapshot.getValue().equals("0")){
                                    holder.button.setText("Align");
//                                    card1.setVisibility(View.INVISIBLE);

                                } else if (snapshot.getValue().equals("1")) {
                                    holder.button.setText("Requested");

                                } else if(snapshot.getValue().equals("2")){
                                    holder.button.setText("Diverge");

                                }

                            } catch (Exception e) {

                            }
                        } else {
                            holder.button.setText("Align");
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }catch (Exception e){}


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    myRef.child(model.getUserId()).child(fAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            Map<String,Object> map = new HashMap<>();
                            if(snapshot.child("status").exists()){
                                if(snapshot.child("status").getValue().equals("0")){
                                    Toast.makeText(v.getContext(),"Requested:}",Toast.LENGTH_SHORT).show();
                                    holder.button.setText("Requested");
                                    map.put("status","1");
                                    myRef.child(model.getUserId()).child(fAuth.getUid()).updateChildren(map);

                                } else if (snapshot.child("status").getValue().equals("1")) {
                                    holder.button.setText("Align");
                                    Toast.makeText(v.getContext(),"Request retrieved:{",Toast.LENGTH_SHORT).show();
                                    map.put("status","0");
                                    myRef.child(model.getUserId()).child(fAuth.getUid()).updateChildren(map);
                                }else{
                                    Toast.makeText(v.getContext(),"Unaligned",Toast.LENGTH_SHORT).show();
                                    holder.button.setText("Align");
                                    map.put("status","0");
                                    myRef.child(model.getUserId()).child(fAuth.getUid()).updateChildren(map);
                                    myRef.child(fAuth.getUid()).child(model.getUserId()).updateChildren(map);
                                }

                            }else {
                                Toast.makeText(v.getContext(),"Requested:}",Toast.LENGTH_SHORT).show();
                                holder.button.setText("Requested");
                                map.put("status","1");

                                myRef.child(model.getUserId()).child(fAuth.getUid()).updateChildren(map);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }catch (Exception e){}



            }
        });





        try{
        dbRef.child("Likes").child(model.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Map<String,Object> map  = new HashMap<>();
                holder.like.setText(String.valueOf(snapshot.getChildrenCount()));
                map.put("likes",String.valueOf(snapshot.getChildrenCount()));
                dbRef.child("students").child(model.getUserId()).updateChildren(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });}catch (Exception e){}
        try {
            dbRef.child("Likes").child(model.getUserId()).child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        holder.like.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_red_heart_svgrepo_com, 0, 0, 0);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){}

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareLayout.simpleLayoutShare(v.getContext(), holder.card,"Here is the profile of "+model.getName());
            }
        });



        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
//                holder.like.playAnimation();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        Map<String,Object> map  = new HashMap<>();
                        FirebaseAuth fAuth = FirebaseAuth.getInstance();
                        try {
                            dbRef.child("students").child(fAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists()){
                                        String purl = (String) snapshot.child("purl").getValue();
                                        map.put("userId",fAuth.getCurrentUser().getUid());
                                        map.put("purl",purl);
                                        try {


                                            dbRef.child("Likes").child(model.getUserId()).child(fAuth.getCurrentUser().getUid()).updateChildren(map);


                                        }catch (Exception ignored){}
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });


                        }catch (Exception ignored){}



                        Toast.makeText(v.getContext(), "You Liked: "+model.getName(), Toast.LENGTH_SHORT).show();
//                        holder.like.pauseAnimation();

                    }
                }, 0);

            }
        });


        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                        new UserDescrFragment(model.getUserId(),model.getName(),model.getBranch(),model.getYear()
                                ,model.getShortBio(),model.getPurl(),model.getHobbies(),model.getBirthday()
                                ,model.getQualitylike(),model.getQualitydislike(),model.getFoods()
                                ,model.getBooks(),model.getTravellike())).addToBackStack(null).commit();
            }
        });

    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_rv,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        RoundedImageView profileImage;
        TextView shortBio;
        TextView name;
        TextView branch;
        TextView year;
        TextView like,share,button;

        TextView tvCollege;
        CardView card;



        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.postImg);
            shortBio = itemView.findViewById(R.id.postDescription);
            name = itemView.findViewById(R.id.name);
            branch=itemView.findViewById(R.id.branch);
            year = itemView.findViewById(R.id.year);
            like = itemView.findViewById(R.id.like);
            tvCollege = itemView.findViewById(R.id.college);
            share = itemView.findViewById(R.id.share);
            share = itemView.findViewById(R.id.share);
            card = itemView.findViewById(R.id.card);
            button = itemView.findViewById(R.id.saves);
        }
    }
}
