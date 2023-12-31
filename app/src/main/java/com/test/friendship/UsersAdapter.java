package com.test.friendship;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.test.friendship.Model.Messages;
import com.test.friendship.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.test.friendship.Model.Chat;
import com.test.friendship.Model.User;
import java.util.List;
import com.test.friendship.R;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private boolean ischat;
    private OnItemClick onItemClick;

    Typeface MR, MRR;
    String theLastMessage;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference dbRef = database.getReference("students");

    public UsersAdapter(Context mContext, OnItemClick onItemClick, List<User> mUsers, boolean ischat) {
        this.onItemClick = onItemClick;
        this.mUsers = mUsers;
        this.mContext = mContext;
        this.ischat = ischat;

        if (mContext != null) {
            MRR = Typeface.createFromAsset(mContext.getAssets(), "fonts/myriadregular.ttf");
            MR = Typeface.createFromAsset(mContext.getAssets(), "fonts/myriad.ttf");
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.message_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = mUsers.get(position);
        holder.username.setTypeface(MR);
        holder.last_msg.setTypeface(MRR);

        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile_image.setImageResource(R.drawable.ic_profile);
        } else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profile_image);
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students").child(user.getId());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    holder.last_msg.setText((String) snapshot.child("branch").getValue());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (ischat) {
            if (user.getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.GONE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra("visit_user_id", user.getId());
                intent.putExtra("visit_user_name", user.getUsername());
                intent.putExtra("visit_image", user.getImageURL());
                mContext.startActivity(intent);
            }
        });

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) view.getContext();
                dbRef.child(user.getId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserModel model = (UserModel) snapshot.getValue(UserModel.class);
                        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                                new UserDescrFragment(model.getUserId(), model.getName(), model.getBranch(), model.getYear()
                                        , model.getShortBio(), model.getPurl(), model.getHobbies(), model.getBirthday()
                                        , model.getQualitylike(), model.getQualitydislike(), model.getFoods()
                                        , model.getBooks(), model.getTravellike())).addToBackStack(null).commit();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        lastMessage(user.getId(), holder.last_msg);
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView username;
        public ImageView profile_image;
        private ImageView img_on;
        private ImageView img_off;
        private TextView last_msg;

        public ViewHolder(View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
            last_msg = itemView.findViewById(R.id.last_msg);
        }
    }

    // Check for the last message
    private void lastMessage(final String userId, final TextView last_msg) {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Messages");

        reference.child(firebaseUser.getUid()).child(userId)
                .orderByKey()
                .limitToLast(1)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            Messages message = snapshot.getValue(Messages.class);
                            if (message != null) {
                                String messageType = message.getType();
                                String messageText = message.getMessage();

                                if (messageType.equals("text")) {
                                    theLastMessage = messageText;
                                } else if (messageType.equals("image")) {
                                    theLastMessage = "Image message";
                                } else if (messageType.equals("pdf")) {
                                    theLastMessage = "PDF document";
                                } else if (messageType.equals("docx")) {
                                    theLastMessage = "Word document";
                                }
                            }
                        }

                        switch (theLastMessage) {
                            case "default":
                                last_msg.setText("No Message");
                                break;

                            default:
                                last_msg.setText(theLastMessage);
                                break;
                        }

                        theLastMessage = "default";
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
           });
}
}