package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {



    LottieAnimationView frog1,frog2,frog3,frog4;


    private EditText emailTextView, passwordTextView;
    private Button Btn;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    TextView tvForgot,tvSignup;
    private FirebaseAuth mAuth= FirebaseAuth.getInstance();
    ProgressBar progressbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



    }
    @Override
    protected void onStart() {

        super.onStart();
        if(mAuth.getCurrentUser() != null && mAuth.getCurrentUser().isEmailVerified()){

            fStore.collection("users").document(mAuth.getCurrentUser().getUid())
                    .get().addOnCompleteListener(tasks -> {
                        if (tasks.isSuccessful()) {
                            DocumentSnapshot document = tasks.getResult();
                            if (document.exists()) {
                                String detailsGiven = document.getString("detailsGiven");
                                FirebaseFirestore fMaintainance = FirebaseFirestore.getInstance();
                                fMaintainance.collection("Friends").document("underMaintainance").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task2) {
                                        DocumentSnapshot maintainance = task2.getResult();
                                        if(maintainance.exists()){
                                            String num = maintainance.getString("maintainance");

                                            if(num.equals("1")){
                                                Intent in = new Intent(SplashScreenActivity.this, UnderMaintainance.class);
                                                startActivity(in);
                                                finish();

                                            }else {
                                                if(detailsGiven.equals("1")){
                                                    FirebaseAuth.getInstance().getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            Intent intent
                                                                    = new Intent(SplashScreenActivity.this,
                                                                    MainActivity.class);
                                                            Toast.makeText(getApplicationContext(),"Welcome🙏",Toast.LENGTH_LONG).show();
                                                            startActivity(intent);
                                                            finish();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(getApplicationContext(),"Your Account Has been Suspended",Toast.LENGTH_LONG).show();
                                                        }
                                                    });



                                                }else{
                                                    if(mAuth.getCurrentUser().isEmailVerified()){
                                                        Intent intent
                                                                = new Intent(SplashScreenActivity.this,TermsAndCondition.class);
                                                        startActivity(intent);
                                                        finish();
//                                                                    finish();

                                                    }

                                                }

                                            }
                                        }
                                    }
                                });








                            }
                        }else {


                        }
                    });

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(SplashScreenActivity.this, test.class);
                    startActivity(i);
                    finish();
                }
            }, 1500);
        }



    }
}