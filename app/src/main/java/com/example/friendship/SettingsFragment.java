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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendship.FriendRequestAdapter;
import com.example.friendship.Model.Celebration;
import com.example.friendship.Model.Events;
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


import java.util.ArrayList;
import java.util.List;


public class SettingsFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerViewCeleb;
    private RecyclerView recyclerViewEvents;

    private NotificationAdapter userAdapter;
    private EventsAdapter eventAdapter;
    private CelebrationAdapter celebAdapter;
    static OnItemClick onItemClick;
    LinearLayoutManager manager1;
    LinearLayoutManager manager2;
    LinearLayoutManager manager3;



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


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerViewCeleb = view.findViewById(R.id.recycler_view_celeb);
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

        FirebaseRecyclerOptions<Events> options =
                new FirebaseRecyclerOptions.Builder<Events>()
                        .setQuery(reference, Events.class)
                        .build();

        eventAdapter=new EventsAdapter(options);
        recyclerViewEvents.setAdapter(eventAdapter);
        eventAdapter.startListening();


    }



    private void readUsersCeleb() {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Celebration");
        String userId = fAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference().child("Celebration").child(fAuth.getUid());

        FirebaseRecyclerOptions<Celebration> options =
                new FirebaseRecyclerOptions.Builder<Celebration>()
                        .setQuery(reference, Celebration.class)
                        .build();

        celebAdapter=new CelebrationAdapter(options);
        recyclerViewCeleb.setAdapter(celebAdapter);
        celebAdapter.startListening();



    }


    private void readUsers() {

        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Connection");
        String userId = fAuth.getCurrentUser().getUid();
        DatabaseReference reference = database.getReference().child("Connection").child(userId);

        Query query = reference.orderByChild("status").equalTo("1");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(query, User.class)
                        .build();


        userAdapter=new NotificationAdapter(options);
        recyclerView.setAdapter(userAdapter);
        userAdapter.startListening();

    }
}
