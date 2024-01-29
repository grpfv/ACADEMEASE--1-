package com.example.project_acadeamease1;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Arrays;
import java.util.List;

public class Schedule extends AppCompatActivity {

    private Button sunButton,monButton, tueButton, wedButton, thuButton, friButton, satButton;
    ScheduleAdapter scheduleAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        ImageButton launchScheduleButton = findViewById(R.id.launchScheduleButton);
        recyclerView = findViewById(R.id.sched_recycler_view);

        launchScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showScheduleDialog();
            }
        });

        // Initialize day buttons
        sunButton = findViewById(R.id.sun_btn);
        monButton = findViewById(R.id.mon_btn);
        tueButton = findViewById(R.id.tue_btn);
        wedButton = findViewById(R.id.wed_btn);
        thuButton = findViewById(R.id.thu_btn);
        friButton = findViewById(R.id.fri_btn);
        satButton = findViewById(R.id.sat_btn);


        // Set click listeners for each day button
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(sunButton);

            }
        });
        monButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(monButton);
            }
        });

        tueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(tueButton);
            }
        });
        wedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(wedButton);
            }
        });
        thuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(thuButton);
            }
        });
        friButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(friButton);
            }
        });
        satButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDayButtonClick(satButton);
            }
        });

        onDayButtonClick(sunButton);
    }

    private void setupRecyclerView(String selectedDay) {
        Query query = Utility.getCollectionReferenceForSched().whereEqualTo("day", selectedDay);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        FirestoreRecyclerOptions<SchedModel> options = new FirestoreRecyclerOptions.Builder<SchedModel>()
                .setQuery(query, SchedModel.class).build();

        recyclerView.setLayoutManager(layoutManager);

        scheduleAdapter = new ScheduleAdapter(options, this);
        recyclerView.setAdapter(scheduleAdapter);
        scheduleAdapter.startListening();
    }


    private void onDayButtonClick(Button selectedButton) {
        // Reset background color for all day buttons
        resetButtonColors();

        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.clicked_text_color));
        // Set background color for the selected day button
        selectedButton.setBackgroundColor(ContextCompat.getColor(this, R.color.selected_button_color));

        setupRecyclerView(selectedButton.getText().toString());
    }


    @Override
    protected void onStart() {
        super.onStart();
        scheduleAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        scheduleAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        scheduleAdapter.notifyDataSetChanged();
    }

    private void showScheduleDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        ScheduleAdd scheduleDialog = new ScheduleAdd();
        scheduleDialog.show(fragmentManager, "ScheduleAdd");
    }

    private void resetButtonColors() {
        // List of day buttons
        List<Button> dayButtons = Arrays.asList(sunButton, monButton, tueButton, wedButton, thuButton, friButton, satButton);

        for (Button button : dayButtons) {
            // Reset background color for all day buttons
            button.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

            // Reset text color for all day buttons
            button.setTextColor(ContextCompat.getColor(this, android.R.color.black));
        }
    }

}