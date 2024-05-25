package com.example.gymapp;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class AddExerciseActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private EditText etExerciseName;
    private Spinner spExerciseCategory;
    private Button btnAddExercise;
    private ListView listViewCategories;
    private CategoryAdapter adapter;
    private List<Exercise> exercises;
    private HashMap<String, List<Exercise>> exercisesByCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        db = new DatabaseHelper(this);
        etExerciseName = findViewById(R.id.etExerciseName);
        spExerciseCategory = findViewById(R.id.spExerciseCategory);
        btnAddExercise = findViewById(R.id.btnAddExercise);
        listViewCategories = findViewById(R.id.listViewCategories);

        // Popunjavanje spinnera kategorijama
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.exercise_categories, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExerciseCategory.setAdapter(adapterSpinner);

        loadExercisesByCategory();

        btnAddExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = etExerciseName.getText().toString();
                String category = spExerciseCategory.getSelectedItem().toString();
                if (!name.isEmpty()) {
                    if (db.exerciseExists(name, category)) {
                        Toast.makeText(AddExerciseActivity.this, "Exercise already exists in this category", Toast.LENGTH_SHORT).show();
                    } else {
                        db.addExercise(name, category);
                        loadExercisesByCategory();
                    }
                }
            }
        });
    }

    private void loadExercisesByCategory() {
        exercisesByCategory = new HashMap<>();
        List<String> categories = Arrays.asList(getResources().getStringArray(R.array.exercise_categories));
        for (String category : categories) {
            exercisesByCategory.put(category, db.getExercisesByCategory(category));
        }

        adapter = new CategoryAdapter(this, categories, exercisesByCategory);
        listViewCategories.setAdapter(adapter);
    }
}
