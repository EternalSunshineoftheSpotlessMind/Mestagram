package com.example.instagramsample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private EditText CEmail;
    private EditText CUsername;
    private EditText CPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.CEmail = this.findViewById(R.id.et_register_email);
        this.CUsername = this.findViewById(R.id.et_register_username);
        this.CPassword = this.findViewById(R.id.et_register_password);
    }

    public void returnToMainActivity(View CView) {
        this.finish();
    }

    public void register(View CView) {
        this.createUser();
    }

    private void createUser() {
        FirebaseAuth CAuth = FirebaseAuth.getInstance();

        String strEmail = this.CEmail.getText().toString().trim();
        String strUsername = this.CUsername.getText().toString().trim();
        String strPassword = this.CPassword.getText().toString().trim();

        // Check the values of strEmail, strUsername, and strPassword.


        CAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Log.d("FBAUTH", "Successful");
                            FirebaseUser CUser = CAuth.getCurrentUser();

                            UserProfileChangeRequest CProfileChange = new UserProfileChangeRequest.Builder().setDisplayName(strUsername).build();
                            CUser.updateProfile(CProfileChange);
                            returnToLogin();
                        }
                        else {
                            Log.d("FBAUTH", "Error");
                            promptError();
                        }
                    }
                });

    }

    private void returnToLogin() {
        this.finish();
    }

    private void promptError() {
        Toast.makeText(RegisterActivity.this, "FAILED TO CREATE USER", Toast.LENGTH_LONG).show();
    }
}