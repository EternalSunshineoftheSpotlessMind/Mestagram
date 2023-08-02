package com.example.instagramsample;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText CEmail;
    private EditText CPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseAuth.getInstance().signOut();

        this.CEmail = this.findViewById(R.id.et_login_email);
        this.CPassword = this.findViewById(R.id.et_login_password);
        this.buttonLogin = this.findViewById(R.id.btn_login_login);

        this.buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;

                email = String.valueOf(CEmail.getText());
                password = String.valueOf(CPassword.getText());

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Fill-up all fields.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth CAuth = FirebaseAuth.getInstance();
        FirebaseUser CUser = CAuth.getCurrentUser();
        if(CUser != null) {
            this.launchProfileActivity();
        }
    }

    public void launchRegisterActivity(View CView) {
        Intent CIntent = new Intent(MainActivity.this, RegisterActivity.class);
        this.startActivity(CIntent);
    }

    public void login(View CView) {
        FirebaseAuth CAuth = FirebaseAuth.getInstance();

        String strEmail = this.CEmail.getText().toString().trim();
        String strPassword = this.CPassword.getText().toString().trim();

        // Verify values of strEmail and strPassword.

        CAuth.signInWithEmailAndPassword(strEmail, strPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser CUser = CAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_LONG).show();
                            launchProfileActivity();
                        }
                        else {
                            promptError();
                        }
                    }
                });
    }

    private void launchProfileActivity() {
        Intent CIntent = new Intent(MainActivity.this, ProfileActivity.class);
        this.startActivity(CIntent);
    }

    private void promptError() {
        Toast.makeText(MainActivity.this, "FAILED TO LOGIN USER", Toast.LENGTH_LONG).show();
    }
}