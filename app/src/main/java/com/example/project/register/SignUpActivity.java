package com.example.project.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    protected static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private EditText emailedit;
    private EditText useredit;
    private  EditText passedit;
    private Button signupbut;
    private Button loginbutreg;
    DatabaseReference databaseReference;

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
                    emailedit.setError("Email is required");
                    emailedit.requestFocus();
                }

                if(passwords.isEmpty()){
                    passedit.setError("Password is requires");
                    passedit.requestFocus();
                }

                if(users.isEmpty()){
                    useredit.setError("Username is required");
                    useredit.requestFocus();
                }

                else  if(emails.isEmpty() && passwords.isEmpty() && users.isEmpty()){
                    Toast.makeText(SignUpActivity.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }

                else{
                mAuth.createUserWithEmailAndPassword(emails, passwords)
                        .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if(user!=null){
                                        Log.d(TAG,"userid"+user.getUid());
                                    }

                                    FirebaseDatabase.getInstance();

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName(users).build();

                                    user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
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
