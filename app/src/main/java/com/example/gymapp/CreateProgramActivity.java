package com.example.gymapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class CreateProgramActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ListView listViewPrograms;
    private Button btnAddNewProgram;
    private ProgramAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        db = new DatabaseHelper(this);
        listViewPrograms = findViewById(R.id.listViewPrograms);
        btnAddNewProgram = findViewById(R.id.btnAddNewProgram);

        loadPrograms();

        btnAddNewProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProgramActivity.this, NewProgramActivity.class);
                startActivityForResult(intent, 1);
            }
        });
    }

    private void loadPrograms() {
        List<String> programs = db.getAllPrograms();
        adapter = new ProgramAdapter(this, programs);
        listViewPrograms.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            loadPrograms();
        }
    }

    public List<Exercise> getExercisesByProgramTitle(String programTitle) {
        return db.getExercisesByProgramTitle(programTitle);
    }
}

