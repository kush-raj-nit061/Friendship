package com.example.friendship;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GiftsFragment extends Fragment {

    RecyclerView recFeatured1;
    RecyclerView recFeatured2;
    FeaturedAdapter userAdapter;
    FeaturedAdapter userAdapter2;
    DatabaseReference reference;

    LinearLayoutManager manager;
    LinearLayoutManager manager2;

    public GiftsFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_gifts_fragment, container, false);


        recFeatured1  = view.findViewById(R.id.recFeatured1);
        recFeatured2  = view.findViewById(R.id.recFeatured2);

        manager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        manager2 = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);

        recFeatured1.setLayoutManager(manager);
        recFeatured2.setLayoutManager(manager2);
//        recFeatured.setHasFixedSize(true);
        recFeatured1.setItemAnimator(null);
        recFeatured2.setItemAnimator(null);


        FirebaseRecyclerOptions<FeaturedModel> options =
                new FirebaseRecyclerOptions.Builder<FeaturedModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Featured"), FeaturedModel.class)
                        .build();
        userAdapter=new FeaturedAdapter(options);
        recFeatured1.setAdapter(userAdapter);

        userAdapter.startListening();

        FirebaseRecyclerOptions<FeaturedModel> options2 =
                new FirebaseRecyclerOptions.Builder<FeaturedModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("CoolFeatured"), FeaturedModel.class)
                        .build();
        userAdapter2=new FeaturedAdapter(options2);
        recFeatured2.setAdapter(userAdapter2);
        userAdapter2.startListening();



        return view;
    }
}