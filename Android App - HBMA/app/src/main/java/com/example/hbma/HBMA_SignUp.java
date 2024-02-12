package com.example.hbma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hbma.databinding.ActivityHbmaSignUpBinding;
import com.example.hbma.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class HBMA_SignUp extends AppCompatActivity {
    ActivityHbmaSignUpBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHbmaSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(HBMA_SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        //Already have an account.
        binding.tvsignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HBMA_SignUp.this,HBMA_SignIn.class);
                startActivity(intent);
            }
        });




        //sign up or create a new user

        binding.btnsignup.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                if(binding.signetEmail.getText().toString().isEmpty() || binding.signetPassword.getText().toString().isEmpty() ||
                binding.userName.getText().toString().isEmpty()){

                    binding.txtemail.setText("Fill all the credentials");
                }
                else{
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(binding.signetEmail.getText().toString(),binding.signetPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            User user = new User(binding.userName.getText().toString(),binding.signetEmail.getText().toString(),binding.signetPassword.getText().toString());
                            String id = task.getResult().getUser().getUid();
                            firebaseDatabase.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(HBMA_SignUp.this, "User created successfully!", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(HBMA_SignUp.this,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }}
        });

    }



}