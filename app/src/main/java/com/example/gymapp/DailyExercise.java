package com.example.gymapp;

public class DailyExercise {
    private int id;
    private String date;
    private int exerciseId;
    private int sets;
    private int reps;
    private double weight;
    private DatabaseHelper dbHelper;

    public DailyExercise(int id, String date, int exerciseId, int sets, int reps, double weight, DatabaseHelper dbHelper) {
        this.id = id;
        this.date = date;
        this.exerciseId = exerciseId;
        this.sets = sets;
        this.reps = reps;
        this.weight = weight;
        this.dbHelper = dbHelper;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public int getExerciseId() {
        return exerciseId;
    }

    public int getSets() {
        return sets;
    }

    public int getReps() {
        return reps;
    }

    public double getWeight() {
        return weight;
    }

    public String getExerciseName() {
        return dbHelper.getExerciseNameById(exerciseId);
    }
}
