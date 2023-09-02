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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class FeaturedAdapter extends FirebaseRecyclerAdapter<FeaturedModel,FeaturedAdapter.userAdapterHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();


    public FeaturedAdapter(@NonNull FirebaseRecyclerOptions<FeaturedModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull FeaturedModel model) {

        holder.name.setText(model.getName());
        holder.name1.setText(model.getName());
        holder.branch.setText(model.getBranch());
        holder.branch1.setText(model.getBranch());
        holder.shortBio.setText(model.getShortBio());
        holder.year.setText(model.getYear());
        Glide.with(holder.profileImage.getContext()).load(model.getPurl()).into(holder.profileImage);
        holder.medal.setAnimationFromUrl(model.getMedallottie());
        holder.medal1.setAnimationFromUrl(model.getMedallottie());

    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.featured_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView shortBio;
        TextView name;
        TextView name1;
        TextView branch;
        TextView branch1;
        TextView year;
        LottieAnimationView medal;
        LottieAnimationView medal1;




        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.purl);
            shortBio = itemView.findViewById(R.id.shortBio);
            name = itemView.findViewById(R.id.name);
            name1 = itemView.findViewById(R.id.name1);
            branch=itemView.findViewById(R.id.branch);
            branch1=itemView.findViewById(R.id.branch1);
            year = itemView.findViewById(R.id.year);
            medal = itemView.findViewById(R.id.medal);
            medal1 = itemView.findViewById(R.id.medal1);

        }
    }
}
