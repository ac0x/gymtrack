package com.example.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CalendarView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private Button btnAddExercise;
    private Button btnCreateProgram;
    private Button btnViewProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnCreateProgram = findViewById(R.id.btnCreateProgram);
        btnViewProgress = findViewById(R.id.btnViewProgress);

        btnAddExercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
            startActivity(intent);
        });

        btnCreateProgram.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CreateProgramActivity.class);
            startActivity(intent);
        });

        btnViewProgress.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
            startActivity(intent);
        });
    }
}
