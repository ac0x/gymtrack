package com.example.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ExerciseListActivity extends AppCompatActivity {

    private ListView listViewExercises;
    private DatabaseHelper db;
    private ArrayAdapter<Exercise> adapter;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_list);

        db = new DatabaseHelper(this);
        listViewExercises = findViewById(R.id.listViewExercises);

        category = getIntent().getStringExtra("category");
        loadExercises(category);
    }

    private void loadExercises(String category) {
        List<Exercise> exercises = db.getExercisesByCategory(category);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        listViewExercises.setAdapter(adapter);

        listViewExercises.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Exercise selectedExercise = exercises.get(position);
                Intent intent = new Intent(ExerciseListActivity.this, ExerciseProgressActivity.class);
                intent.putExtra("exerciseId", selectedExercise.getId());
                startActivity(intent);
            }
        });
    }
}
