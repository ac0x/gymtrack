package com.example.gymapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ExerciseProgressActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_progress);

        lineChart = findViewById(R.id.lineChart);

        DatabaseHelper db = new DatabaseHelper(this);
        List<DailyExercise> exercises = db.getAllDailyExercises();

        List<Entry> entries = new ArrayList<>();
        for (DailyExercise exercise : exercises) {
            int dayOfYear = DateUtil.getDayOfYear(exercise.getDate());
            if (dayOfYear != -1) {
                entries.add(new Entry(dayOfYear, (float) exercise.getWeight()));
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Exercise Progress");
        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh
    }
}
