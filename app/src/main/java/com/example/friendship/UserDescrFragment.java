package com.example.friendship;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.sshadkany.shapes.RoundRectView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import soup.neumorphism.NeumorphButton;
import soup.neumorphism.NeumorphCardView;

public class UserDescrFragment extends Fragment {
    String userId,name,branch,year,shortBio,purl,hobbies,birthday,qualitylike,qualitydislike,foods,books,travellike;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageReference = storage.getReference();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Connection");
    DatabaseReference myOwnRef = database.getReference("students");


    public  UserDescrFragment(){}
    public UserDescrFragment(String userId,String name, String branch, String year, String shortBio, String purl,String hobbies,String birthday,String qualitylike,String qualitydislike,String foods,String books,String travellike){
        this.userId=userId;
        this.name = name;
        this.branch = branch;
        this.purl = purl;
        this.year = year;
        this.shortBio = shortBio;
        this.foods = foods;
        this.hobbies = hobbies;
        this.books=books;
        this.travellike = travellike;
        this.qualitydislike = qualitydislike;
        this.qualitylike = qualitylike;
        this.birthday = birthday;

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.fragment_user_descr, container, false);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout round = v.findViewById(R.id.round);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RoundRectView roundrect = v.findViewById(R.id.roundrect);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RoundRectView roundrect1 = v.findViewById(R.id.roundrect1);
         @SuppressLint({"MissingInflatedId", "LocalSuppress"}) CardView cardView = v.findViewById(R.id.card5);
        ImageView imagegholder = v.findViewById(R.id.imagegholder);
        TextView nameholder = v.findViewById(R.id.nameholder);
        TextView branchholder = v.findViewById(R.id.branchholder);
        TextView yearholder = v.findViewById(R.id.yearholder);
        NeumorphButton button = v.findViewById(R.id.button);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) LinearLayout privatebutton = v.findViewById(R.id.privatea);
        if(userId.equals(fAuth.getUid())){
            button.setVisibility(View.GONE);
        }


        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullProfileLoader.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });
        imagegholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullProfileLoader.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullProfileLoader.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });roundrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullProfileLoader.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });roundrect1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), FullProfileLoader.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });

        NeumorphCardView card1 = v.findViewById(R.id.card1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView shortBioholder = v.findViewById(R.id.shortbioholder);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tvfoods = v.findViewById(R.id.tvfoods);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tvhobbies = v.findViewById(R.id.tvhobbies);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tvbirthday = v.findViewById(R.id.tvbirthday);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView tvqualitylike = v.findViewById(R.id.tvqualitylike);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tvqualitydislike = v.findViewById(R.id.tvqualitydislike);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tvbooks = v.findViewById(R.id.tvbooks);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})TextView tvtravellike = v.findViewById(R.id.tvtravellike);
        Map<String,Object> objectMap = new HashMap<>();
        objectMap.put("username",name);
        objectMap.put("branch",branch);
        objectMap.put("imageURL",purl);
        objectMap.put("id",userId);
        objectMap.put("bio",shortBio);
        try {
            myRef.child(fAuth.getUid()).child(userId).updateChildren(objectMap);
        }catch(Exception e){

        }




        Map<String,Object> myProfile = new HashMap<>();



        try {
            myOwnRef.child(fAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        String myName = (String) snapshot.child("name").getValue();
                        String myBranch = (String) snapshot.child("branch").getValue();
                        String myPurl = (String) snapshot.child("purl").getValue();
                        String myUserId = (String) snapshot.child("userId").getValue();
                        String myOwnBio= (String) snapshot.child("shortBio").getValue();

                        myProfile.put("username",myName);
                        myProfile.put("branch",myBranch);
                        myProfile.put("imageURL",myPurl);
                        myProfile.put("id",myUserId);
                        myProfile.put("bio",myOwnBio);
                        try {
                            myRef.child(userId).child(fAuth.getUid()).updateChildren(myProfile);
                        }catch (Exception e){}


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){}

        try {
            myRef.child(userId).child(fAuth.getUid().toString()).child("status").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    try {
                        if (snapshot.getValue() != null) {
                            try {
                                if(snapshot.getValue().equals("0")){
                                    button.setText("Align");
                                    card1.setVisibility(View.INVISIBLE);
                                    privatebutton.setVisibility(View.INVISIBLE);

                                } else if (snapshot.getValue().equals("1")) {
                                    button.setText("Requested");
                                    card1.setVisibility(View.VISIBLE);
                                    privatebutton.setVisibility(View.INVISIBLE);

                                } else if(snapshot.getValue().equals("2")){
                                    button.setText("Diverge");
                                    privatebutton.setVisibility(View.VISIBLE);
                                    card1.setVisibility(View.VISIBLE);

                                }

                            } catch (Exception e) {

                            }
                        } else {
                            button.setText("Align");
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }catch (Exception e){}



        try {
            if(birthday.length()>7){
                tvbirthday.setText(birthday.substring(0,6));
            }
        }catch (Exception e){}


        tvbooks.setText(books);
        tvfoods.setText(foods);
        tvhobbies.setText(hobbies);
        tvqualitydislike.setText(qualitydislike);
        tvqualitylike.setText(qualitylike);
        tvtravellike.setText(travellike);
        nameholder.setText(name);
        branchholder.setText(branch);
        yearholder.setText(year);
        shortBioholder.setText(shortBio);
        Glide.with(getContext()).load(purl).into(imagegholder);
        imagegholder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                privatebutton.setVisibility(View.VISIBLE);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child(userId).child(fAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Map<String,Object> map = new HashMap<>();
                        if(snapshot.child("status").exists()){
                            if(snapshot.child("status").getValue().equals("0")){
                                Toast.makeText(getContext(),"Requested:}",Toast.LENGTH_SHORT).show();
//                                    Toast.makeText(getContext(),userId,Toast.LENGTH_SHORT).show();
                                card1.setVisibility(View.VISIBLE);
                                privatebutton.setVisibility(View.INVISIBLE);
                                button.setText("Requested");
                                map.put("status","1");
                                myRef.child(userId).child(fAuth.getUid()).updateChildren(map);

                            } else if (snapshot.child("status").getValue().equals("1")) {
                                button.setText("Align");
                                Toast.makeText(getContext(),"Request retrieved:{",Toast.LENGTH_SHORT).show();
                                card1.setVisibility(View.INVISIBLE);
                                privatebutton.setVisibility(View.INVISIBLE);
                                map.put("status","0");
                                myRef.child(userId).child(fAuth.getUid()).updateChildren(map);
                            }else{
                                card1.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(),"Unaligned",Toast.LENGTH_SHORT).show();
                                privatebutton.setVisibility(View.INVISIBLE);
                                button.setText("Align");
                                map.put("status","0");
                                myRef.child(userId).child(fAuth.getUid()).updateChildren(map);
                                myRef.child(fAuth.getUid()).child(userId).updateChildren(map);
                            }

                        }else {
                            card1.setVisibility(View.VISIBLE);
                            privatebutton.setVisibility(View.INVISIBLE);
                            Toast.makeText(getContext(),"Requested:}",Toast.LENGTH_SHORT).show();
                            button.setText("Requested");
                            map.put("status","1");

                            myRef.child(userId).child(fAuth.getUid()).updateChildren(map);

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });







//                myRef.child(fAuth.getUid().toString()).child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DataSnapshot> task) {
//                        int i = 0;
//
//                        for(DataSnapshot snapshot: task.getResult().getChildren()){
//                            i=1;
//                            Toast.makeText(getContext(),snapshot.getKey().toString(),Toast.LENGTH_SHORT).show();
//
//
//                            if (snapshot.getKey().toString().trim().equals(userId)){
//
//
//
//                                Map<String,Object> map = new HashMap<>();
//                                if(snapshot.getValue().equals("0")){
////                                    Toast.makeText(getContext(),userId,Toast.LENGTH_SHORT).show();
//                                    card1.setVisibility(View.VISIBLE);
//                                    privatebutton.setVisibility(View.INVISIBLE);
//                                    button.setText("Requested");
//                                    map.put(userId,"1");
//                                    myRef.child(fAuth.getUid()).updateChildren(map);
//                                    break;
//                                } else if (snapshot.getValue().equals("1")) {
//                                    button.setText("Align");
//                                    card1.setVisibility(View.INVISIBLE);
//                                    privatebutton.setVisibility(View.INVISIBLE);
//                                    map.put(userId,"0");
//                                    myRef.child(fAuth.getUid()).updateChildren(map);
//                                    break;
//                                }else{
//                                    card1.setVisibility(View.INVISIBLE);
//                                    privatebutton.setVisibility(View.INVISIBLE);
//                                    button.setText("Diverge");
//                                    map.put(userId,"0");
//                                    myRef.child(fAuth.getUid()).updateChildren(map);
//                                    break;
//
//                                }
//
//                            }else{
//                                card1.setVisibility(View.VISIBLE);
//                                privatebutton.setVisibility(View.INVISIBLE);
//                                i=1;
//
//                                button.setText("Requested");
//                                Map<String,Object> map = new HashMap<>();
//                                map.put(userId,"1");
//                                myRef.child(fAuth.getUid()).updateChildren(map);
//
//                            }
//                        }
//
//                        if(i==0){
//                            card1.setVisibility(View.VISIBLE);
//                            privatebutton.setVisibility(View.INVISIBLE);
//                            button.setText("Requested");
//                            Map<String,Object> map = new HashMap<>();
//                            map.put(userId,"1");
//                            myRef.child(fAuth.getUid()).updateChildren(map);
//                        }
//                    }
//                });

            }
        });

        return v;
    }
    public void onBackPressed(){
        AppCompatActivity appCompatActivity = (AppCompatActivity) getContext();
        appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer,
                new DashboardFragment()).addToBackStack(null).commit();

    }
}