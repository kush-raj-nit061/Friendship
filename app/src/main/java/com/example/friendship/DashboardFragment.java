package com.example.friendship;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.rolud.solidglowanimation.SolidGlowAnimation;

public class DashboardFragment extends Fragment {

    RecyclerView recView;
    UserAdapter userAdapter;
    SolidGlowAnimation animation_view_complex_view;


    public DashboardFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_dashboard_fragment, container, false);
        recView = (RecyclerView) view.findViewById(R.id.recPeople);
        animation_view_complex_view = view.findViewById(R.id.animation_view_complex_view);
        animation_view_complex_view.startAnimation();

        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<UserModel> options =
                new FirebaseRecyclerOptions.Builder<UserModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("students"), UserModel.class)
                        .build();

        userAdapter=new UserAdapter(options);
        recView.setAdapter(userAdapter);
        userAdapter.startListening();


        return view;
    }


}

