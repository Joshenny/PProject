package com.example.project.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText emaillogin;
    private EditText passlogin;
    private Button loginbut;
    private Button regbutlog;
    private Button forgotpass;
    private CheckBox remember;
    private FirebaseAuth.AuthStateListener authStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        emaillogin=(EditText) findViewById(R.id.editemaillogin);
        passlogin=(EditText) findViewById(R.id.editpasslogin);
        loginbut=(Button) findViewById(R.id.butlogin);
        remember=(CheckBox) findViewById(R.id.rememberme);

        SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
        String checkbox=preferences.getString("remember","");
        String account2=preferences.getString("account2","");
        String password2=preferences.getString("password2","");
        if(checkbox.equals("true")){
            emaillogin.setText(account2);
            passlogin.setText(password2);
            remember.setChecked(true);
        }
        else if(checkbox.equals("false"))
        {
            Toast.makeText(LoginActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
            remember.setChecked(false);
        }

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences preferences= getSharedPreferences("checkbox", MODE_PRIVATE);
                SharedPreferences.Editor editor= preferences.edit();
                if(buttonView.isChecked()){
                    editor.putString("remember","true");
                    editor.putString("account2",emaillogin.getText().toString());
                    editor.putString("password2",passlogin.getText().toString());
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Checked", Toast.LENGTH_SHORT).show();
                }

                else if(!buttonView.isChecked()){
                    editor.putString("remember", "false");
                    editor.remove("account2");
                    editor.remove("password2");
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Unchecked", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
            }
        });

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
                    emaillogin.setError("Please Enter Email Address");
                    emaillogin.requestFocus();
                }

                if(passwords.isEmpty())
                {
                    passlogin.setError("Please Enter Password");
                    passlogin.requestFocus();
                }

                else if(emails.isEmpty() && passwords.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
                }

                else{
                    mAuth.signInWithEmailAndPassword(emails,passwords).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()){
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Intent intent = new Intent(LoginActivity.this, BeaconDetect.class);
                                startActivity(intent);
                            }
                        }
                    });



                }
            }

        });
    }}