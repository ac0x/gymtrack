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

    // Tabela za vežbe
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_CATEGORY = "category";

    // Tabela za dnevne vežbe
    private static final String TABLE_DAILY_EXERCISES = "daily_exercises";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_EXERCISE_ID = "exercise_id";
    private static final String COLUMN_SETS = "sets";
    private static final String COLUMN_REPS = "reps";
    private static final String COLUMN_WEIGHT = "weight";

    // Tabele za programe
    private static final String TABLE_PROGRAMS = "programs";
    private static final String COLUMN_PROGRAM_ID = "program_id";
    private static final String COLUMN_TITLE = "title";

    private static final String TABLE_PROGRAM_EXERCISES = "program_exercises";

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

        String CREATE_DAILY_EXERCISES_TABLE = "CREATE TABLE " + TABLE_DAILY_EXERCISES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_EXERCISE_ID + " INTEGER,"
                + COLUMN_SETS + " INTEGER,"
                + COLUMN_REPS + " INTEGER,"
                + COLUMN_WEIGHT + " REAL" + ")";
        db.execSQL(CREATE_DAILY_EXERCISES_TABLE);

        String CREATE_PROGRAMS_TABLE = "CREATE TABLE " + TABLE_PROGRAMS + "("
                + COLUMN_PROGRAM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT" + ")";
        db.execSQL(CREATE_PROGRAMS_TABLE);

        String CREATE_PROGRAM_EXERCISES_TABLE = "CREATE TABLE " + TABLE_PROGRAM_EXERCISES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "program_id INTEGER,"
                + "exercise_id INTEGER,"
                + "FOREIGN KEY(program_id) REFERENCES programs(id),"
                + "FOREIGN KEY(exercise_id) REFERENCES exercises(id)" + ")";
        db.execSQL(CREATE_PROGRAM_EXERCISES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DAILY_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAMS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PROGRAM_EXERCISES);
        onCreate(db);
    }

    public void addExercise(String name, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_CATEGORY, category);
        db.insert(TABLE_EXERCISES, null, values);
        db.close();
    }

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

    public void addDailyExercise(String date, int exerciseId, int sets, int reps, double weight) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_EXERCISE_ID, exerciseId);
        values.put(COLUMN_SETS, sets);
        values.put(COLUMN_REPS, reps);
        values.put(COLUMN_WEIGHT, weight);
        db.insert(TABLE_DAILY_EXERCISES, null, values);
        db.close();
    }

    public List<DailyExercise> getDailyExercises(String date) {
        List<DailyExercise> dailyExercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_DAILY_EXERCISES + " WHERE " + COLUMN_DATE + "=?", new String[]{date});
        if (cursor.moveToFirst()) {
            do {
                DailyExercise dailyExercise = new DailyExercise(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXERCISE_ID)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_SETS)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_REPS)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT)),
                        this // Pass DatabaseHelper instance
                );
                dailyExercises.add(dailyExercise);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return dailyExercises;
    }

    public boolean exerciseExists(String name, String category) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_NAME + "=? AND " + COLUMN_CATEGORY + "=?", new String[]{name, category});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
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

    public List<Exercise> getExercisesByProgramTitle(String programTitle) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT exercises.id, exercises.name, exercises.category FROM exercises " +
                "INNER JOIN program_exercises ON exercises.id = program_exercises.exercise_id " +
                "INNER JOIN programs ON programs.id = program_exercises.program_id " +
                "WHERE programs.title = ?";
        Cursor cursor = db.rawQuery(query, new String[]{programTitle});
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
        values.put(COLUMN_TITLE, title);
        long programId = db.insert(TABLE_PROGRAMS, null, values);

        for (Exercise exercise : exercises) {
            ContentValues exerciseValues = new ContentValues();
            exerciseValues.put("program_id", programId);
            exerciseValues.put("exercise_id", exercise.getId());
            db.insert("program_exercises", null, exerciseValues);
        }
        db.close();
    }

    public String getExerciseNameById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_NAME + " FROM " + TABLE_EXERCISES + " WHERE " + COLUMN_ID + "=?", new String[]{String.valueOf(id)});
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
            cursor.close();
            db.close();
            return name;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

}
