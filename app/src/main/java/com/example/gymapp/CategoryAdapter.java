package com.example.gymapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<String> categories;
    private HashMap<Integer, Boolean> expandedPositions;
    private HashMap<String, List<Exercise>> exercisesByCategory;

    public CategoryAdapter(Context context, List<String> categories, HashMap<String, List<Exercise>> exercisesByCategory) {
        this.context = context;
        this.categories = categories;
        this.expandedPositions = new HashMap<>();
        this.exercisesByCategory = exercisesByCategory;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_category, parent, false);
        }

        TextView tvCategoryTitle = convertView.findViewById(R.id.tvCategoryTitle);
        LinearLayout llExercises = convertView.findViewById(R.id.llExercises);

        final String categoryTitle = categories.get(position);
        tvCategoryTitle.setText(categoryTitle);

        boolean expanded = expandedPositions.get(position) != null && expandedPositions.get(position);
        llExercises.setVisibility(expanded ? View.VISIBLE : View.GONE);

        if (expanded) {
            List<Exercise> exercises = exercisesByCategory.get(categoryTitle);
            llExercises.removeAllViews();
            for (Exercise exercise : exercises) {
                TextView tvExercise = new TextView(context);
                tvExercise.setText(exercise.getName());
                llExercises.addView(tvExercise);
            }
        }

        tvCategoryTitle.setOnClickListener(new View.OnClickListener() {
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
