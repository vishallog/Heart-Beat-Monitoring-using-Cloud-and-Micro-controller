package com.example.hbma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.hbma.databinding.ActivityRecordsBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class records extends AppCompatActivity {
    ActivityRecordsBinding binding;
    DatabaseReference database;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecordsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        database = FirebaseDatabase.getInstance().getReference("Exceeding").child(auth.getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String bp = snapshot.child("exceed").getValue().toString();
                String time = snapshot.child("time").getValue().toString();

                binding.bpexceeds1.setText(bp);
                binding.date1.setText(time);

                String exceed2 = (String) binding.bpexceeds2.getText();
                String date2 = (String) binding.date2.getText();
                binding.bpexceeds2.setText(binding.bpexceeds1.getText());
                binding.date2.setText(binding.date1.getText());

                String exceed3 = (String) binding.bpexceeds3.getText();
                String date3 = (String) binding.date3.getText();

                binding.bpexceeds3.setText(exceed2);
                binding.date3.setText(date2);

                String exceed4 = (String) binding.bpexceeds4.getText();
                String date4 = (String) binding.date4.getText();

                binding.bpexceeds4.setText(exceed3);
                binding.date4.setText(date3);

                String exceed5 = (String) binding.bpexceeds5.getText();
                String date5 = (String) binding.date5.getText();

                binding.bpexceeds5.setText(exceed4);
                binding.date5.setText(date4);

                String exceed6 = (String) binding.bpexceeds6.getText();
                String date6 = (String) binding.date6.getText();

                binding.bpexceeds6.setText(exceed5);
                binding.date6.setText(date5);


                String exceed7 = (String) binding.bpexceeds7.getText();
                String date7 = (String) binding.date7.getText();

                binding.bpexceeds7.setText(exceed6);
                binding.date7.setText(date6);



                String exceed8 = (String) binding.bpexceeds8.getText();
                String date8 = (String) binding.date8.getText();

                binding.bpexceeds8.setText(exceed7);
                binding.date8.setText(date7);



                String exceed9 = (String) binding.bpexceeds9.getText();
                String date9 = (String) binding.date9.getText();

                binding.bpexceeds9.setText(exceed8);
                binding.date9.setText(date8);





                binding.bpexceeds7.setText(exceed9);
                binding.date7.setText(date9);










            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}