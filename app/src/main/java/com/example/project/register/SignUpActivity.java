package com.example.project.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.project.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailedit;
    private EditText useredit;
    private  EditText passedit;
    private Button signupbut;
    private FirebaseUser user;
    private String emails;
    private String passwords;
    private String phones;
    private Button loginbutreg;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth=FirebaseAuth.getInstance();
        emailedit=(EditText) findViewById(R.id.emailsignup);
        passedit=(EditText) findViewById(R.id.passwordsignup);
        useredit=(EditText)findViewById(R.id.usernamesignup);

        loginbutreg=(Button) findViewById(R.id.loginbutt);
        loginbutreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

        signupbut=(Button) findViewById(R.id.signupreg);
        signupbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emails=emailedit.getText().toString();
                String passwords = passedit.getText().toString();
                String users= useredit.getText().toString();

                if(emails.isEmpty())
                {
                    emailedit.setError("Invalid Email");
                    emailedit.requestFocus();
                }

                if(passwords.isEmpty()){
                    passedit.setError("Please Enter Password");
                    passedit.requestFocus();
                }

                else  if(emails.isEmpty() && passwords.isEmpty()){

                    Toast.makeText(SignUpActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();

                }

                else{
                mAuth.createUserWithEmailAndPassword(emails, passwords)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(users).build();

                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(SignUpActivity.this, "username stored successful", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });

                                    startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                    Toast.makeText(SignUpActivity.this, "Registration is successful", Toast.LENGTH_SHORT).show();

                                } else {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }}
        });
    }
}
