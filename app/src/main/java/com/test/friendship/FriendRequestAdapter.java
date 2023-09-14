package com.test.friendship;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.test.friendship.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.test.friendship.Model.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.HashMap;
import java.util.Map;
public class FriendRequestAdapter extends FirebaseRecyclerAdapter<User,FriendRequestAdapter.userAdapterHolder> {

    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Connection");


    public FriendRequestAdapter(@NonNull FirebaseRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull User model) {

        try {

            holder.name.setText(model.getUsername());
            holder.branch.setText(model.getBranch());
            Glide.with(holder.profileImage.getContext()).load(model.getImageURL()).into(holder.profileImage);
            holder.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.accept.setVisibility(View.INVISIBLE);
                    holder.reject.setVisibility(View.VISIBLE);
                    Map<String,Object> map = new HashMap<>();
                    map.put("status","2");
                    Toast.makeText(holder.accept.getContext(), "Accepted",Toast.LENGTH_SHORT).show();
                    myRef.child(fAuth.getCurrentUser().getUid()).child(model.getId()).updateChildren(map);
                    myRef.child(model.getId()).child(fAuth.getCurrentUser().getUid()).updateChildren(map);
                }
            });
            holder.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.reject.setVisibility(View.INVISIBLE);
                    holder.accept.setVisibility(View.VISIBLE);
                    Map<String,Object> map = new HashMap<>();
                    map.put("status","0");
                    myRef.child(fAuth.getCurrentUser().getUid()).child(model.getId()).updateChildren(map);
                }
            });


        }catch (Exception e){
            Toast.makeText(holder.name.getContext(), "Something wrong in Events",Toast.LENGTH_SHORT).show();
        }





    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_friend_request,parent,false);
        return new userAdapterHolder(view);
    }

    public class userAdapterHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        ImageView accept,reject;
        TextView name,branch;


        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profileImage = itemView.findViewById(R.id.profile_image);
            name = itemView.findViewById(R.id.name);
            branch = itemView.findViewById(R.id.branch);
            accept = itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);

        }
    }
}
