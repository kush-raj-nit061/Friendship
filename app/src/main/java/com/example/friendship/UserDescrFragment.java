package com.example.friendship;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;

public class UserDescrFragment extends Fragment {
    String name,branch,year,shortBio,purl;

    public  UserDescrFragment(){}
    public UserDescrFragment(String name, String branch, String year, String shortBio, String purl){
        this.name = name;
        this.branch = branch;
        this.purl = purl;
        this.year = year;
        this.shortBio = shortBio;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_user_descr, container, false);
        ImageView imagegholder = v.findViewById(R.id.imagegholder);
        TextView nameholder = v.findViewById(R.id.nameholder);
        TextView branchholder = v.findViewById(R.id.branchholder);
        TextView yearholder = v.findViewById(R.id.yearholder);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView shortBioholder = v.findViewById(R.id.shortbioholder);
        nameholder.setText(name);
        branchholder.setText(branch);
        yearholder.setText(year);
        shortBioholder.setText(shortBio);
        Glide.with(getContext()).load(purl).into(imagegholder);

        return v;
    }
    public void onBackPressed(){

        AppCompatActivity appCompatActivity = (AppCompatActivity) getContext();
        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new DashboardFragment()).addToBackStack(null).commit();

    }
}