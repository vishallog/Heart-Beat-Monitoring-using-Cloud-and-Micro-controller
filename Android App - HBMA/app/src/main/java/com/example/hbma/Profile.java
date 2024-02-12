package com.example.hbma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.hbma.databinding.ActivityProfileBinding;
import com.example.hbma.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    ActivityProfileBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        binding.email.setText(user.getEmail());


        reference = FirebaseDatabase.getInstance().getReference("Profile");
        reference.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot snapshot = task.getResult();
                        String uname = String.valueOf(snapshot.child("username").getValue());


                        String profilepic1 = String.valueOf(snapshot.child("profilepic").getValue());
                        binding.User.setText(uname);
                        Picasso.get().load(profilepic1).placeholder(R.drawable.profile).into(binding.profile);
                    }
                    else{
                        Toast.makeText(Profile.this, "result not exits", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Profile.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.userName.getText().toString().equals("") || binding.email.getText().toString().equals("") || binding.profilepic.getText().toString().equals("") ||
                        binding.phoneNo.getText().toString().equals("") || binding.relativeNo.getText().toString().equals("")){
                    Toast.makeText(Profile.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
                else{

                User users = new User();
                users.setProfilepic(binding.profilepic.getText().toString());
                users.setUsername(binding.userName.getText().toString());
                users.setEmail(user.getEmail());
                users.setUserId(user.getUid());
                users.setRelativeNo(binding.relativeNo.getText().toString());
                users.setPhoneNo(binding.phoneNo.getText().toString());

                database.getReference().child("Profile").child(user.getUid()).setValue(users);
                startActivity(new Intent(Profile.this,MainActivity.class));
                finish();


            }}
        });

    }


}