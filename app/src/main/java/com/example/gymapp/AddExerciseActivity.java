package com.example.gymapp;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

public class AddExerciseActivity extends AppCompatActivity {

    private EditText etExerciseName;
    private Spinner spExerciseCategory;
    private Button btnAddExercise;
    private ListView listViewCategories;
    private DatabaseHelper db;
    private CategoryAdapter adapter;
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

        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(this,
                R.array.exercise_categories, android.R.layout.simple_spinner_item);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spExerciseCategory.setAdapter(adapterSpinner);

        btnAddExercise.setOnClickListener(v -> addExercise());

        loadCategories();
    }

    private void addExercise() {
        String name = etExerciseName.getText().toString().trim();
        String category = spExerciseCategory.getSelectedItem().toString();

        if (name.isEmpty()) {
            Toast.makeText(this, "Please enter exercise name", Toast.LENGTH_SHORT).show();
            return;
        }

        if (db.exerciseExists(name, category)) {
            Toast.makeText(this, "Exercise already exists", Toast.LENGTH_SHORT).show();
        } else {
            db.addExercise(name, category);
            Toast.makeText(this, "Exercise added", Toast.LENGTH_SHORT).show();
            loadCategories();
        }
    }

    private void loadCategories() {
        List<String> categories = db.getAllCategories();
        exercisesByCategory = new HashMap<>();

        for (String category : categories) {
            List<Exercise> exercises = db.getExercisesByCategory(category);
            exercisesByCategory.put(category, exercises);
        }

        adapter = new CategoryAdapter(this, categories, exercisesByCategory);
        listViewCategories.setAdapter(adapter);
    }
}
