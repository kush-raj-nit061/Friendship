package com.example.friendship;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.friendship.FriendRequestAdapter;
import com.example.friendship.Model.Celebration;
import com.example.friendship.Model.Events;
import com.example.friendship.Model.Likedby;
import com.example.friendship.Model.User;
import com.example.friendship.OnItemClick;
import com.example.friendship.R;

import com.example.friendship.UserAdapter;
import com.example.friendship.UserModel;
import com.example.friendship.UsersAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class SettingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCeleb;
    private RecyclerView recyclerViewEvents;

    private NotificationAdapter userAdapter;
    private EventsAdapter eventAdapter;
    private LikedbyAdapter celebAdapter;
    static OnItemClick onItemClick;
    LinearLayoutManager manager1;
    LinearLayoutManager manager2;
    String key = "";
    LottieAnimationView likedBy;
    LinearLayoutManager manager3;
    DatabaseReference myRefCeleb;
    RelativeLayout celeb;
    String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};



    public static SettingsFragment newInstance(OnItemClick click) {
        onItemClick = click;
        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_settings_fragment, container, false);


        celeb = view.findViewById(R.id.celebration);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewCeleb = view.findViewById(R.id.recycler_view_celeb);
        likedBy = view.findViewById(R.id.likedby);
        recyclerViewEvents = view.findViewById(R.id.recEvents);
        manager1 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        manager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        manager3 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        recyclerView.setHasFixedSize(true);


        recyclerView.setLayoutManager(manager1);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerViewCeleb.setHasFixedSize(true);
        recyclerViewCeleb.setLayoutManager(manager2);
        DividerItemDecoration dividerItemDecorationCeleb = new DividerItemDecoration(recyclerViewCeleb.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewCeleb.addItemDecoration(dividerItemDecorationCeleb);


        recyclerViewEvents.setHasFixedSize(true);
        recyclerViewEvents.setLayoutManager(manager3);
        DividerItemDecoration dividerItemDecorationEvents = new DividerItemDecoration(recyclerViewEvents.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewEvents.addItemDecoration(dividerItemDecorationCeleb);



        try {
            myRefCeleb = FirebaseDatabase.getInstance().getReference("Events");
            myRefCeleb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        long l = snapshot.getChildrenCount();
                        if(l>0){
                            recyclerViewEvents.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){}


        readUsersEvent();

        readUsersCeleb();




        readUsers();

        return view;
    }

    private void readUsersEvent(){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String userId = fAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference().child("Events");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        String s = datePatternFormat.format(new Date().getTime());
                        int i = Integer.parseInt(s.substring(0,2));
                        if(snapshot1.child("date").getValue().equals(String.valueOf(i-1)) || snapshot1.child("date").getValue().equals("0"+String.valueOf(i-1))){
                               key = snapshot1.getKey();
                            if(!key.equals("")){
                                reference.child(key).removeValue();
                            }
                        }
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                    Query query = reference.orderByChild("date").limitToLast(count);

                    FirebaseRecyclerOptions<Events> options =
                            new FirebaseRecyclerOptions.Builder<Events>()
                                    .setQuery(query, Events.class)
                                    .build();

                    eventAdapter=new EventsAdapter(options);
                    recyclerViewEvents.setAdapter(eventAdapter);
                    eventAdapter.startListening();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){

        }









    }



    private void readUsersCeleb() {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Celebration");
        String userId = fAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference().child("Likes").child(fAuth.getCurrentUser().getUid());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat datePatternFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        recyclerViewCeleb.setVisibility(View.GONE);



        try {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        celeb.setVisibility(View.VISIBLE);
                        recyclerViewCeleb.setVisibility(View.VISIBLE);
                        likedBy.setVisibility(View.VISIBLE);
                        FirebaseRecyclerOptions<Likedby> options =
                                new FirebaseRecyclerOptions.Builder<Likedby>()
                                        .setQuery(reference, Likedby.class)
                                        .build();
                        celebAdapter=new LikedbyAdapter(options);
                        recyclerViewCeleb.setAdapter(celebAdapter);
                        celebAdapter.startListening();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){}







    }


    private void readUsers() {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Connection");
        String userId = fAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference().child("Connection").child(userId);

        try {

            Query query = reference.orderByChild("status").equalTo("1");

            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    long l = snapshot.getChildrenCount();
                    if(!(l == 0)){
                        recyclerView.setVisibility(View.VISIBLE);
                        FirebaseRecyclerOptions<User> options =
                                new FirebaseRecyclerOptions.Builder<User>()
                                        .setQuery(query, User.class)
                                        .build();


                        userAdapter=new NotificationAdapter(options);
                        recyclerView.setAdapter(userAdapter);
                        userAdapter.startListening();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){}





    }
}
