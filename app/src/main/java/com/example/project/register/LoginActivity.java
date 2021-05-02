package com.example.project.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.Beacon.BeaconDetect;
import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Tag;

public class LoginActivity extends AppCompatActivity {
    protected static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private EditText emaillogin;
    private EditText passlogin;
    private Button loginbut;
    private Button regbutlog;
    private Button forgotpass;
    private CheckBox remember;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME="Presfile";
    private FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance();
        emaillogin=(EditText) findViewById(R.id.editemaillogin);
        passlogin=(EditText) findViewById(R.id.editpasslogin);
        loginbut=(Button) findViewById(R.id.butlogin);
        remember=(CheckBox) findViewById(R.id.rememberme);


        SharedPreferences sp=getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        if(sp.contains("pref_name")){
            String u=sp.getString("pref_name", "not found");
            emaillogin.setText(u.toString());
        }

        if(sp.contains("pref_pass")){
            String u=sp.getString("pref_pass", "not found");
            passlogin.setText(u.toString());
        }

        if(sp.contains("pref_check")){
            Boolean b=sp.getBoolean("pref_check", false);
            remember.setChecked(b);
        }

        forgotpass=(Button) findViewById(R.id.butforgot);
        forgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails=emaillogin.getText().toString();

                if(emails.isEmpty()){
                    emaillogin.setError("Please Enter Email Address");
                    emaillogin.requestFocus();
                }

                else {
                    mAuth.sendPasswordResetEmail(emails).addOnCompleteListener(LoginActivity.this, (task) -> {
                        if (!task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "Unable to send reset email", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Toast.makeText(LoginActivity.this, "Reset link sent to your email", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });

        regbutlog=(Button) findViewById(R.id.signupbutt);
        regbutlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });


        loginbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails=emaillogin.getText().toString();
                String passwords=passlogin.getText().toString();

                if(emails.isEmpty()){
                    emaillogin.setError("Email is required");
                    emaillogin.requestFocus();
                }

                if(passwords.isEmpty())
                {
                    passlogin.setError("Password is required");
                    passlogin.requestFocus();
                }

                else if(emails.isEmpty() && passwords.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                }

                else{
                    if(remember.isChecked()){
                        Boolean boolIsChecked = remember.isChecked();
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("pref_name",emaillogin.getText().toString());
                        editor.putString("pref_pass",passlogin.getText().toString());
                        editor.putBoolean("pref_check", boolIsChecked);
                        editor.apply();
                    }

                    else{
                        sharedPreferences.edit().clear().apply();
                    }

                    mAuth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                FirebaseUser user = mAuth.getCurrentUser();
                                if(user!=null){
                                    Log.d(TAG,"userid: "+user.getUid());
                                }


                                Toast.makeText(LoginActivity.this, "Login in successfully", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(getApplicationContext(), BeaconDetect.class));
                                //Intent intent = new Intent(LoginActivity.this, BeaconDetect.class);
                                //startActivity(intent);
                            }
                        }
                    });



                }
            }

        });
    }}