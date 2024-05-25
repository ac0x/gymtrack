package com.example.gymapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddExerciseActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etExerciseName;
    private Spinner spExerciseCategory;
    private Button btnAddExercise;
    private ListView listViewExercises;
    private ExerciseAdapter adapter;
    private List<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        db = new DatabaseHelper(this);
        etExerciseName = findViewById(R.id.etExerciseName);
        spExerciseCategory = findViewById(R.id.spExerciseCategory);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        listViewExercises = findViewById(R.id.listViewExercises);

        exercises = db.getAllExercises();
        adapter = new ExerciseAdapter(this, exercises);
        listViewExercises.setAdapter(adapter);

        // Popunjavanje spinnera kategorijama
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.exercise_categories, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExerciseCategory.setAdapter(adapterSpinner);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etExerciseName.getText().toString();
                String category = spExerciseCategory.getSelectedItem().toString();
                if (!name.isEmpty()) {
                    db.addExercise(name, category);
                    exercises.clear();
                    exercises.addAll(db.getAllExercises());
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }
}
