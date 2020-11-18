package com.example.vhackathonevents;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    Button loginButton;
    EditText emailId ;
    EditText password ;
    String emailIdText , passwordText ;
    FirebaseAuth mauth;
    FirebaseFirestore fireStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailId = findViewById(R.id.loginId);
        password = findViewById(R.id.loginPassword);
        mauth = FirebaseAuth.getInstance() ;
        fireStore = FirebaseFirestore.getInstance();
    }

    public void loginProcess(View view){
        emailIdText = emailId.getText().toString();
        passwordText = password.getText().toString();

        if(TextUtils.isEmpty(emailIdText)) {
            emailId.setError("enter your id");
            return;
        }
        if(TextUtils.isEmpty(passwordText)) {
            password.setError("enter your id");
            return;
        }
        // pop dialogBox

        mauth.signInWithEmailAndPassword(emailIdText ,passwordText).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if user not verified
               /* if(!user.isEmailVerified()){
                    Toast.makeText(LoginActivity.this, "Email Not Verified", Toast.LENGTH_SHORT).show();
                    verificationDialogBox.closeDialogBox();
                    return;
                }*/

                if (task.isSuccessful() ) {
                    Toast.makeText(LoginActivity.this, "login Successful...", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    finish();
                } else{
                    Toast.makeText(LoginActivity.this, "Check password/email", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e.getMessage());
            }
        });
    }

}