package com.test.friendship;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.test.friendship.R;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.test.friendship.Model.Collaborator;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollaboratorAdapter extends FirebaseRecyclerAdapter<Collaborator,CollaboratorAdapter.userAdapterHolder> {

    private FragmentManager fragmentManager;

    FragmentTransaction transaction = null;




    public CollaboratorAdapter(@NonNull FirebaseRecyclerOptions<Collaborator> options,FragmentManager fragment) {
        super(options);
        this.fragmentManager = fragment;
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
                    try {
                        Glide.with(holder.profile.getContext()).load(purl).into(holder.profile);
                    }catch (Exception e){}

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            holder.rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference dbs = FirebaseDatabase.getInstance().getReference("students").child(model.getId());
                    dbs.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                UserModel model = snapshot.getValue(UserModel.class);
                                AppCompatActivity appCompatActivity = unwrap(v.getContext());
                                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                                        new UserDescrFragment(model.getUserId(),model.getName(),model.getBranch(),model.getYear()
                                                ,model.getShortBio(),model.getPurl(),model.getHobbies(),model.getBirthday()
                                                ,model.getQualitylike(),model.getQualitydislike(),model.getFoods()
                                                ,model.getBooks(),model.getTravellike())).addToBackStack(null).commit();




                                try {


                                }catch (Exception e){

                                    Toast.makeText(v.getContext(), e.toString(), Toast.LENGTH_SHORT).show();
                                }


                            }



                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
            });


        }catch (Exception e){
            Toast.makeText(holder.name.getContext(), "Something wrong in Celebration",Toast.LENGTH_SHORT).show();
        }



    }
    private static AppCompatActivity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        assert context instanceof AppCompatActivity;
        return (AppCompatActivity) context;
    }

    @NonNull
    @Override
    public userAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collaborator_item,parent,false);
        return new userAdapterHolder(view);
    }

    public static class userAdapterHolder extends RecyclerView.ViewHolder {

        ImageView profile;
        TextView name,branch;
        RelativeLayout rv;

        public userAdapterHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.profile);
            name = itemView.findViewById(R.id.name);
            branch = itemView.findViewById(R.id.branch);
            rv = itemView.findViewById(R.id.rl);

        }
    }
    private void setFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        transaction.add(fragment,"all");
        transaction.addToBackStack(null);
        transaction.replace(R.id.frame, fragment).commit();
    }
}
