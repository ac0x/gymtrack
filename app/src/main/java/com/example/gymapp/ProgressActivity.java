package com.example.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;

public class ProgressActivity extends AppCompatActivity {

    private ListView listViewProgressCategories;
    private DatabaseHelper db;
    private HashMap<String, List<Exercise>> exercisesByCategory;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        db = new DatabaseHelper(this);
        listViewProgressCategories = findViewById(R.id.listViewProgressCategories);

        loadCategories();
    }

    private void loadCategories() {
        List<String> categories = db.getAllCategories();
        exercisesByCategory = new HashMap<>();

        for (String category : categories) {
            List<Exercise> exercises = db.getExercisesByCategory(category);
            exercisesByCategory.put(category, exercises);
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listViewProgressCategories.setAdapter(adapter);

        listViewProgressCategories.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = categories.get(position);
                Intent intent = new Intent(ProgressActivity.this, ExerciseListActivity.class);
                intent.putExtra("category", selectedCategory);
                startActivity(intent);
            }
        });
    }
}
