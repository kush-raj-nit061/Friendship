package com.example.friendship;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.example.friendship.Model.Events;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class EventsAdapter extends FirebaseRecyclerAdapter<Events,EventsAdapter.userAdapterHolder> {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Connection");





    public EventsAdapter(@NonNull FirebaseRecyclerOptions<Events> options) {
        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Events model) {

        holder.date.setText(model.getDate());
        holder.month.setText(model.getMonth());
        holder.description.setText(model.getDescription());
        holder.eventname.setText(model.getEventname());
        holder.eventtitle.setText(model.getEventtitle());
        holder.eventtitle.setText(model.getMonth());
        Glide.with(holder.eventpurl.getContext()).load(model.getEventpurl()).into(holder.eventpurl);
        String link1,link2,link3;
        link1 = model.getLink1();
        link2 = model.getLink2();
        link3 = model.getLink3();
        if(!(link1 ==null)){
            holder.link1.setVisibility(View.VISIBLE);
            holder.link1.setText(link1);
        }if(!(link2 ==null)){
            holder.link2.setVisibility(View.VISIBLE);
            holder.link2.setText(link2);
        }if(!(link3 ==null)){
            holder.link3.setVisibility(View.VISIBLE);
            holder.link3.setText(link3);
        }
        holder.eventpurl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context cont = v.getContext();
                Intent i = new Intent(cont, FullProfileLoader.class);
                i.putExtra("purl",model.getEventpurl());
                cont.startActivity(i);

            }
        });


    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView eventpurl;
        TextView eventname,eventtitle,link1,link2,link3,description,date,month;


        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            eventpurl = itemView.findViewById(R.id.eventpurl);
            eventname = itemView.findViewById(R.id.tvTitle);
            eventtitle = itemView.findViewById(R.id.tvSubTitle);
            link1 = itemView.findViewById(R.id.link1);
            link2 = itemView.findViewById(R.id.link2);
            link3 = itemView.findViewById(R.id.link3);
            month = itemView.findViewById(R.id.tvLession);
            date = itemView.findViewById(R.id.tvDate);
            description = itemView.findViewById(R.id.description);





        }
    }
}
