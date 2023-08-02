package com.example.instagramsample;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URI;

public class ProfileActivity extends AppCompatActivity {
    private TextView CDisplayName;
    private ImageView CPhoto;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        this.CDisplayName = this.findViewById(R.id.tv_display_name);
        this.CPhoto = this.findViewById(R.id.iv_profile_photo);

        this.activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if(result.getResultCode() == Activity.RESULT_OK) {
                            Intent CData = result.getData();
                            imageUri = CData.getData();
                            CPhoto.setImageURI(imageUri);
                        }
                    }
                }
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth CAuth = FirebaseAuth.getInstance();
        FirebaseUser CUser = CAuth.getCurrentUser();
        if(CUser != null) {
            this.CDisplayName.setText(CUser.getDisplayName());
        }
        else {
            this.finish();
        }
    }

    public void logout(View CView) {
        FirebaseAuth.getInstance().signOut();
        this.finish();
    }

    public void browse(View CView) {
        Intent CBrowseGallery = new Intent();
        CBrowseGallery.setAction(Intent.ACTION_GET_CONTENT);
        CBrowseGallery.setType("image/*");
        this.activityResultLauncher.launch(CBrowseGallery);
    }

    public void upload(View CView) {
        FirebaseStorage CStorage = FirebaseStorage.getInstance();
        StorageReference CStorageReference = CStorage.getReference();
        StorageReference CReference = CStorageReference.child("images/Sample.png");

        CReference.putFile(this.imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(ProfileActivity.this, "Image uploaded!", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}