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

public class NotificationAdapter extends FirebaseRecyclerAdapter<User,NotificationAdapter.userAdapterHolder> {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Connection");
    DatabaseReference chatRef = database.getReference("Chatlist");


    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull User model) {


        holder.name.setText(model.getUsername());
        holder.branch.setText(model.getBranch());
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.accept.setVisibility(View.INVISIBLE);
                holder.reject.setVisibility(View.VISIBLE);
                Map<String,Object> map = new HashMap<>();
                Map<String,Object> ch = new HashMap<>();
                Map<String,Object> cha = new HashMap<>();
                ch.put("id",model.getId());
                cha.put("id",fAuth.getUid());
                map.put("status","2");
                chatRef.child(fAuth.getUid()).child(model.getId()).updateChildren(ch);
                chatRef.child(model.getId()).child(fAuth.getUid()).updateChildren(cha);
                myRef.child(fAuth.getUid().toString()).child(model.getId()).updateChildren(map);
            }
        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.reject.setVisibility(View.INVISIBLE);
                holder.accept.setVisibility(View.VISIBLE);
                Map<String,Object> map = new HashMap<>();
                map.put("status","0");
                myRef.child(fAuth.getUid().toString()).child(model.getId()).updateChildren(map);
            }
        });

    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification__single_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView accept,reject;
        TextView name,branch;


        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);
            name = itemView.findViewById(R.id.name);
            branch = itemView.findViewById(R.id.branch);



        }
    }
}
