package com.example.friendship;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText emailTextView, passwordTextView,fullname,branch,inputConfirmPassword;
    private Button Btn;
    ProgressBar progressBar ;
    TextView buttonSignIn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth fAuth;
    DatabaseReference myRef;



    private Button b;


    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public String userID;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.progressBar = findViewById(R.id.progressBarSignUp);
        progressBar.setVisibility(View.GONE);


        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        // initialising all views through id defined above
        fullname       = findViewById(R.id.inputFirstName);
        branch = findViewById(R.id.inputLastName);
        emailTextView        = findViewById(R.id.inputEmail);
        passwordTextView        = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        Btn         = findViewById(R.id.buttonSignUp);





        buttonSignIn= findViewById(R.id.textSignIn);
        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,test.class);
                startActivity(i);
            }
        });



        // Set on Click Listener on Registration button
        Btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {

                registerNewUser();
            }
        });
    }

    private void registerNewUser()
    {
        String email, password,name,branchs,confirmPass;
        email = emailTextView.getText().toString();
        password = passwordTextView.getText().toString();
        name = fullname.getText().toString();
        branchs = branch.getText().toString();
        confirmPass = inputConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this, "Enter first name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(branchs)) {
            Toast.makeText(SignUpActivity.this, "Enter Branch Details", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if (!email.contains("nitp.ac.in")) {
            Toast.makeText(SignUpActivity.this, "Enter NIT Patna email", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(SignUpActivity.this, "Enter password", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(confirmPass)) {
            Toast.makeText(SignUpActivity.this, "Confirm your password", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(confirmPass)) {
            Toast.makeText(SignUpActivity.this, "Password & confirm password must be same", Toast.LENGTH_SHORT).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            // create new user or register new user
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {

                            if (task.isSuccessful()) {




                                userID = mAuth.getCurrentUser().getUid();


                                myRef = database.getReference("students");







                                DocumentReference documentReference = fStore.collection("users").document(userID);
                                Map<String,Object> user = new HashMap<>();
                                user.put("fName",name);
                                user.put("email",email);
                                user.put("branch",branchs);
                                user.put("password",password);
                                user.put("userType","8");
                                user.put("detailsGiven","0");


                                Map<String,Object> users = new HashMap<>();
                                users.put("name",name);
                                users.put("userId",userID);
                                users.put("premium","8");
                                users.put("branch",branchs);
                                users.put("purl","");
                                users.put("shortBio","");
                                users.put("location","");
                                users.put("hobbies","");
                                users.put("books","");
                                users.put("foods","");
                                users.put("year","");
                                users.put("travellike","");
                                users.put("qualitylike","");
                                users.put("qualitydislike","");
                                users.put("birthday","");
                                users.put("connectionId","");


                                myRef.child(String.valueOf(userID)).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Data Saved",Toast.LENGTH_LONG).show();

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),"Check your network connection",Toast.LENGTH_LONG).show();

                                    }
                                });





                                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Log.d(TAG,"onSuccess: user Profile is created for "+ userID);
                                        progressBar.setVisibility(View.GONE);

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                                mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> tasks) {
                                        if(tasks.isSuccessful()){
                                            Toast.makeText(getApplicationContext(),"Registration successful! Please verify your Email id", Toast.LENGTH_SHORT).show();
                                            Intent intent
                                                    = new Intent(SignUpActivity.this,
                                                    test.class);
                                            startActivity(intent);

                                        }else{
                                            Toast.makeText(getApplicationContext(), "Failed to send verification link", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });

                                // hide the progress bar


                                // if the user created intent to login activity

                            }
                            else {

                                // Registration failed
                                Toast.makeText(
                                                getApplicationContext(),
                                                "User Already Exists",
                                                Toast.LENGTH_LONG)
                                        .show();

                                // hide the progress bar

                            }
                        }
                    });

        }

    }





}

