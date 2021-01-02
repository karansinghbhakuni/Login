package com.enzo.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    Button loginbtn;
    EditText txtEmail,txtpass;
    TextView txtskip,txthelp,txtloginactivity;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginbtn=findViewById(R.id.btn_login);
        txtEmail=findViewById(R.id.txt_email);
        txtpass=findViewById(R.id.txt_pass);
        txtskip=findViewById(R.id.txt_skip);
        txthelp=findViewById(R.id.txt_help);
        txtloginactivity=findViewById(R.id.txt_registeractivity);

        mAuth=FirebaseAuth.getInstance();
        txtloginactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=txtEmail.getText().toString().trim();
                String password=txtpass.getText().toString().trim();
                loginuser(email,password);
            }
        });
        txthelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recoverpassword();
            }
        });
        txtskip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savedata("guest");
            }
        });




    }

    void loginuser(String email,String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            isverified();
                            savedata(email);


                        } else {
                            // If sign in fails, display a message to the user.

                        }
                        // ...
                    }
                });
    }
    void isverified(){
        FirebaseUser firebaseUser=mAuth.getCurrentUser();
        Boolean flag=firebaseUser.isEmailVerified();
        if(flag){

        }
        else{
            Toast.makeText(LoginActivity.this,"Please verify your email",Toast.LENGTH_SHORT).show();
            mAuth.signOut();
        }
    }

    void savedata(String email){
        SharedPreferences sharedPreferences=getSharedPreferences("logindata",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("logincounter",true);
        editor.putString("useremail",email);
        editor.apply();
        startActivity(new Intent(LoginActivity.this,DashboardActivity.class));
        finish();
    }






    void  recoverpassword(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        EditText mail= new EditText(this);
        mail.setHint("Enter your email");
        mail.setMinEms(16);

        linearLayout.addView(mail);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String txt=mail.getText().toString().trim();
                beginrecovery(txt);
            }
        });

        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    void beginrecovery(String email){
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this,"Email sent..",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(LoginActivity.this,"failed",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}