package com.test.friendship;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.test.friendship.R;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.friendship.Model.Developer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
public class DeveloperAdapter extends FirebaseRecyclerAdapter<Developer,DeveloperAdapter.userAdapterHolder> {



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
