package com.example.project_acadeamease1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.Calendar;

public class MainActivity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        // Get the current Firebase user
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        CharSequence todayDate = DateFormat.format("EEEE, MMMM d", calendar);

        // Set "Today, Month Day" text with the current date
        TextView todayDateTextView = findViewById(R.id.day);
        todayDateTextView.setText(todayDate);

        // Get the TextView for "LOG OUT"
        TextView logoutTextView = findViewById(R.id.logout);

        // Call the method to underline the TextView
        underlineText(logoutTextView);

        // Set OnClickListener for "LOG OUT"
        logoutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Handle logout action here (sign out from Firebase)
                FirebaseAuth.getInstance().signOut();

                // Navigate back to MainActivity or perform any other action
                Intent intent = new Intent(MainActivity4.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to MainActivity with the back button
            }
        });

        // Get the ImageView for Profile
        ImageView profile = findViewById(R.id.imageView);

        // Set OnClickListener for "Profile"
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to Profile
                Intent intent = new Intent(MainActivity4.this, MainActivity5.class);
                startActivity(intent);
                finish(); // Finish the current activity to prevent going back to MainActivity with the back button
            }
        });

        // Set the user's first name to the TextView
        setUserName(currentUser.getUid(), currentUser.getPhotoUrl());
    }

    // Method to set the user's first name to the TextView
    private void setUserName(String userId, Uri userPhotoUri) {
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Registered User").child(userId);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Assuming names are stored in the database by these path
                    String firstName = dataSnapshot.child("Firstname").getValue(String.class);
                    String lastName = dataSnapshot.child("Lastname").getValue(String.class);
                    String birthday = dataSnapshot.child("Birthday").getValue(String.class);
                    String schoolname = dataSnapshot.child("School").getValue(String.class);
                    String yearlevel = dataSnapshot.child("YearLevel").getValue(String.class);
                    String profileImageUriString = dataSnapshot.child("Profile Photo URL").getValue(String.class);

                    // Set  to the  TextView
                    TextView userTextView = findViewById(R.id.User);
                    userTextView.setText(firstName);
                    TextView userNameTextView = findViewById(R.id.Username);
                    userNameTextView.setText(firstName + " " + lastName);
                    TextView birthdayTextView = findViewById(R.id.Userbday);
                    birthdayTextView.setText(birthday);
                    TextView schoolnameTextView = findViewById(R.id.schoolname);
                    schoolnameTextView.setText(schoolname);
                    TextView yearlevelTextView = findViewById(R.id.yearlvl);
                    yearlevelTextView.setText(yearlevel);

                    // Get the ImageView dimensions in pixels
                    int targetWidth = (int) getResources().getDimensionPixelSize(R.dimen.imageView2_width);
                    int targetHeight = (int) getResources().getDimensionPixelSize(R.dimen.imageView2_height);

                    // Load the user's profile image into imageView2 using Glide
                    ImageView imageView2 = findViewById(R.id.imageView2);
                    if (userPhotoUri != null) {
                        // Use Glide to load the image into an ImageView
                        Glide.with(MainActivity4.this)
                                .load(userPhotoUri)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)  // Adjust caching strategy
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        // Convert the Drawable to a Bitmap
                                        Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
                                        Canvas canvas = new Canvas(bitmap);
                                        resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                                        resource.draw(canvas);

                                        // Resize the Bitmap to match imageView2 size
                                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                                        // Set the resized Bitmap to the ImageView
                                        imageView2.setImageBitmap(resizedBitmap);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                        // Handle case where the image load is cleared
                                    }
                                });
                    } else if (profileImageUriString != null) {
                        // Load the profile image using the URL if the Uri is null
                        Uri profileImageUri = Uri.parse(profileImageUriString);
                        Glide.with(MainActivity4.this)
                                .load(profileImageUri)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)  // Adjust caching strategy
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        // Convert the Drawable to a Bitmap
                                        Bitmap bitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
                                        Canvas canvas = new Canvas(bitmap);
                                        resource.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                                        resource.draw(canvas);

                                        // Resize the Bitmap to match imageView2 size
                                        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, targetWidth, targetHeight, true);

                                        // Set the resized Bitmap to the ImageView
                                        imageView2.setImageBitmap(resizedBitmap);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                        // Handle case where the image load is cleared
                                    }
                                });
                    } else {
                        // Handle the case where both Uri and URL are null (no image selected)
                        imageView2.setImageResource(R.drawable.default_profile_image); // Set a default image
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
            }
        });
    }

    // Method to underline Logout TextView
    private void underlineText(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }
}
