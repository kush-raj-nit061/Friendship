package com.example.friendship;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class DashboardFragment extends Fragment {

    RecyclerView recView;
    UserAdapter userAdapter;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_dashboard_fragment, container, false);
        recView = (RecyclerView) view.findViewById(R.id.recPeople);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"), UserModel.class)
                        .build();

        userAdapter=new UserAdapter(options);
        recView.setAdapter(userAdapter);


        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        userAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }
}

