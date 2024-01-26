package com.example.project_acadeamease1;
import androidx.annotation.NonNull;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity5 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main5);

        // Set OnClickListener for "Edit" text under "YEAR LEVEL"
        TextView yearLevelEdit = findViewById(R.id.yrlvledit);
        underlineText(yearLevelEdit);

        // Set OnClickListener for "Edit" text under "SCHOOL NAME"
        TextView schoolNameEdit = findViewById(R.id.schoolnameedit);
        underlineText(schoolNameEdit);

        // Set OnClickListener for "CHANGE PROFILE" text
        TextView changePhotoTextView = findViewById(R.id.change_photo);
        underlineText(changePhotoTextView);

        // Fetch user data and set EditText values
        fetchAndSetUserData();

        // Set user profile image
        setProfileImage();
    }

    // Method to underline a TextView
    private void underlineText(TextView textView) {
        SpannableString content = new SpannableString(textView.getText());
        content.setSpan(new UnderlineSpan(), 0, content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(content);
    }

    // Fetch user data and set EditText values
    private void fetchAndSetUserData() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Registered User")
                    .child(currentUser.getUid());

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String firstname = dataSnapshot.child("Firstname").getValue(String.class);
                        String lastname = dataSnapshot.child("Lastname").getValue(String.class);
                        String userBday = dataSnapshot.child("Birthday").getValue(String.class);
                        String userAge = dataSnapshot.child("Age").getValue(String.class);
                        String userYearLevel = dataSnapshot.child("YearLevel").getValue(String.class);
                        String userSchoolName = dataSnapshot.child("School").getValue(String.class);

                        // Set values to respective EditText fields
                        EditText boxName = findViewById(R.id.boxname);
                        EditText boxBday = findViewById(R.id.boxbday);
                        EditText boxAge = findViewById(R.id.boxage);
                        EditText boxYearLevel = findViewById(R.id.yearlvl);
                        EditText boxSchoolName = findViewById(R.id.boxschoolname);

                        boxName.setText(firstname + " " + lastname);
                        boxBday.setText(userBday);
                        boxAge.setText(userAge);
                        boxYearLevel.setText(userYearLevel);
                        boxSchoolName.setText(userSchoolName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors if needed
                }
            });
        }
    }

    // Set user profile image
    private void setProfileImage() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null) {
            DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("Registered User")
                    .child(currentUser.getUid());

            userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String profileImageUriString = dataSnapshot.child("Profile Photo URL").getValue(String.class);

                        // Set profile image using Glide or other libraries
                        // For simplicity, using a separate method to load image from URL
                        loadImageFromUrl(profileImageUriString);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle errors if needed
                }
            });
        }
    }

    // Load image from URL and set it to the profile ImageView
    private void loadImageFromUrl(String imageUrl) {
        ImageView profileImageView = findViewById(R.id.profile);

        new Thread(() -> {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(input);

                // Resize the bitmap to a circular shape
                Bitmap circularBitmap = getRoundedBitmap(bitmap);

                runOnUiThread(() -> {
                    // Set the circular image to the ImageView
                    profileImageView.setImageBitmap(circularBitmap);
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    // Method to set the image in circular shape
    private void setCircularImage(Uri imageUri) {
        if (imageUri != null) {
            try {
                // Convert the image URI to a Bitmap
                InputStream inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap originalBitmap = BitmapFactory.decodeStream(inputStream);

                // Create a circular bitmap using the original bitmap
                Bitmap circularBitmap = getRoundedBitmap(originalBitmap);

                // Set the circular bitmap to the ImageView
                ImageView profileImageView = null;
                profileImageView.setImageBitmap(circularBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error handling image", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Selected image URI is null", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);

        // Create a shader to paint with a circular pattern
        Shader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint.setShader(shader);

        // Draw a circle on the canvas with the bitmap shader
        canvas.drawCircle(width / 2f, height / 2f, Math.min(width, height) / 2f, paint);

        return output;
    }

    public void buttonCLick(View view) {
        Intent intent = new Intent(this, MainActivity4.class);
        startActivity(intent);
    }
}



