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
import com.example.friendship.Model.Developer;
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

public class DeveloperAdapter extends FirebaseRecyclerAdapter<Developer,DeveloperAdapter.userAdapterHolder> {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference();


    public DeveloperAdapter(@NonNull FirebaseRecyclerOptions<Developer> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Developer model) {


        try {

            holder.name.setText(model.getName());
            holder.description.setText(model.getDescription());
            holder.post.setText(model.getPost());
            Glide.with(holder.profileImage.getContext()).load(model.getPurl()).into(holder.profileImage);


        }catch (Exception e){
            Toast.makeText(holder.name.getContext(), "Something wrong in Developer",Toast.LENGTH_SHORT).show();
        }






    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.developer_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView description;
        TextView name,post;





        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profileimg);

            name = itemView.findViewById(R.id.Name);
            post = itemView.findViewById(R.id.post);
            description = itemView.findViewById(R.id.descr);


        }
    }
}
