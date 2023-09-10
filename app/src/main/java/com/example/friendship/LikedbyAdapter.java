package com.example.friendship;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
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
import com.example.friendship.Model.Likedby;
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

public class LikedbyAdapter extends FirebaseRecyclerAdapter<Likedby,LikedbyAdapter.userAdapterHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();


    public LikedbyAdapter(@NonNull FirebaseRecyclerOptions<Likedby> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Likedby model) {

        try {
            dbRef.child("students").child(model.getUserId()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String prof = (String) snapshot.child("purl").getValue();
                        Glide.with(holder.profileImage.getContext()).load(prof).into(holder.profileImage);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Glide.with(holder.profileImage.getContext()).load(model.getPurl()).into(holder.profileImage);
        }




        holder.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("students").child(model.getUserId());
                db.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            UserModel model1 = (UserModel) snapshot.getValue(UserModel.class);

                            AppCompatActivity appCompatActivity = unwrap(v.getContext());
                            appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                                    new UserDescrFragment(model.getUserId(),model1.getName(),model1.getBranch(),model1.getYear()
                                            ,model1.getShortBio(),model1.getPurl(),model1.getHobbies(),model1.getBirthday()
                                            ,model1.getQualitylike(),model1.getQualitydislike(),model1.getFoods()
                                            ,model1.getBooks(),model1.getTravellike())).addToBackStack(null).commit();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }
        });



    }

    private static AppCompatActivity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        assert context instanceof AppCompatActivity;
        return (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.likedby_single,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {

        CircleImageView profileImage;





        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_images);

        }
    }
}
