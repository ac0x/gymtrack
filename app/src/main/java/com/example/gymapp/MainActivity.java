package com.example.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private RecyclerView recyclerViewDailyExercises;
    private Button btnAddDailyExercise, btnAddProgramExercise, btnAddExercise, btnCreateProgram, btnViewProgress;
    private DailyExerciseAdapter dailyExerciseAdapter;
    private DatabaseHelper db;
    private TextView tvNoExercises;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        recyclerViewDailyExercises = findViewById(R.id.recyclerViewDailyExercises);
        btnAddDailyExercise = findViewById(R.id.btnAddDailyExercise);
        btnAddProgramExercise = findViewById(R.id.btnAddProgramExercise);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnCreateProgram = findViewById(R.id.btnCreateProgram);
        btnViewProgress = findViewById(R.id.btnViewProgress);
        tvNoExercises = findViewById(R.id.tvNoExercises);

        db = new DatabaseHelper(this);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                loadDailyExercises(selectedDate);
            }
        });

        btnAddDailyExercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddDailyExerciseActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivity(intent);
        });

        btnAddProgramExercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddProgramDailyExerciseActivity.class);
            intent.putExtra("selectedDate", selectedDate);
            startActivity(intent);
        });

        btnAddExercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExerciseActivity.class);
            startActivity(intent);
        });

        btnCreateProgram.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NewProgramActivity.class);
            startActivity(intent);
        });

        btnViewProgress.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ProgressActivity.class);
            startActivity(intent);
        });

        // Initialize RecyclerView
        recyclerViewDailyExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDailyExercises.setHasFixedSize(true);

        // Load exercises for today's date on startup
        long today = calendarView.getDate();
        selectedDate = getDateFromCalendarView(today);
        loadDailyExercises(selectedDate);
    }

    private void loadDailyExercises(String date) {
        List<DailyExercise> dailyExercises = db.getDailyExercises(date);
        dailyExerciseAdapter = new DailyExerciseAdapter(this, dailyExercises);
        recyclerViewDailyExercises.setAdapter(dailyExerciseAdapter);

        if (dailyExercises.isEmpty()) {
            tvNoExercises.setVisibility(View.VISIBLE);
            recyclerViewDailyExercises.setVisibility(View.GONE);
        } else {
            tvNoExercises.setVisibility(View.GONE);
            recyclerViewDailyExercises.setVisibility(View.VISIBLE);
        }
    }

    private String getDateFromCalendarView(long milliseconds) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-M-d");
        java.util.Date resultDate = new java.util.Date(milliseconds);
        return sdf.format(resultDate);
    }
}
