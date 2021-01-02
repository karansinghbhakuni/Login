package com.enzo.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {
    Button logout;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView display;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        logout=findViewById(R.id.btnlogout);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        display=findViewById(R.id.displayname);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
                sharedPreferences.edit().clear().commit();
                startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkuserstatus();
    }

    void checkuserstatus(){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        Boolean counter=sharedPreferences.getBoolean("logincounter",Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        String email=sharedPreferences.getString("useremail",String.valueOf(MODE_PRIVATE));
        if (counter){
            display.setText(email);
        }
        else{
            startActivity(new Intent(DashboardActivity.this,LoginActivity.class));
            finish();
        }
    }
}