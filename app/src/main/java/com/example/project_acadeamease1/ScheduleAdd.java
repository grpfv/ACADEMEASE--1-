package com.example.project_acadeamease1;

//// Schedule Activity
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import android.view.ViewGroup;

import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;

public class ScheduleAdd extends DialogFragment {

    private EditText editSubject, editStartTime, editEndTime;
    private Spinner spinnerDay;
    private TimePicker timePicker;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_schedule_add, container, false);

        editSubject = view.findViewById(R.id.editSubject);
        spinnerDay = view.findViewById(R.id.spinnerDay);
        editStartTime = view.findViewById(R.id.editStartTime);
        editEndTime = view.findViewById(R.id.editEndTime);
        timePicker = view.findViewById(R.id.timePicker);

        // Set onClickListener for start time EditText
        editStartTime.setOnClickListener(view1 -> showTimePicker(editStartTime));

        // Set onClickListener for end time EditText
        editEndTime.setOnClickListener(view12 -> showTimePicker(editEndTime));

        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v-> saveSchedule());

        return view;
    }


    void saveSchedule(){
        String schedSubject = editSubject.getText().toString();
        String schedDay = spinnerDay.getSelectedItem().toString();
        String schedStartTime = editStartTime.getText().toString();
        String schedEndTime = editEndTime.getText().toString();

        if (schedSubject.isEmpty() || schedSubject == null){
            Toast.makeText(requireContext(), "Please enter Subject", Toast.LENGTH_SHORT).show();
            editSubject.setError("Subject Name is required");
            return;
        }
        if (schedStartTime.isEmpty() || schedStartTime == null){
            Toast.makeText(requireContext(), "Please enter Start Time", Toast.LENGTH_SHORT).show();
            editStartTime.setError("Start Time is required");
            return;
        }
        if (schedEndTime.isEmpty() || schedEndTime == null){
            Toast.makeText(requireContext(), "Please enter End Time", Toast.LENGTH_SHORT).show();
            editEndTime.setError("End Time is required");
            return;
        }

        SchedModel schedule = new SchedModel();
        schedule.setSchedSubject(schedSubject);
        schedule.setDay(schedDay);
        schedule.setSubjectTime(schedStartTime + " - "  +schedEndTime);


        saveScheduletoFirebase(schedule);
    }

    void saveScheduletoFirebase(SchedModel schedModel){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForSched().document();

        documentReference.set(schedModel).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(requireContext(),"Schedule Added",Toast.LENGTH_SHORT).show();
                    dismiss();
                }else{
                    Toast.makeText(requireContext(),"Failed Adding Schedule",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    private void showTimePicker(final EditText editText) {
        // Inflate the custom layout for the time picker dialog
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_time_picker, null);

        // Create a dialog and set its content view
        final Dialog dialog = new Dialog(requireContext());
        dialog.setContentView(dialogView);

        // Get references to TimePicker and Button in the dialog
        final TimePicker dialogTimePicker = dialogView.findViewById(R.id.dialogTimePicker);
        Button btnSetTime = dialogView.findViewById(R.id.btnSetTime);

        // Set up the TimePicker
        dialogTimePicker.setIs24HourView(false);

        // Set a listener to handle the "Set Time" button click
        btnSetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = dialogTimePicker.getHour();
                int minute = dialogTimePicker.getMinute();

                // Format the selected time in 12-hour format with AM/PM
                String selectedTime = String.format(Locale.getDefault(), "%02d:%02d %s",
                        (hour == 0 || hour == 12) ? 12 : hour % 12, minute, (hour < 12) ? "AM" : "PM");

                // Set the selected time to the EditText
                editText.setText(selectedTime);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });

        // Show the dialog
        dialog.show();
    }
}
