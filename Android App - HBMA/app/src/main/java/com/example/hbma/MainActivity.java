package com.example.hbma;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hbma.databinding.ActivityMainBinding;
import com.example.hbma.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    FirebaseAuth mAuth;
    Dialog dialog;
    FirebaseDatabase database;

    String Relatives_no;
    String location;
    DatabaseReference reference;
    ActivityMainBinding binding;
    Ringtone r;
    NavigationView navigationView;
    Handler handler;
    ActionBarDrawerToggle drawerToggle;
    boolean consicious = false;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        drawerToggle = new ActionBarDrawerToggle(this,binding.drawer,R.string.open,R.string.close);
        binding.drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.nav.bringToFront();




         handler = new Handler();
        //Pop up dialog box.
        dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_bg));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button yes = dialog.findViewById(R.id.btn_okay);
        Button no = dialog.findViewById(R.id.btn_cancel);


        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consicious = true;

                if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                    sendSMS();
                    sendSMS1();
                }
                else{

                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                    sendSMS();
                    sendSMS1();
                }

               // Toast.makeText(MainActivity.this, "Connecting with Hospital ....", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                r.stop();
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                consicious = true;
                dialog.dismiss();
                r.stop();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Profile");
        reference.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot snapshot = task.getResult();



                        String profilepic1 = String.valueOf(snapshot.child("profilepic").getValue());
                        Relatives_no = String.valueOf(snapshot.child("relativeNo").getValue()).toString();

                        Picasso.get().load(profilepic1).placeholder(R.drawable.profile).into(binding.profile);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "result not exits", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home: {
                        Intent intent = new Intent(MainActivity.this,HBMA_SignIn.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.nav_backup:{
                        startActivity(new Intent(MainActivity.this,records.class));
                        //Toast.makeText(MainActivity.this, "backup", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_profile:{
                        Intent intent = new Intent(MainActivity.this,Profile.class);
                        startActivity(intent);

                        break;
                    }
                    case R.id.nav_aboutus:{
                        Toast.makeText(MainActivity.this, "aboutus", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.nav_logout:{
                        mAuth.signOut();
                        Intent intent = new Intent(MainActivity.this,HBMA_SignIn.class);
                        startActivity(intent);
                        finish();
                        break;
                    }
                    default:{
                        Toast.makeText(MainActivity.this, "default", Toast.LENGTH_SHORT).show();
                    }

                }
                return true;
            }
        });
       // addNodeMCU();
        updateNavigationHeader();
        MainFunction();


    }

    @Override
    public void onBackPressed() {
        if(binding.drawer.isDrawerOpen(GravityCompat.START)){
            binding.drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    public void updateNavigationHeader(){
        View headerView = binding.nav.getHeaderView(0);
        TextView username = headerView.findViewById(R.id.userName);
        TextView email = headerView.findViewById(R.id.email);
        ImageView image = headerView.findViewById(R.id.profilepic);

        reference = FirebaseDatabase.getInstance().getReference("Profile");
        reference.child(mAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().exists()){
                        DataSnapshot snapshot = task.getResult();
                        String uname = String.valueOf(snapshot.child("username").getValue());
                        String email1 = String.valueOf(snapshot.child("email").getValue());
                        String profilepic1 = String.valueOf(snapshot.child("profilepic").getValue());
                        username.setText(uname);
                        email.setText(email1);
                        Picasso.get().load(profilepic1).placeholder(R.drawable.profile).into(image);
                    }
                    else{
                        Toast.makeText(MainActivity.this, "result not exits", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();
                }
            }
        });

      //  binding.bp.setText("");





    }


    public void MainFunction(){
        //Toast.makeText(this,mAuth.getUid(), Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference("NodeMCU").child(mAuth.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String bp = snapshot.child("bloodPressure").getValue().toString();
                location = snapshot.child("location").getValue().toString();
                binding.bp.setText(bp+" ppm");
                int c = Integer.parseInt(bp);
                if(c > 130){
                    User users = new User();
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    String currentDateAndTime = sdf.format(new Date());

                    users.setTime(currentDateAndTime);
                    users.setExceed(bp);


                    database.getReference().child("Exceeding").child(mAuth.getUid()).setValue(users);

                    try {
                        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
                        r.play();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                dialog.show();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if(dialog.isShowing()){
                            dialog.dismiss();
                            if(!dialog.isShowing()){
                                if(consicious == false){
                                    if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
                                        sendSMS();
                                        sendSMS1();
                                    }
                                    else{

                                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.SEND_SMS},100);
                                        sendSMS();
                                        sendSMS1();
                                    }

                                    Toast.makeText(MainActivity.this, "Connecting with Hospital ....", Toast.LENGTH_SHORT).show();
                                r.stop();
                            }}
                        }
                    }
                },10000);

            }

            else {
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void addNodeMCU(){
        User users = new User();
        users.setBloodPressure("74");
        users.setLocation("41°24'12.2 N 2°10'26.5 E");
        database.getReference().child("NodeMCU").child(mAuth.getUid()).setValue(users);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            sendSMS();
            sendSMS1();
        }
        else{
            Toast.makeText(this, "Permission is denied!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendSMS(){

        String sms = "Hello sir, you relative needs a medical attention!,his location is:";

        String pno = Relatives_no;
        if(!sms.isEmpty() && !pno.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+91"+pno, null, sms, null, null);
            Toast.makeText(this,"Calling for help", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendSMS1(){
        String lo = location;


        String pno = Relatives_no;
        if(!lo.isEmpty() && !pno.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+91"+pno, null, lo, null, null);
            Toast.makeText(this,"Calling for help", Toast.LENGTH_SHORT).show();
        }
    }
}