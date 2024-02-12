package com.example.hbma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hbma.databinding.ActivityHbmaSignInBinding;
import com.example.hbma.databinding.ActivityHbmaSignUpBinding;
import com.example.hbma.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class HBMA_SignIn extends AppCompatActivity {
    ActivityHbmaSignInBinding binding;
    private FirebaseAuth mAuth;

    FirebaseDatabase database;
    ProgressDialog progressDialog;
    GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHbmaSignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();


        //Progress bar..
        progressDialog = new ProgressDialog(HBMA_SignIn.this);
        progressDialog.setTitle("Sign in");
        progressDialog.setMessage("We're signing in your account");



        //Signup.
        binding.tvsignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HBMA_SignIn.this,HBMA_SignUp.class);
                startActivity(intent);
            }
        });


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();



        googleSignInClient = GoogleSignIn.getClient(this ,gso);

        //Sign in Button Events.
        binding.btnsingIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (binding.loginetEmail.getText().toString().isEmpty() || binding.loginpassword.getText().toString().isEmpty()) {
                    binding.txtemail.setText("Fill all the credentials");
                } else {
                    progressDialog.show();
                    mAuth.signInWithEmailAndPassword(binding.loginetEmail.getText().toString(),
                            binding.loginpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Intent intent = new Intent(HBMA_SignIn.this, MainActivity.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(HBMA_SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


        //Sign in using Google.
        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
                progressDialog.show();


            }
        });

        //check the users availability.
        if(mAuth.getCurrentUser() != null){
            Intent intent = new Intent(HBMA_SignIn.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            //Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show();
        }
    }
    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG","firebaseAuthWithGoogle"+ account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Log.w("TAG","Google sign in Failed",e);
                throw new RuntimeException(e);
            }


        }
        else{
            startActivity(new Intent(HBMA_SignIn.this,HBMA_SignIn.class));
        }
    }






    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,  null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Log.d("TAG", "signInWithCredential:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    User users = new User();
                    users.setUserId(user.getUid());
                    users.setUsername(user.getDisplayName());
                    users.setProfilepic(user.getPhotoUrl().toString());
                    database.getReference().child("Users").child(user.getUid()).setValue(users);

                    //Toast.makeText(SignInActivity.this, "Passed", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(HBMA_SignIn.this,MainActivity.class));
                    finish();
                    // updateUI(user);
                }
                else{
                    Toast.makeText(HBMA_SignIn.this, "Failed", Toast.LENGTH_SHORT).show();
                    Log.w("TAG", "signInWithCredentials:failure",task.getException());
                    startActivity(new Intent(HBMA_SignIn.this,HBMA_SignIn.class));
                    //     Snackbar.make(binding.,"Authentication Failed.",Snackbar.LENGTH_SHORT).show();
                    //  updateI(null);
                }
            }
        });
    }


}