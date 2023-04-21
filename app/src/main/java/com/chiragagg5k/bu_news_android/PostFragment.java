package com.chiragagg5k.bu_news_android;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.chiragagg5k.bu_news_android.objects.NewsObject;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
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
import com.squareup.picasso.Picasso;

@SuppressWarnings("rawtypes")
public class PostFragment extends Fragment {

    TextView image_status, what_to_post_tv;
    EditText heading, description;
    Button choose_image, post_button;
    ImageView preview_image;
    StorageReference storageRef;
    DatabaseReference databaseRef, userRef;
    StorageTask uploadTask;
    Uri image_uri;
    Spinner category_spinner;
    ArrayAdapter<CharSequence> category_adapter;
    FirebaseUser user;

    /**
     * This is the callback for the result of the activity started by selectImage()
     */
    ActivityResultLauncher<Intent> getImageActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(

            ),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        image_uri = data.getData();

                        String name = UtilityClass.queryName(requireActivity().getContentResolver(), image_uri);
                        image_status.setText(getResources().getString(R.string.selected_image, name));
                        Picasso.get().load(image_uri).into(preview_image);
                        preview_image.setVisibility(View.VISIBLE);
                    }
                }
            });

    public PostFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        heading = view.findViewById(R.id.post_title_et);
        description = view.findViewById(R.id.post_description_et);
        choose_image = view.findViewById(R.id.post_image_btn);
        post_button = view.findViewById(R.id.post_btn);
        image_status = view.findViewById(R.id.selected_image_tv);
        category_spinner = view.findViewById(R.id.categories_spinner);
        preview_image = view.findViewById(R.id.preview_image);
        what_to_post_tv = view.findViewById(R.id.what_to_post_tv);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference("users").child(user.getUid());

        category_adapter = ArrayAdapter.createFromResource(getContext(), R.array.choice_category_names, android.R.layout.simple_spinner_item);
        category_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category_spinner.setAdapter(category_adapter);

        choose_image.setOnClickListener(v -> selectImage());

        post_button.setOnClickListener(v -> {

            // to avoid multiple uploads
            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(requireActivity(), "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                post();
            }

        });

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot lastPostTimeSnapshot = snapshot.child("lastPostTime");
                DataSnapshot isAdminSnapshot = snapshot.child("admin");
                Boolean isAdmin = isAdminSnapshot.getValue(Boolean.class);

                if (isAdmin != null && isAdmin) {
                    what_to_post_tv.setText("You are an admin. You can post anything you want without cooldown.");
                    return;
                }

                if (!lastPostTimeSnapshot.exists()) {
                    return;
                }

                long lastPostTime = lastPostTimeSnapshot.getValue(Long.class);
                long currentTime = System.currentTimeMillis();
                long timeDiff = currentTime - lastPostTime;
                if (timeDiff < 7200000) {
                    post_button.setEnabled(false);
                    choose_image.setEnabled(false);

                    try {
                        post_button.setBackgroundColor(getResources().getColor(R.color.backgroundColorDarker));
                        choose_image.setBackgroundColor(getResources().getColor(R.color.backgroundColorDarker));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    what_to_post_tv.setText("Sorry for not informing in advance that posting has a cooldown of 2hrs! You can post again in " + (120 - timeDiff / 60000) + " minutes. Please wait.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Start a new Intent to select an image from the gallery
     */
    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getImageActivityResultLauncher.launch(intent);
    }

    // idk wtf is this. i just know it gets the file extension
    private String getFileExtension(Uri uri) {
        ContentResolver cR = requireActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /**
     * Upload the image to the firebase storage and the details to the firebase database using UploadObject
     */
    private void post() {

        if (heading.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter a heading", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(heading);

        } else if (description.getText().toString().isEmpty()) {
            Toast.makeText(requireActivity(), "Please enter a description", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(description);

        } else if (image_uri == null) {
            Toast.makeText(requireActivity(), "Please select an image", Toast.LENGTH_SHORT).show();
            YoYo.with(Techniques.Shake)
                    .duration(700)
                    .repeat(1)
                    .playOn(choose_image);

        } else {
            // a big number + file extension (jpg or whatever)
            StorageReference fileReference = storageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(image_uri));

            uploadTask = fileReference.putFile(image_uri)
                    .addOnSuccessListener(taskSnapshot -> {

                        post_button.setEnabled(true);
                        post_button.setText(getResources().getString(R.string.post));
                        Toast.makeText(requireActivity(), "Upload successful", Toast.LENGTH_SHORT).show();

                        // get the download url of the image
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            NewsObject upload = new NewsObject(heading.getText().toString(), description.getText().toString(), category_spinner.getSelectedItem().toString(), uri.toString(), UtilityClass.getCurrentDateInMilliSeconds());
                            String uploadId = databaseRef.push().getKey();
                            if (uploadId != null) databaseRef.child(uploadId).setValue(upload);
                        });

                        // update the last post time
                        userRef.child("lastPostTime").setValue(System.currentTimeMillis());

                        // clear the fields after 1 seconds
                        new Handler().postDelayed(() -> {
                            heading.setText("");
                            description.setText("");
                            image_status.setText(getResources().getString(R.string.no_image_selected));
                            image_uri = null;
                            preview_image.setVisibility(View.GONE);
                        }, 1000);

                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(requireActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        YoYo.with(Techniques.Shake)
                                .duration(700)
                                .repeat(1)
                                .playOn(post_button);
                        post_button.setEnabled(true);
                        post_button.setText(getResources().getString(R.string.post));
                    })
                    .addOnProgressListener(snapshot -> {
                        post_button.setEnabled(false);
                        post_button.setText(getResources().getString(R.string.post_loading));
                    });
        }
    }
}