package com.test.friendship;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.os.Bundle;
import com.test.friendship.R;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.alimuzaffar.lib.widgets.AnimatedEditText;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
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
//    SolidGlowAnimation animation_view_complex_view;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
    AnimatedEditText searchEditText;
    LottieAnimationView progress;
    private Parcelable recyclerViewState;
    LinearLayout linear;
    ImageView eye;
    // Add this variable

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
//        animation_view_complex_view = view.findViewById(R.id.animation_view_complex_view);
//        animation_view_complex_view.startAnimation();
        searchEditText = view.findViewById(R.id.etSearch);
        progress = view.findViewById(R.id.progress);
        eye = view.findViewById(R.id.eyes);
        linear = view.findViewById(R.id.linear);
        progress.setVisibility(View.VISIBLE);

        linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linear.animate().alpha(1f).setDuration(0);

                linear.animate().alpha(0f).setDuration(1900).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linear.animate().alpha(0f).setDuration(1800);
                        linear.setVisibility(View.GONE);
                        eye.setVisibility(View.GONE);

                    }
                });

            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                linear.animate().alpha(1f).setDuration(0);

                linear.animate().alpha(0f).setDuration(1900).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        linear.animate().alpha(0f).setDuration(1800);
                        linear.setVisibility(View.GONE);
                        eye.setVisibility(View.GONE);

                    }
                });


            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);

        recView.setLayoutManager(manager);
        recView.setItemAnimator(null);
        recView.setItemViewCacheSize(20);
        recView.setDrawingCacheEnabled(true);
        recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        initializeRecyclerView();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Nothing to do here
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().toLowerCase().trim();

                // Update your adapter with the new options
                try {
                    // Create a query based on the entered text
                    Query query = FirebaseDatabase.getInstance().getReference().child("students")
                            .orderByChild("lower")
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
        try {
            if (recView.getLayoutManager() != null) {
                recyclerViewState = recView.getLayoutManager().onSaveInstanceState();
                outState.putParcelable("recycler_state", recyclerViewState);
            }

        }catch (Exception ignored){}

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Restore the RecyclerView's scroll state if available
        try {
            if (recyclerViewState != null) {
                recView.getLayoutManager().onRestoreInstanceState(recyclerViewState);
            }

        }catch (Exception ignored){}

    }

    // Method to initialize FirebaseRecyclerOptions and UserAdapter
    private void initializeRecyclerView() {
        reference = FirebaseDatabase.getInstance().getReference();
        try {
            FirebaseRecyclerOptions<UserModel> options =
                    new FirebaseRecyclerOptions.Builder<UserModel>()
                            .setQuery(FirebaseDatabase.getInstance().getReference().child("students")
                                    .orderByChild("likes"), UserModel.class)
                            .build();
            userAdapter = new UserAdapter(options);
            recView.setAdapter(userAdapter);
            userAdapter.startListening();
//

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progress.setVisibility(View.GONE);
                }
            },4000);

        }catch (Exception e){

            Toast.makeText(getContext(), "Something error in students data", Toast.LENGTH_SHORT).show();
        }





    }
}