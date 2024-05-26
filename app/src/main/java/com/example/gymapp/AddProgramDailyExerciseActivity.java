package com.example.gymapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class AddProgramDailyExerciseActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private Spinner spPrograms;
    private Button btnAddProgram;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_program_daily_exercise);

        db = new DatabaseHelper(this);
        spPrograms = findViewById(R.id.spPrograms);
        btnAddProgram = findViewById(R.id.btnAddProgram);

        selectedDate = getIntent().getStringExtra("selectedDate");

        List<String> programs = db.getAllPrograms();
        ArrayAdapter<String> programAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, programs);
        programAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPrograms.setAdapter(programAdapter);

        btnAddProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProgramExercises();
            }
        });
    }

    private void addProgramExercises() {
        String selectedProgram = (String) spPrograms.getSelectedItem();
        List<Exercise> exercises = db.getExercisesByProgramTitle(selectedProgram);

        for (Exercise exercise : exercises) {
            db.addDailyExercise(selectedDate, exercise.getId(), 0, 0, 0);  // Default sets, reps, and weight can be updated later
        }

        Toast.makeText(this, "Program exercises added", Toast.LENGTH_SHORT).show();
        finish();
    }
}
