package com.example.friendship.BasicDetails;

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


public class UsersFragment extends Fragment {

    private RecyclerView recyclerView;

    Typeface MR, MRR;
    FrameLayout frameLayout;
    TextView es_descp, es_title;

    private FriendRequestAdapter userAdapter;
    private List<User> mUsers;
    static OnItemClick onItemClick;

    EditText search_users;

    public static UsersFragment newInstance(OnItemClick click) {
        onItemClick = click;
        Bundle args = new Bundle();

        UsersFragment fragment = new UsersFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users, container, false);


        MRR = Typeface.createFromAsset(getContext().getAssets(), "fonts/myriadregular.ttf");
        MR = Typeface.createFromAsset(getContext().getAssets(), "fonts/myriad.ttf");

        recyclerView = view.findViewById(R.id.recycler_view);
        frameLayout = view.findViewById(R.id.es_layout);
        es_descp = view.findViewById(R.id.es_descp);
        es_title = view.findViewById(R.id.es_title);

        es_descp.setTypeface(MR);
        es_title.setTypeface(MRR);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        mUsers = new ArrayList<>();

        readUsers();

        return view;
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

            FirebaseRecyclerOptions<User> options =
                    new FirebaseRecyclerOptions.Builder<User>()
                            .setQuery(query, User.class)
                            .build();


            userAdapter=new FriendRequestAdapter(options);
            recyclerView.setAdapter(userAdapter);
            userAdapter.startListening();

        }catch (Exception e){}



    }
}
