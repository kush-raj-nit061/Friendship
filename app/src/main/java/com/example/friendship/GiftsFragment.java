package com.example.friendship;


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

    RecyclerView recFeatured;
    FeaturedAdapter userAdapter;
    DatabaseReference reference;

    public GiftsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_gifts_fragment, container, false);


        recFeatured  = view.findViewById(R.id.recFeatured);

        recFeatured.setLayoutManager(new LinearLayoutManager(getContext()));
//        recFeatured.setHasFixedSize(true);
        recFeatured.setItemAnimator(null);


        FirebaseRecyclerOptions<FeaturedModel> options =
                new FirebaseRecyclerOptions.Builder<FeaturedModel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Featured"), FeaturedModel.class)
                        .build();
        userAdapter=new FeaturedAdapter(options);
        recFeatured.setAdapter(userAdapter);
        userAdapter.startListening();



        return view;
    }
}