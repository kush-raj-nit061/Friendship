package com.example.friendship;
import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.friendship.BasicDetails.UsersFragment;
import com.example.friendship.Model.Chatlist;
import com.example.friendship.Model.User;
import com.example.friendship.Notifications.Token;
import com.github.sshadkany.shapes.CircleView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.List;
public class MailFragment extends Fragment {
    public MailFragment(){}
    private boolean isOpen = false;
    FragmentManager manager;
    FragmentTransaction transaction = null;
    private ConstraintSet layout1, layout2;
    private ConstraintLayout constraintLayout;
    private CircleView imageViewPhoto;
    private RecyclerView recyclerView;
    Typeface MR, MRR;
    private UsersAdapter userAdapter;
    private List<User> mUsers;
    FrameLayout frameLayout;
    TextView tvNoFriends, tvNoReq;
    ImageView imgProfile;
    FirebaseUser fuser;
    DatabaseReference reference;
    private List<Chatlist> usersList;
    static OnItemClick onItemClick;
    String purl;

    public static MailFragment newInstance(OnItemClick click)
    {
        onItemClick = click;
        Bundle args = new Bundle();
        MailFragment fragment = new MailFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_mail_fragment, container, false);
        layout1 = new ConstraintSet();
        layout2 = new ConstraintSet();
        imageViewPhoto = view.findViewById(R.id.photo);
        constraintLayout = view.findViewById(R.id.constraint_layout);
        layout2.clone(requireContext(), R.layout.activity_testing1);
        layout1.clone(constraintLayout);
        TextView tvReq = view.findViewById(R.id.tvReq);
        tvNoReq = view.findViewById(R.id.tvNoReq);
        tvNoFriends = view.findViewById(R.id.tvNoFriends);
        imgProfile = view.findViewById(R.id.imgProfile);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        DatabaseReference dbref = database.getReference();

        try {
            dbref.child("students").child(fAuth.getUid()).child("purl").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    purl = (String) snapshot.getValue();
                    try
                    {
                        Glide.with(getContext()).load(purl).into(imgProfile);
                    }
                    catch (Exception e){}
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }catch (Exception e){}

        try {
            DatabaseReference referencer = database.getReference().child("Connection").child(fAuth.getUid());
            Query queryr = referencer.orderByChild("status").equalTo("2");
            queryr.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    tvNoFriends.setText(String.valueOf((int) snapshot.getChildrenCount()));
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }catch (Exception e){}
        DatabaseReference reference = database.getReference().child("Connection").child(fAuth.getUid());
        Query query = reference.orderByChild("status").equalTo("1");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = dataSnapshot.getChildrenCount();
                tvNoReq.setText(String.valueOf(count));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
        tvReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment;
                AppCompatActivity appCompatActivity = (AppCompatActivity) v.getContext();
                FragmentTransaction transaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainer, new UsersFragment());
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        imageViewPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isOpen) {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout2.applyTo(constraintLayout);
                    isOpen = !isOpen;
                } else {
                    TransitionManager.beginDelayedTransition(constraintLayout);
                    layout1.applyTo(constraintLayout);
                    isOpen = !isOpen;
                }
            }
        });

        MRR = Typeface.createFromAsset(getContext().getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getContext().getAssets(), "fonts/myriad.ttf");
        recyclerView = view.findViewById(R.id.recConnection);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        DatabaseReference reference1 = database.getReference().child("Connection").child(fAuth.getUid());
        Query query1 = reference1.orderByChild("status").equalTo("2");
        reference = FirebaseDatabase.getInstance().getReference("Chatlist").child(fuser.getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chatlist chatlist = snapshot.getValue(Chatlist.class);
                    usersList.add(chatlist);
                }
                if(usersList.size()==0){
                }
                else{
                }
                chatList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        updateToken(String.valueOf(FirebaseMessaging.getInstance().getToken()));
        return view;
    }
    private void updateToken(String token){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");
        Token token1 = new Token(token);
        reference.child(fuser.getUid()).setValue(token1);
    }
    private void chatList() {
        mUsers = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    for (Chatlist chatlist : usersList){
                        if (user!= null && user.getId()!=null && chatlist!=null && chatlist.getId()!= null &&
                                user.getId().equals(chatlist.getId())){
                            mUsers.add(user);
                        }
                    }
                }
                userAdapter = new UsersAdapter(getContext(), onItemClick,mUsers, true);
                recyclerView.setAdapter(userAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
