package com.example.friendship;

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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import pl.droidsonroids.gif.GifImageView;

public class UserAdapter extends FirebaseRecyclerAdapter<UserModel,UserAdapter.userAdapterHolder> {


    public UserAdapter(@NonNull FirebaseRecyclerOptions<UserModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull UserModel model) {

        holder.name.setText(model.getName());
        holder.branch.setText(model.getBranch());
        holder.year.setText(model.getYear());
        holder.shortBio.setText(model.getShortBio());
        holder.gifImageView.setVisibility(Integer.parseInt(model.getPremium()));
        Glide.with(holder.profileImage.getContext()).load(model.getPurl()).into(holder.profileImage);

        holder.gifLove.setVisibility(View.GONE);
        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                holder.like.playAnimation();

                handler.postDelayed(new Runnable() {
                    public void run() {
                        Toast.makeText(v.getContext(), "You Liked: "+model.getName(), Toast.LENGTH_SHORT).show();
                        holder.like.pauseAnimation();

                    }
                }, 3500);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_single_row,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView shortBio;
        TextView name;
        TextView branch;
        TextView year;
        TextView btnlike;
        LottieAnimationView gifImageView,like;
        GifImageView gifLove;



        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileImage);
            shortBio = itemView.findViewById(R.id.shortBio);
            name = itemView.findViewById(R.id.name);
            branch=itemView.findViewById(R.id.branch);
            year = itemView.findViewById(R.id.year);
            gifImageView =itemView.findViewById(R.id.crown);
            gifLove = itemView.findViewById(R.id.gifLove);
            like = itemView.findViewById(R.id.like);
        }
    }
}
