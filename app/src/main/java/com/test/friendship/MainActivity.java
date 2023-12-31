package com.test.friendship;



import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.Manifest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import com.test.friendship.R;

import android.os.Handler;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.test.friendship.Admins.AnnouncementActivity;
import com.test.friendship.Collaboration.CollabDetails1;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mxn.soul.flowingdrawer_core.ElasticDrawer;
import com.mxn.soul.flowingdrawer_core.FlowingDrawer;
import com.wwdablu.soumya.lottiebottomnav.FontBuilder;
import com.wwdablu.soumya.lottiebottomnav.FontItem;
import com.wwdablu.soumya.lottiebottomnav.ILottieBottomNavCallback;
import com.wwdablu.soumya.lottiebottomnav.LottieBottomNav;
import com.wwdablu.soumya.lottiebottomnav.MenuItem;
import com.wwdablu.soumya.lottiebottomnav.MenuItemBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



public class MainActivity extends AppCompatActivity implements ILottieBottomNavCallback {
    FragmentTransaction transaction = null;
    LottieBottomNav bottomNav;
    boolean doubleBackToExitPressedOnce = false;
    ArrayList<MenuItem> list;
    DatabaseReference reference;
    DatabaseReference reference2;
    DatabaseReference reference3;
    FirebaseAuth fAuth = FirebaseAuth.getInstance();
    FlowingDrawer mDrawer;
//    ImageView iv_menu;
    ImageView ivDrawer;
    CardView cvProfile,terms,cvPrivacy,cvAboutUs,cvHelp,cvNotifications,cvCollab,share;
    TextView branch,year,name;
    String purl;
    private ViewPager viewPager;
    Boolean permission_notif = false;
    ImageView eyes;



    String [] permissions ;
    NavigationView navView;
    DrawerLayout drawerLayout;
    Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        terms = findViewById(R.id.terms);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{Manifest.permission.POST_NOTIFICATIONS};
        }

        navView = findViewById(R.id.navmenu);
        drawerLayout=findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggle.syncState();




        bottomNav   = findViewById(R.id.bottom_nav);
        cvCollab = findViewById(R.id.collab);
        cvProfile = findViewById(R.id.cvProfile);
        ivDrawer = findViewById(R.id.ivDrawer);
        branch = findViewById(R.id.branch);
        year = findViewById(R.id.year);
        name = findViewById(R.id.drawerName);
        cvPrivacy = findViewById(R.id.cvPrivacy);
        cvAboutUs = findViewById(R.id.aboutus);
        cvHelp = findViewById(R.id.help);
        share = findViewById(R.id.share);
        cvNotifications = findViewById(R.id.notification);
        eyes = findViewById(R.id.eyes);
        eyes.setImageResource(R.drawable.eyeopen);

        eyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bottomNav.getVisibility() == View.GONE){

                    eyes.setImageResource(R.drawable.eyeopen);

                    bottomNav.setVisibility(View.VISIBLE);}
                else {
                    bottomNav.setVisibility(View.GONE);
                    eyes.setImageResource(R.drawable.eyes);
                }
            }
        });









        FirebaseMessaging.getInstance().subscribeToTopic("All")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Done";
                        if (!task.isSuccessful()) {
                            msg = "Failed";
                        }

                    }
                });




        reference2 = FirebaseDatabase.getInstance().getReference("students");
        reference3 = FirebaseDatabase.getInstance().getReference("unregistered");


        Map<String,Object> map = new HashMap<>();

        try {
            reference2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        for(DataSnapshot snapshot1 : snapshot.getChildren()){
                            String lower = String.valueOf(snapshot1.child("name").getValue()).toLowerCase();
                            map.put("lower",lower);
                            reference2.child(snapshot1.getKey()).updateChildren(map);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }catch (Exception e){}



        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, TermsAndConditionsActivity.class);
                startActivity(i);
            }
        });
        cvPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, PrivacyAndSecurity.class);
                startActivity(i);
            }
        });
        cvCollab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, CollaborationActivity.class);
                startActivity(i);
            }
        });
        cvAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AboutUs.class);
                startActivity(i);

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FirebaseFirestore.getInstance().collection("ShareApp").document("AppLink").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot link1= task.getResult();
                            if(link1.exists()){
                                String link = link1.getString("link");
                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_TEXT, "Friendship");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Friendship");
                                intent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.linkdescription) + "\n\n" + link);
                                startActivity(Intent.createChooser(intent, "Friendship"));
                            }

                        }
                    });


                }catch (Exception ignored){}

            }
        });

        cvHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, HelpAndSupport.class);
                startActivity(i);
            }
        });

        cvNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseFirestore fStore = FirebaseFirestore.getInstance();
                try {

                    fStore.collection("Managers").document(fAuth.getCurrentUser().getUid())
                            .get().addOnCompleteListener(tasks -> {
                                if (tasks.isSuccessful()) {
                                    DocumentSnapshot document = tasks.getResult();
                                    if (document.exists()) {
                                        String detailsGiven = document.getString("Admin");
                                        if (detailsGiven.equals("1")) {
                                            Intent i = new Intent(MainActivity.this, AnnouncementActivity.class);
                                            startActivity(i);
                                            // For admin work
                                        } else {
                                            Toast.makeText(getApplicationContext(), "You don't have permission to set Clubs Notification", Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "You don't have permission to set Notification", Toast.LENGTH_SHORT).show();

                                }
                            });
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "You don't have permission to set Notification:", Toast.LENGTH_SHORT).show();

                }

            }
        });
        try {

            reference2.child(fAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Glide.with(getApplicationContext()).load(snapshot.child("purl").getValue()).into(ivDrawer);
                    purl= (String) snapshot.child("purl").getValue();
                    year.setText(String.valueOf(snapshot.child("year").getValue()));
                    branch.setText(String.valueOf(snapshot.child("branch").getValue()));
                    name.setText(String.valueOf(snapshot.child("name").getValue()));

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Something wrong",Toast.LENGTH_SHORT).show();
        }



        cvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });

        ivDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, FullProfileLoader.class);
                i.putExtra("purl",purl);
                startActivity(i);
            }
        });



        bottomNav   = findViewById(R.id.bottom_nav);

        //Set font item
        FontItem fontItem = FontBuilder.create("Colleagues")
                .selectedTextColor(Color.BLACK)
                .unSelectedTextColor(Color.GRAY)
                .selectedTextSize(16) //SP
                .unSelectedTextSize(12) //SP
                .setTypeface(Typeface.createFromAsset(getAssets(), "coffeesugar.ttf"))
                .build();

        //Menu Dashboard
        MenuItem item1 = MenuItemBuilder.create("homes.json", MenuItem.Source.Assets, fontItem, "dash")
                .pausedProgress(1f)
                .loop(false)
                .build();

        //Example Spannable String (at Menu Gifts)
        SpannableString spannableString = new SpannableString("Featured");
        spannableString.setSpan(new ForegroundColorSpan(Color.RED), 0, 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        //Menu Gifts
        fontItem = FontBuilder.create(fontItem).setTitle(spannableString).build();
        MenuItem item2 = MenuItemBuilder.createFrom(item1, fontItem)
                .selectedLottieName("gifts.json")
                .unSelectedLottieName("gifts.json")
                .loop(true)
                .build();

        //Menu Mail
        fontItem = FontBuilder.create(fontItem).setTitle("Messages").build();
        MenuItem item3 = MenuItemBuilder.createFrom(item1, fontItem)
                .selectedLottieName("messages.json")
                .unSelectedLottieName("messages.json")
                .pausedProgress(0.75f)
                .build();

        //Menu Settings
        fontItem = FontBuilder.create(fontItem).setTitle("Notification").build();
        MenuItem item4 = MenuItemBuilder.createFrom(item1, fontItem)
                .selectedLottieName("notification.json")
                .unSelectedLottieName("notification.json")
                .pausedProgress(0.75f)
                .build();

        list = new ArrayList<>(4);
        list.add(item1);
        list.add(item2);
        list.add(item3);
        list.add(item4);

        bottomNav.setCallback(this);
        bottomNav.setMenuItemList(list);
        bottomNav.setSelectedIndex(0);

        setFragment(new DashboardFragment());
    }

    private void requestPermission() {

        try{

            if(ContextCompat.checkSelfPermission(MainActivity.this,permissions[0]) == PackageManager.PERMISSION_GRANTED){
                permission_notif = true;
            }else {

                if(shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)){
                    Toast.makeText(getApplicationContext(),"Grant Permission for Latest Event Notification ",Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Grant Permission for Latest Event Notification ",Toast.LENGTH_SHORT).show();
                }
                requestPermissionLauncherNotification.launch(permissions[0]);
            }

        }catch (Exception e){


        }

    }

    private ActivityResultLauncher<String>requestPermissionLauncherNotification=
            registerForActivityResult(new ActivityResultContracts.RequestPermission(),isGranted -> {
                if(isGranted){
                    permission_notif = true;
                }
                else {
                    permission_notif = false;
                    showPermissionDialog("Notification Permission");
                }
            });

    private void showPermissionDialog(String permission_desc) {

        new AlertDialog.Builder(
                MainActivity.this
        ).setTitle("Alert for Notification Permission")
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent rintent = new Intent();
                        rintent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri =  Uri.fromParts("package",getPackageName(),null);
                        rintent.setData(uri);
                        startActivity(rintent);
                        dialog.dismiss();
                    }
                }).setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })

                .show();
    }


    @Override
    public void onMenuSelected(int oldIndex, int newIndex, MenuItem menuItem) {
        switch (newIndex) {
            case 0: {
                setFragment(new DashboardFragment());
                break;
            }
            case 1: {
                setFragment(new GiftsFragment());
                break;
            }
            case 2: {
                setFragment(new MailFragment());
                break;
            }
            case 3: {

                setFragment(new SettingsFragment());
                if(!permission_notif){
                    requestPermission();
                }else {
                    break;
                }

            }
        }
    }

    private void setFragment(Fragment fragment) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, fragment).commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull android.view.MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAnimationStart(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationEnd(int index, MenuItem menuItem) {

    }

    @Override
    public void onAnimationCancel(int index, MenuItem menuItem) {

    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(fAuth.getUid());

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("status", status);

        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
    }


    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        }else {
            super.onBackPressed();
        }
    }

}