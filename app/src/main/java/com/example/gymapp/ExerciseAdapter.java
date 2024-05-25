package com.example.gymapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exercises;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
        this.exercises = exercises;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Exercise exercise = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_exercise_with_delete, parent, false);
        }

        TextView tvExerciseName = convertView.findViewById(R.id.tvExerciseName);
        Button btnDeleteExercise = convertView.findViewById(R.id.btnDeleteExercise);

        tvExerciseName.setText(exercise.getName());

        btnDeleteExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exercises.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}
