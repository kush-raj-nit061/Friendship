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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
public class FeaturedAdapter extends FirebaseRecyclerAdapter<FeaturedModel,FeaturedAdapter.userAdapterHolder> {




    public FeaturedAdapter(@NonNull FirebaseRecyclerOptions<FeaturedModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull userAdapterHolder holder, int position, @NonNull FeaturedModel model) {

        try {

            holder.name.setText(model.getName());
            holder.name1.setText(model.getName());
            holder.branch.setText(model.getBranch());
            holder.branch1.setText(model.getBranch());
            holder.shortBio.setText(model.getShortBio());
            holder.year.setText(model.getYear());
            Glide.with(holder.profileImage.getContext()).load(model.getPurl()).into(holder.profileImage);
            holder.medal.setAnimationFromUrl(model.getMedallottie());
            holder.medal1.setAnimationFromUrl(model.getMedallottie());


        }catch (Exception e){
            Toast.makeText(holder.name.getContext(), "Something wrong in Featured",Toast.LENGTH_SHORT).show();
        }



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
