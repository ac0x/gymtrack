package com.example.gymapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class AddDailyExerciseActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etSets, etReps, etWeight;
    private Spinner spExercises;
    private Button btnAddExercise;
    private RecyclerView recyclerViewDailyExercises;
    private List<DailyExercise> dailyExercises;
    private ArrayAdapter<Exercise> exerciseAdapter;
    private DailyExerciseAdapter dailyExerciseAdapter;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daily_exercise);

        db = new DatabaseHelper(this);
        etSets = findViewById(R.id.etSets);
        etReps = findViewById(R.id.etReps);
        etWeight = findViewById(R.id.etWeight);
        spExercises = findViewById(R.id.spExercises);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        recyclerViewDailyExercises = findViewById(R.id.recyclerViewDailyExercises);

        selectedDate = getIntent().getStringExtra("selectedDate");

        List<Exercise> exercises = db.getAllExercises();
        exerciseAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, exercises);
        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExercises.setAdapter(exerciseAdapter);

        dailyExercises = db.getDailyExercises(selectedDate);
        dailyExerciseAdapter = new DailyExerciseAdapter(this, dailyExercises);
        recyclerViewDailyExercises.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewDailyExercises.setAdapter(dailyExerciseAdapter);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDailyExercise();
            }
        });
    }

    private void addDailyExercise() {
        Exercise selectedExercise = (Exercise) spExercises.getSelectedItem();
        int sets = Integer.parseInt(etSets.getText().toString());
        int reps = Integer.parseInt(etReps.getText().toString());
        double weight = Double.parseDouble(etWeight.getText().toString());

        db.addDailyExercise(selectedDate, selectedExercise.getId(), sets, reps, weight);
        dailyExercises.add(new DailyExercise(0, selectedDate, selectedExercise.getId(), sets, reps, weight, db));
        dailyExerciseAdapter.notifyDataSetChanged();

        Toast.makeText(this, "Exercise added", Toast.LENGTH_SHORT).show();
    }
}
