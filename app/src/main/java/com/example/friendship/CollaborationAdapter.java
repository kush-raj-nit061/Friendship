package com.example.friendship;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.example.friendship.Model.Collaboration;
import com.example.friendship.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rolud.solidglowanimation.SolidGlowAnimation;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class CollaborationAdapter extends FirebaseRecyclerAdapter<Collaboration,CollaborationAdapter.userAdapterHolder> {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Connection");


    public CollaborationAdapter(@NonNull FirebaseRecyclerOptions<Collaboration> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Collaboration model) {

        Glide.with(holder.image.getContext()).load(model.getPurl()).into(holder.image);
        holder.projectName.setText(model.getProjectname());
        holder.projectType.setText(model.getProjecttype());
        holder.seeMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Intent i = new Intent(cont, SeeCollaborationDetail.class);
                i.putExtra("purl",model.getPurl());
                i.putExtra("date",model.getDate());
                i.putExtra("projectname",model.getProjectname());
                i.putExtra("description",model.getDescription());
                i.putExtra("requirement",model.getRequirement());
                i.putExtra("projecttype",model.getProjecttype());
                i.putExtra("id",model.getId());
                cont.startActivity(i);
            }
        });




    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collab_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView projectName,projectType;
        Button seeMore;



        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            projectName = itemView.findViewById(R.id.projectName);
            projectType = itemView.findViewById(R.id.projectType);
            seeMore = itemView.findViewById(R.id.seeMore);


        }
    }
}
