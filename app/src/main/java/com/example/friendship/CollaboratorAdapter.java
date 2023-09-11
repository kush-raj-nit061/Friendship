package com.example.friendship;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.example.friendship.Model.Celebration;
import com.example.friendship.Model.Collaborator;
import com.example.friendship.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
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

public class CollaboratorAdapter extends FirebaseRecyclerAdapter<Collaborator,CollaboratorAdapter.userAdapterHolder> {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Celebration");


    public CollaboratorAdapter(@NonNull FirebaseRecyclerOptions<Collaborator> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull Collaborator model) {

        try {

            DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("students").child(model.getId());
            dbs.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String name = (String) snapshot.child("name").getValue();
                    String branch = (String) snapshot.child("branch").getValue();
                    String purl = (String) snapshot.child("purl").getValue();

                    holder.name.setText(name);
                    holder.branch.setText(branch);
                    Glide.with(holder.profile.getContext()).load(purl).into(holder.profile);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });


        }catch (Exception e){
            Toast.makeText(holder.name.getContext(), "Something wrong in Celebration",Toast.LENGTH_SHORT).show();
        }



    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collaborator_item,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name,branch;


        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            branch = itemView.findViewById(R.id.branch);



        }
    }
}
