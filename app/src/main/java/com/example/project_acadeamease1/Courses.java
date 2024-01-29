package com.example.project_acadeamease1;



import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

public class Courses extends AppCompatActivity{

    RecyclerView recyclerView;
    CourseAdapter courseAdapter;
    ImageButton launchScheduleButton, backbutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courses);

        launchScheduleButton = findViewById(R.id.launchCoursesButton);
        backbutton = findViewById(R.id.backbutton);
        recyclerView = findViewById(R.id.recycler_view);

        backbutton.setOnClickListener(v-> {
            finish();
        });

        launchScheduleButton.setOnClickListener(v -> showCourseDialog());
        setupRecyclerView();


    }

    void showCourseDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddCourses scheduleDialog = new AddCourses();
        scheduleDialog.show(fragmentManager, "AddCourses");
    }

    void setupRecyclerView(){
        Query query = Utility.getCollectionReferenceForCourses();
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        FirestoreRecyclerOptions<CourseModel> options = new FirestoreRecyclerOptions.Builder<CourseModel>().setQuery(query, CourseModel.class).build();
        recyclerView.setLayoutManager(layoutManager);

        courseAdapter = new CourseAdapter(options,this);
        recyclerView.setAdapter(courseAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        courseAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        courseAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        courseAdapter.notifyDataSetChanged();
    }
}
