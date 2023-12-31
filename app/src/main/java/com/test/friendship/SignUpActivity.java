package com.test.friendship;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;
import android.widget.Button;

import com.airbnb.lottie.LottieAnimationView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import com.test.friendship.R;


public class SignUpActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    private EditText emailTextView, passwordTextView,fullname,inputConfirmPassword;
    Spinner branch;
    private Button Btn;
    ProgressBar progressBar ;
    String strbranchs;
    TextView buttonSignIn;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseAuth fAuth;
    DatabaseReference myRef;
    DatabaseReference unregistered= FirebaseDatabase.getInstance().getReference("unregistered");
    DatabaseReference mRef = database.getReference("Connection");




    private Button b;
//    LottieAnimationView frog1,frog2,frog3,frog4;



    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public String userID;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_signup);
        this.progressBar = findViewById(R.id.progressBarSignUp);
        progressBar.setVisibility(View.GONE);
        // taking FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();
//        frog1 = findViewById(R.id.frog1);
//        frog2= findViewById(R.id.frog2);
//        frog3 = findViewById(R.id.frog3);
//        frog4 = findViewById(R.id.frog4);
        // initialising all views through id defined above
        fullname       = findViewById(R.id.inputFirstName);
        branch = findViewById(R.id.branch);
        emailTextView        = findViewById(R.id.inputEmail);
        passwordTextView        = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        Btn         = findViewById(R.id.buttonSignUp);
//        Handler handler = new Handler();
//
//        handler.postDelayed(new Runnable() {
//            public void run() {
//
//
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

        branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strbranchs =parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("");
        arrayList.add("Electrical Engineering");
        arrayList.add("Computer Science and Engineering");
        arrayList.add("Electronics and communication Engineering");
        arrayList.add("Civil Engineering");
        arrayList.add("Architecture");
        arrayList.add("Mechanical Engineering");
        arrayList.add("Chemical Technology");
        arrayList.add("Integrated M.S.C");
        arrayList.add("Dual Degree");
        arrayList.add("Others");


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,arrayList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        branch.setAdapter(adapter);
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
        branchs = strbranchs;
        confirmPass = inputConfirmPassword.getText().toString();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(SignUpActivity.this, "Enter Your Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(branchs)) {
            Toast.makeText(SignUpActivity.this, "Enter Branch Details", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(email)) {
            Toast.makeText(SignUpActivity.this, "Enter email", Toast.LENGTH_SHORT).show();
        } else if ((!email.contains("nitp.ac.in")) && (!email.contains("nitrkl.ac.in"))) {
            Toast.makeText(SignUpActivity.this, "Enter Your College email", Toast.LENGTH_SHORT).show();
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

                                myRef = database.getReference("unregistered");

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                                HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("id", userID);
                                hashMap.put("username", name);
                                hashMap.put("imageURL", "default");
                                hashMap.put("status", "offline");
                                hashMap.put("bio", "");
                                hashMap.put("search", name.toLowerCase());
                                reference.setValue(hashMap);

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
                                users.put("likes","0");
                                users.put("premiumres","https://firebasestorage.googleapis.com/v0/b/friendship-c4818.appspot.com/o/vip.json?alt=media&token=df29f227-5d3e-440a-8572-c82ae93c21b7");
                                if(email.contains("nitp.ac.in")){users.put("college","NATIONAL INSTITUTE OF TECHNOLOGY PATNA"); }
                                else if (email.contains("nitrkl.ac.in")){users.put("college","NATIONAL INSTITUTE OF TECHNOLOGY ROURKELA");}else{
                                    users.put("college","");
                                }



                                myRef.child(String.valueOf(userID)).setValue(users).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(),"Updated",Toast.LENGTH_LONG).show();

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

