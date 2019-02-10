package com.example.intrahackathon;

import androidx.annotation.*;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Button signup;
    private EditText email;
    private EditText password;
    private TextView login;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        signup = (Button) findViewById(R.id.signup);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        login = (TextView) findViewById(R.id.login);
        database = FirebaseDatabase.getInstance();
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        signup.setOnClickListener(this);
        login.setOnClickListener(this);


    }

    private void usersignup(){
        final String mailId = email.getText().toString().trim();
        String pass = password.getText().toString().trim();

        if(TextUtils.isEmpty(mailId)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG);
            return;
        }
        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Please enter a password",Toast.LENGTH_SHORT);
            return;
        }

        progressDialog.setMessage("Registering please wait ..........");
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(mailId,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            DatabaseReference users = database.getReference("users").child(task.getResult().getUser().getUid().toString());
                            User newuser = new User(users.getKey(), mailId, new ArrayList<String>(Arrays.asList("dummy"))
                                    ,new ArrayList<String>(Arrays.asList("dummy")),
                                    new ArrayList<String>(Arrays.asList("dummy"))
                                    , 0);
                            users.setValue(newuser);
                            //profile activity
                            Toast.makeText(SignupActivity.this,"Registered successfully",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this,MainActivity.class));
                        }
                        else{
                            Toast.makeText(SignupActivity.this,"Something happenned",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == signup){
            usersignup();
        }
        if(v == login){
            finish();
            startActivity(new Intent(this,MainActivity.class));
        }
    }
}