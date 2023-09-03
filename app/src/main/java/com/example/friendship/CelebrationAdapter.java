package com.example.friendship;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.example.friendship.Model.Celebration;
import com.example.friendship.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.rolud.solidglowanimation.SolidGlowAnimation;
import java.util.HashMap;
import java.util.Map;
import pl.droidsonroids.gif.GifImageView;

public class CelebrationAdapter extends FirebaseRecyclerAdapter<Celebration,CelebrationAdapter.userAdapterHolder> {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Celebration");


    public CelebrationAdapter(@NonNull FirebaseRecyclerOptions<Celebration> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Celebration model) {

        try {

            holder.img2.setAnimationFromUrl(model.getLottie2());
            holder.imgFire.setAnimationFromUrl(model.getLottie1());
            Glide.with(holder.circleImage.getContext()).load(model.getPurl()).into(holder.circleImage);
            Glide.with(holder.fullImage.getContext()).load(model.getPurl()).into(holder.fullImage);
            holder.eventName.setText(model.getEventname());
            holder.userName.setText(model.getUsername());

            holder.imgFire.setVisibility(View.INVISIBLE);
            holder.arrowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.imgFire.playAnimation();
                    holder.imgFire.setVisibility(View.VISIBLE);
                }
            });
            holder.imgFire.setVisibility(View.INVISIBLE);


        }catch (Exception e){
            Toast.makeText(holder.eventName.getContext(), "Something wrong in Celebration",Toast.LENGTH_SHORT).show();
        }



    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_celebration_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        Button arrowBtn;
        CardView cardView;
        LottieAnimationView img2,imgFire;
        ImageView circleImage,fullImage;
        TextView eventName,userName;


        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            imgFire = itemView.findViewById(R.id.skyshot);
            circleImage = itemView.findViewById(R.id.circleImage);
            fullImage = itemView.findViewById(R.id.imageView);
            eventName = itemView.findViewById(R.id.eventName);
            userName=  itemView.findViewById(R.id.username);
            arrowBtn = itemView.findViewById(R.id.arrowBtn);
            cardView = itemView.findViewById(R.id.cardView);
            img2 = itemView.findViewById(R.id.imageView2);

        }
    }
}
