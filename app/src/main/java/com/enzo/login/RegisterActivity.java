package com.enzo.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    Button registerbtn;
    EditText txtEmail,txtpass,txtname,txtphone;
    TextView txtskip,txtregisteractivity;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerbtn=findViewById(R.id.btn_register);
        txtEmail=findViewById(R.id.txt_email);
        txtpass=findViewById(R.id.txt_pass);
        txtskip=findViewById(R.id.txt_skip);
        txtname=findViewById(R.id.txt_name);
        txtphone=findViewById(R.id.txt_phoneno);
        txtregisteractivity=findViewById(R.id.txt_loginactivity);
        mAuth=FirebaseAuth.getInstance();
        txtregisteractivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString().trim();
                String pass=txtpass.getText().toString().trim();
                String phone=txtphone.getText().toString().trim();
                String name=txtname.getText().toString().trim();
                registeruser(email,pass,phone,name);
            }
        });
    }
    void registeruser(String email,String password,String phone,String name){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();
                            String uid=user.getUid();
                            HashMap<Object,String> hashMap=new HashMap<>();
                            hashMap.put("email",email);
                            hashMap.put("uid",uid);
                            hashMap.put("name",name);
                            hashMap.put("phone",phone);
                            FirebaseDatabase database=FirebaseDatabase.getInstance();
                            DatabaseReference reference=database.getReference("user");
                            reference.child(uid).setValue(hashMap);
                            verification();
                        } else {

                        }

                        // ...
                    }
                });
    }
    void verification(){
        FirebaseUser firebaseUser= mAuth.getCurrentUser();
        if(firebaseUser!=null){

            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(RegisterActivity.this,"Verification mail sent",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        finish();
                    }
                }
            });
        }
    }
}