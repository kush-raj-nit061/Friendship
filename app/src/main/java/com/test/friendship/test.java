package com.test.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
import com.test.friendship.BasicDetails.DetailsActivity1;
import com.test.friendship.BasicDetails.DetailsActivity2;
import com.test.friendship.BasicDetails.DetailsActivity5;
import com.test.friendship.MainActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import com.test.friendship.R;

public class test extends AppCompatActivity {
//    LottieAnimationView frog1,frog2,frog3,frog4;


    private EditText emailTextView, passwordTextView;
    private Button Btn;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    TextView tvForgot,tvSignup,tvReset;
    ProgressBar progressbar;

    private FirebaseAuth mAuth;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tester);
//        frog1 = findViewById(R.id.frog1);
//        frog2= findViewById(R.id.frog2);
//        frog3 = findViewById(R.id.frog3);
//        frog4 = findViewById(R.id.frog4);
        tvReset = findViewById(R.id.resetPassword);

//        Handler handler = new Handler();
//
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                frog2.setVisibility(View.VISIBLE);
//                frog2.playAnimation();
//                handler.postDelayed(new Runnable() {
//                    public void run() {
//
//                        frog3.playAnimation();
//
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                frog4.playAnimation();
//                            }
//                        },3000);
//                    }
//                }, 3000);
//            }
//        }, 3000);



        mAuth = FirebaseAuth.getInstance();
        tvSignup = findViewById(R.id.textSignUp);



        emailTextView = findViewById(R.id.email);
        passwordTextView = findViewById(R.id.password);
        Btn = findViewById(R.id.buttonSignIn);
//        tvForgot = findViewById(R.id.tvForgot);



        progressbar = findViewById(R.id.progressBarSignIn);
        progressbar.setVisibility(View.GONE);

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(test.this, SignUpActivity.class);
                startActivity(i);
            }
        });
//
        Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                loginUserAccount();
            }
        });
        tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(test.this, ForgotActivity.class);
                startActivity(i);
            }
        });
    }





    private void loginUserAccount()
    {


        String email, password;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter email!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                            "Please enter password!!",
                            Toast.LENGTH_LONG)
                    .show();
            return;
        }
        progressbar.setVisibility(View.VISIBLE);

        // signin existing user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(
                                    @NonNull Task<AuthResult> task)
                            {
                                if (task.isSuccessful()) {




                                    if(mAuth.getCurrentUser().isEmailVerified()){
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

                                                                            Intent in = new Intent(test.this, UnderMaintainance.class);
                                                                            startActivity(in);




                                                                        }else {
                                                                            if(detailsGiven.equals("1")){
                                                                                Intent intent
                                                                                        = new Intent(test.this,
                                                                                        MainActivity.class);
                                                                                Toast.makeText(getApplicationContext(),"Welcome🙏",Toast.LENGTH_LONG).show();
                                                                                startActivity(intent);
//                                                                finish();



                                                                            }else{
                                                                                if(mAuth.getCurrentUser().isEmailVerified()){
                                                                                    Intent intent
                                                                                            = new Intent(test.this,TermsAndCondition.class);
                                                                                    startActivity(intent);
//                                                                    finish();

                                                                                }

                                                                            }

                                                                        }
                                                                    }
                                                                }
                                                            });








                                                        }
                                                    }else {
                                                        progressbar.setVisibility(View.GONE);

                                                    }
                                                });

                                    }else {
                                        Toast.makeText(getApplicationContext(),"Please verify your Email id", Toast.LENGTH_LONG).show();

                                    }



                                    progressbar.setVisibility(View.GONE);

                                    String uid = mAuth.getCurrentUser().getUid();
                                    Map<String,Object> user =new HashMap<>();
                                    user.put("password",password);

                                    fStore.collection("users").document(uid).update(user);

                                }

                                else {

                                    // sign-in failed
                                    Toast.makeText(getApplicationContext(),
                                                    "Login Failed!!",
                                                    Toast.LENGTH_LONG)
                                            .show();

                                    // hide the progress bar
                                    progressbar.setVisibility(View.GONE);
                                }
                            }
                        });


    }

}