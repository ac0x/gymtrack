package com.example.gymapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DailyExerciseAdapter extends RecyclerView.Adapter<DailyExerciseAdapter.DailyExerciseViewHolder> {

    private Context context;
    private List<DailyExercise> dailyExercises;

    public DailyExerciseAdapter(Context context, List<DailyExercise> dailyExercises) {
        this.context = context;
        this.dailyExercises = dailyExercises;
    }

    @NonNull
    @Override
    public DailyExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_daily_exercise, parent, false);
        return new DailyExerciseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyExerciseViewHolder holder, int position) {
        DailyExercise dailyExercise = dailyExercises.get(position);
        holder.tvExerciseName.setText(dailyExercise.getExerciseName());
        holder.tvSets.setText("Sets: " + dailyExercise.getSets());
        holder.tvReps.setText("Reps: " + dailyExercise.getReps());
        holder.tvWeight.setText("Weight: " + dailyExercise.getWeight());
    }

    @Override
    public int getItemCount() {
        return dailyExercises.size();
    }

    public class DailyExerciseViewHolder extends RecyclerView.ViewHolder {
        TextView tvExerciseName, tvSets, tvReps, tvWeight;

        public DailyExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvExerciseName = itemView.findViewById(R.id.tvExerciseName);
            tvSets = itemView.findViewById(R.id.tvSets);
            tvReps = itemView.findViewById(R.id.tvReps);
            tvWeight = itemView.findViewById(R.id.tvWeight);
        }
    }
}
