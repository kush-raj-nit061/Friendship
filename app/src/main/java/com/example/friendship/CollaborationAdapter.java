package com.example.friendship;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.friendship.Model.Collaboration;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class CollaborationAdapter extends FirebaseRecyclerAdapter<Collaboration,CollaborationAdapter.userAdapterHolder> {




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
