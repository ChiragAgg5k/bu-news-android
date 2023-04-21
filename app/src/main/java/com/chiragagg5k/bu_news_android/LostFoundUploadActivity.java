package com.chiragagg5k.bu_news_android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.chiragagg5k.bu_news_android.objects.LostFoundObject;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class LostFoundUploadActivity extends AppCompatActivity {

    ImageView backBtn;
    EditText title, description, location, contact;
    TextView selectImageLabel;
    Button selectImage, upload;
    DatabaseReference lostFoundDatabase, lostDatabase, foundDatabase;
    StorageReference storageRef;
    StorageTask uploadTask;
    Boolean isLost;
    Uri imageUri;

    FirebaseUser user;
    DatabaseReference userDatabase;

    ActivityResultLauncher<Intent> getImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(

            ),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        imageUri = data.getData();
                        String name = UtilityClass.queryName(getContentResolver(), imageUri);
                        selectImageLabel.setText(name);
                    }
                }
            });

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_lostfound);

        user = FirebaseAuth.getInstance().getCurrentUser();

        backBtn = findViewById(R.id.back_button);
        title = findViewById(R.id.lf_upload_item_name);
        description = findViewById(R.id.lf_upload_item_description);
        location = findViewById(R.id.lf_upload_item_location);
        contact = findViewById(R.id.lf_upload_item_contact);
        selectImage = findViewById(R.id.lf_upload_item_image_button);
        selectImageLabel = findViewById(R.id.selectedImageLabel);
        upload = findViewById(R.id.lf_upload_submit_button);

        isLost = getIntent().getBooleanExtra("isLost", false);

        backBtn.setOnClickListener(v -> finish());
        selectImage.setOnClickListener(v -> selectImage());

        lostFoundDatabase = FirebaseDatabase.getInstance().getReference("lost_found");
        storageRef = FirebaseStorage.getInstance().getReference("lost_found");

        lostDatabase = lostFoundDatabase.child("lost");
        foundDatabase = lostFoundDatabase.child("found");
        userDatabase = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contact.setText(snapshot.child("phoneNo").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        upload.setOnClickListener(v -> {
            String titleText = title.getText().toString();
            String descriptionText = description.getText().toString();
            String locationText = location.getText().toString();
            String contactText = contact.getText().toString();

            if (titleText.isEmpty()) {
                title.setError("Title is required");
                title.requestFocus();
                return;
            }

            if (descriptionText.isEmpty()) {
                description.setError("Description is required");
                description.requestFocus();
                return;
            }

            if (locationText.isEmpty()) {
                location.setError("Location is required");
                location.requestFocus();
                return;
            }

            if (imageUri == null) {
                selectImage.setError("Image is required");
                selectImage.requestFocus();
                return;
            }

            if (contactText.isEmpty()) {
                contact.setError("Contact is required");
                contact.requestFocus();
                return;
            }

            StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));


            uploadTask = fileReference.putFile(imageUri).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                        String key = lostDatabase.push().getKey();
                        LostFoundObject lostFoundItem = new LostFoundObject(titleText, descriptionText, locationText, contactText, uri.toString());
                        assert key != null;
                        if (isLost)
                            lostDatabase.child(key).setValue(lostFoundItem);
                        else
                            foundDatabase.child(key).setValue(lostFoundItem);

                        Toast.makeText(LostFoundUploadActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        finish();
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(LostFoundUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnProgressListener(snapshot -> {
                upload.setEnabled(false);
                upload.setText("Uploading...");

            }).addOnCompleteListener(task -> {
                upload.setEnabled(true);
                upload.setText("Upload");

            }).addOnFailureListener(e -> {
                Toast.makeText(LostFoundUploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                upload.setEnabled(true);
            });
        });
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImageActivityResultLauncher.launch(intent);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}
