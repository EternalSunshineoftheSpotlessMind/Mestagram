package com.example.instagramsample;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    private EditText CDescription;
    private Button buttonReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.CEmail = this.findViewById(R.id.et_register_email);
        this.CUsername = this.findViewById(R.id.et_register_username);
        this.CPassword = this.findViewById(R.id.et_register_password);
        this.CDescription = this.findViewById(R.id.et_register_description);
        this.buttonReg = this.findViewById(R.id.btn_register_register);

        this.buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, username, password, description;

                email = String.valueOf(CEmail.getText());
                username = String.valueOf(CUsername.getText());
                password = String.valueOf(CPassword.getText());
                description = String.valueOf(CDescription.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || TextUtils.isEmpty(password) || TextUtils.isEmpty(description)) {
                    Toast.makeText(RegisterActivity.this, "Fill-up all fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 8) {
                    Toast.makeText(RegisterActivity.this, "Password length must be at least 8 characters.", Toast.LENGTH_SHORT).show();
                    return;
                }

                createUser();
            }
        });
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
        String strDescription = this.CDescription.getText().toString().trim();

        CAuth.createUserWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser CUser = CAuth.getCurrentUser();
                            UserProfileChangeRequest CProfileChange = new UserProfileChangeRequest.Builder().setDisplayName(strUsername).build();
                            CUser.updateProfile(CProfileChange);

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FBAUTH", "Successful");
                            Toast.makeText(RegisterActivity.this, "ACCOUNT CREATED", Toast.LENGTH_LONG).show();

                            returnToLogin();
                        }
                        else {
                            // If sign in fails, display a message to the user.
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