package com.example.gymapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class NewProgramActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etProgramTitle;
    private Spinner spCategory;
    private ListView listViewExercises;
    private Button btnAddExercise;
    private Button btnCreateProgram;
    private List<Exercise> selectedExercises;
    private ExerciseAdapter exercisesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_program);

        db = new DatabaseHelper(this);
        etProgramTitle = findViewById(R.id.etProgramTitle);
        spCategory = findViewById(R.id.spCategory);
        listViewExercises = findViewById(R.id.listViewSelectedExercises);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        btnCreateProgram = findViewById(R.id.btnCreateProgram);

        selectedExercises = new ArrayList<>();
        exercisesAdapter = new ExerciseAdapter(this, selectedExercises);
        listViewExercises.setAdapter(exercisesAdapter);

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.exercise_categories, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapterSpinner);

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExercise();
            }
        });

        btnCreateProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createProgram();
            }
        });
    }

    private void addExercise() {
        String category = spCategory.getSelectedItem().toString();
        List<Exercise> exercises = db.getExercisesByCategory(category);

        if (exercises.isEmpty()) {
            Toast.makeText(this, "No exercises found in this category", Toast.LENGTH_SHORT).show();
            return;
        }

        final ArrayAdapter<Exercise> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);

        new AlertDialog.Builder(this)
                .setTitle("Select Exercise")
                .setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Exercise selectedExercise = arrayAdapter.getItem(which);
                        if (selectedExercise != null) {
                            selectedExercises.add(selectedExercise);
                            exercisesAdapter.notifyDataSetChanged();
                        }
                    }
                })
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void createProgram() {
        String title = etProgramTitle.getText().toString();

        if (title.isEmpty() || selectedExercises.isEmpty()) {
            Toast.makeText(this, "Please fill out all fields and add at least one exercise", Toast.LENGTH_SHORT).show();
            return;
        }

        db.addProgram(title, selectedExercises);
        Toast.makeText(this, "Program created", Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }
}

