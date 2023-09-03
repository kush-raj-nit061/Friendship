package com.example.friendship;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rolud.solidglowanimation.SolidGlowAnimation;

public class DashboardFragment extends Fragment {

    RecyclerView recView;
    UserAdapter userAdapter;
    SolidGlowAnimation animation_view_complex_view;
    DatabaseReference reference;
    EditText searchEditText;
    int count;
    private Parcelable recyclerViewState; // Add this variable

    public DashboardFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_dashboard_fragment, container, false);
        recView = view.findViewById(R.id.recPeople);
        animation_view_complex_view = view.findViewById(R.id.animation_view_complex_view);
        animation_view_complex_view.startAnimation();
        searchEditText = view.findViewById(R.id.etSearch);

        recView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Restore the RecyclerView's scroll state if available
        if (savedInstanceState != null) {
            recyclerViewState = savedInstanceState.getParcelable("recycler_state");
        }

        // Initialize FirebaseRecyclerOptions and UserAdapter
        initializeRecyclerView();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();

                // Update your adapter with the new options
                try {
                    // Create a query based on the entered text
                    Query query = FirebaseDatabase.getInstance().getReference().child("students")
                            .orderByChild("name")
                            .startAt(searchText)
                            .endAt(searchText + "\uf8ff");

                    FirebaseRecyclerOptions<UserModel> options = new FirebaseRecyclerOptions.Builder<UserModel>()
                            .setQuery(query, UserModel.class)
                            .build();

                    userAdapter.updateOptions(options);
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Orientation changed", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Nothing to do here
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the RecyclerView's scroll state
        if (recView.getLayoutManager() != null) {
            recyclerViewState = recView.getLayoutManager().onSaveInstanceState();
            outState.putParcelable("recycler_state", recyclerViewState);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Restore the RecyclerView's scroll state if available
        if (recyclerViewState != null) {
            recView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
        }
    }

    // Method to initialize FirebaseRecyclerOptions and UserAdapter
    private void initializeRecyclerView() {
        reference = FirebaseDatabase.getInstance().getReference();

        reference.child("students").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                count = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                FirebaseRecyclerOptions<UserModel> options =
                        new FirebaseRecyclerOptions.Builder<UserModel>()
                                .setQuery(FirebaseDatabase.getInstance().getReference().child("students")
                                        .orderByChild("likes").limitToLast(count), UserModel.class)
                                .build();
                userAdapter = new UserAdapter(options);
                recView.setAdapter(userAdapter);
                userAdapter.startListening();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error here
            }
        });
    }
}