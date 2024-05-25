package com.example.gymapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "gymtrack.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";

    private static final String TABLE_PROGRAMS = "programs";
    private static final String COLUMN_PROGRAM_ID = "id";
    private static final String COLUMN_PROGRAM_TITLE = "title";

    private static final String TABLE_PROGRAM_EXERCISES = "program_exercises";
    private static final String COLUMN_PROGRAM_EXERCISE_PROGRAM_ID = "program_id";
    private static final String COLUMN_PROGRAM_EXERCISE_EXERCISE_ID = "exercise_id";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EXERCISE_TABLE = "CREATE TABLE " + TABLE_EXERCISES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_CATEGORY + " TEXT" + ")";
        db.execSQL(CREATE_EXERCISE_TABLE);

        String CREATE_PROGRAM_TABLE = "CREATE TABLE " + TABLE_PROGRAMS + "("
                + COLUMN_PROGRAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_PROGRAM_TITLE + " TEXT" + ")";
        db.execSQL(CREATE_PROGRAM_TABLE);

        String CREATE_PROGRAM_EXERCISES_TABLE = "CREATE TABLE " + TABLE_PROGRAM_EXERCISES + "("
                + COLUMN_PROGRAM_EXERCISE_PROGRAM_ID + " INTEGER,"
                + COLUMN_PROGRAM_EXERCISE_EXERCISE_ID + " INTEGER,"
                + "FOREIGN KEY(" + COLUMN_PROGRAM_EXERCISE_PROGRAM_ID + ") REFERENCES " + TABLE_PROGRAMS + "(" + COLUMN_PROGRAM_ID + "),"
                + "FOREIGN KEY(" + COLUMN_PROGRAM_EXERCISE_EXERCISE_ID + ") REFERENCES " + TABLE_EXERCISES + "(" + COLUMN_ID + ")" + ")";
        db.execSQL(CREATE_PROGRAM_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAM_EXERCISES);
        onCreate(db);
    }

    // Provera metode da postoji samo jedna
    public void addExercise(String name, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CATEGORY, category);
        db.insert(TABLE_EXERCISES, null, values);
        db.close();
    }

    // Ostale metode za rad sa bazom
    public List<Exercise> getAllExercises() {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES, null);
        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                );
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exercises;
    }

    public List<Exercise> getExercisesByCategory(String category) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_CATEGORY + "=?", new String[]{category});
        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                );
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exercises;
    }

    public void addProgram(String title, List<Exercise> exercises) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        long programId = db.insert("programs", null, values);

        for (Exercise exercise : exercises) {
            ContentValues exerciseValues = new ContentValues();
            exerciseValues.put("program_id", programId);
            exerciseValues.put("exercise_id", exercise.getId());
            db.insert("program_exercises", null, exerciseValues);
        }
        db.close();
    }

    public List<String> getAllPrograms() {
        List<String> programs = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM programs", null);
        if (cursor.moveToFirst()) {
            do {
                programs.add(cursor.getString(cursor.getColumnIndexOrThrow("title")));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return programs;
    }

    public List<Exercise> getExercisesByProgramTitle(String title) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT e." + COLUMN_ID + ", e." + COLUMN_NAME + ", e." + COLUMN_CATEGORY + " FROM " + TABLE_EXERCISES + " e " +
                "JOIN " + TABLE_PROGRAM_EXERCISES + " pe ON e." + COLUMN_ID + " = pe." + COLUMN_PROGRAM_EXERCISE_EXERCISE_ID + " " +
                "JOIN " + TABLE_PROGRAMS + " p ON pe." + COLUMN_PROGRAM_EXERCISE_PROGRAM_ID + " = p." + COLUMN_PROGRAM_ID + " " +
                "WHERE p." + COLUMN_PROGRAM_TITLE + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{title});
        if (cursor.moveToFirst()) {
            do {
                Exercise exercise = new Exercise(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY))
                );
                exercises.add(exercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return exercises;
    }
}
