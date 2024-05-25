package com.example.gymapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;
import java.util.List;

public class ProgramAdapter extends BaseAdapter {

    private Context context;
    private List<String> programs;
    private HashMap<Integer, Boolean> expandedPositions;

    public ProgramAdapter(Context context, List<String> programs) {
        this.context = context;
        this.programs = programs;
        this.expandedPositions = new HashMap<>();
    }

    @Override
    public int getCount() {
        return programs.size();
    }

    @Override
    public Object getItem(int position) {
        return programs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_program, parent, false);
        }

        TextView tvProgramTitle = convertView.findViewById(R.id.tvProgramTitle);
        ImageView ivArrow = convertView.findViewById(R.id.ivArrow);
        LinearLayout llExercises = convertView.findViewById(R.id.llExercises);

        final String programTitle = programs.get(position);
        tvProgramTitle.setText(programTitle);

        boolean expanded = expandedPositions.get(position) != null && expandedPositions.get(position);
        llExercises.setVisibility(expanded ? View.VISIBLE : View.GONE);
        ivArrow.setImageResource(expanded ? android.R.drawable.arrow_up_float : android.R.drawable.arrow_down_float);

        if (expanded) {
            List<Exercise> exercises = ((CreateProgramActivity) context).getExercisesByProgramTitle(programTitle);
            llExercises.removeAllViews();
            for (Exercise exercise : exercises) {
                TextView tvExercise = new TextView(context);
                tvExercise.setText(exercise.getName());
                llExercises.addView(tvExercise);
            }
        }

        ivArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean expanded = expandedPositions.get(position) != null && expandedPositions.get(position);
                expandedPositions.put(position, !expanded);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
}


