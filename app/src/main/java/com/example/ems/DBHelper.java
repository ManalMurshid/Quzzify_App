package com.example.ems;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "QuizzifyDB";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USER_TYPE = "user_type";
    public static final String COLUMN_BATCH = "batch";
    public static final String COLUMN_DEGREE = "degree";

    public static final String USER_TYPE_CANDIDATE = "Candidate";
    public static final String USER_TYPE_EXAMINER = "Examiner";

    public static final String TABLE_PAPERS = "papers";
    public static final String COLUMN_PAPER_ID = "_id";
    public static final String COLUMN_SUBJECT = "subject";
    public static final String COLUMN_YEAR = "year";
    public static final String COLUMN_TIME_DURATION = "time_duration";
    public static final String COLUMN_FORM_LINK = "form_link";
    public static final String COLUMN_DEGREE_PAPER = "degree";
    public static final String COLUMN_USER_NAME = "username"; // Foreign key from users table

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_USERNAME + " TEXT, " +
            COLUMN_PASSWORD + " TEXT, " +
            COLUMN_USER_TYPE + " TEXT, " +
            COLUMN_BATCH + " TEXT, " +
            COLUMN_DEGREE + " TEXT" +
            ");";

    private static final String CREATE_PAPERS_TABLE = "CREATE TABLE " + TABLE_PAPERS + " (" +
            COLUMN_PAPER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUBJECT + " TEXT, " +
            COLUMN_YEAR + " TEXT, " +
            COLUMN_TIME_DURATION + " TEXT, " +
            COLUMN_FORM_LINK + " TEXT, " +
            COLUMN_DEGREE_PAPER + " TEXT, " +
            COLUMN_USER_NAME + " TEXT, " +
            "FOREIGN KEY (" + COLUMN_USER_NAME + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USERNAME + ")" +
            ");";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_PAPERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PAPERS);
        onCreate(db);
    }

    // Method to add a new user to the database
    public long addUser(String username, String password, String userType, String batch, String degree) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_USER_TYPE, userType);
        values.put(COLUMN_BATCH, batch);
        values.put(COLUMN_DEGREE, degree);

        long result= db.insert(TABLE_USERS, null, values);
        db.close();
        return result;
    }



    // Method to authenticate user
//    public boolean authenticateUser(String username, String password) {
//        SQLiteDatabase db = this.getReadableDatabase();
//        String selection = COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?";
//        String[] selectionArgs = {username, password};
//        Cursor cursor = db.query(TABLE_USERS, null, selection, selectionArgs, null, null, null);
//        boolean isValid = cursor.getCount() > 0;
//        cursor.close();
//        return isValid;
//    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        Boolean flag = cursor.getCount() > 0;
        cursor.close();
        return flag;
    }

    public String checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String userType = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));
            if (userType.equals(USER_TYPE_CANDIDATE)) {
                return USER_TYPE_CANDIDATE; // Candidate
            } else if (userType.equals(USER_TYPE_EXAMINER)) {
                return USER_TYPE_EXAMINER; // Examiner
            }
        }
        cursor.close();
        return null; // Default to false if user not found or type not recognized
    }

    public String[] getUserBatchDegree(String username){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String userBatch = cursor.getString(cursor.getColumnIndex(COLUMN_BATCH));
            @SuppressLint("Range") String userDegree = cursor.getString(cursor.getColumnIndex(COLUMN_DEGREE));
            cursor.close();
            return new String[]{userBatch, userDegree};
        }
        cursor.close();
        return null;
    }

    // Method to get user type
    @SuppressLint("Range")
    public String getUserType(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_USER_TYPE};
        String selection = COLUMN_USERNAME + " = ?";
        String[] selectionArgs = {username};
        Cursor cursor = db.query(TABLE_USERS, columns, selection, selectionArgs, null, null, null);
        String userType = "";
        if (cursor.moveToFirst()) {
            userType = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));
        }
        cursor.close();
        return userType;
    }

    // Method to add a new paper to the database
    // Method to add a new paper to the database with the username of the logged-in user
    public long addPaper(String subject, String year, String timeDuration, String formLink, Context context, String degree, String loggedInUserName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_TIME_DURATION, timeDuration);
        values.put(COLUMN_FORM_LINK, formLink);
        // Retrieve the username of the logged-in user from SharedPreferences or any other mechanism
//        String loggedInUsername = getLoggedInUsername(context);
        values.put(COLUMN_USER_NAME, loggedInUserName);
        values.put(COLUMN_DEGREE_PAPER, degree);

        long result= db.insert(TABLE_PAPERS, null, values);
        db.close();
        return result;
    }

    // Method to delete a paper by its ID
    public void deletePaper(int paperId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PAPERS, COLUMN_PAPER_ID + " = ?", new String[]{String.valueOf(paperId)});
        db.close();
    }

    // Method to update a paper
    public long updatePaper(int paperId, String subject, String year, String timeDuration, String formLink, String degree, String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SUBJECT, subject);
        values.put(COLUMN_YEAR, year);
        values.put(COLUMN_TIME_DURATION, timeDuration);
        values.put(COLUMN_FORM_LINK, formLink);
        values.put(COLUMN_DEGREE_PAPER, degree);
        values.put(COLUMN_USER_NAME, username);

        long result = db.update(TABLE_PAPERS, values, COLUMN_PAPER_ID + " = ?", new String[]{String.valueOf(paperId)});
        db.close();
        return result;
    }




    // Method to get the username of the logged-in user
    private String getLoggedInUsername(Context context) {
        // Implement the logic to retrieve the username of the logged-in user
        // For example, you can use SharedPreferences to store the username when the user logs in
        // Here, I'm assuming you have stored the username in SharedPreferences with key "username"
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("username", ""); // "" is the default value if username is not found
    }

    public List<Paper> getPapersForUser(String userName) {
        List<Paper> papers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_PAPER_ID, COLUMN_SUBJECT, COLUMN_YEAR, COLUMN_TIME_DURATION, COLUMN_FORM_LINK, COLUMN_DEGREE_PAPER};
        String selection = COLUMN_USER_NAME + " = ?";
        String[] selectionArgs = {String.valueOf(userName)};
        Cursor cursor = db.query(TABLE_PAPERS, columns, selection, selectionArgs, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int paperId = cursor.getInt(cursor.getColumnIndex(COLUMN_PAPER_ID));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT));
            @SuppressLint("Range") String year = cursor.getString(cursor.getColumnIndex(COLUMN_YEAR));
            @SuppressLint("Range") String timeDuration = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_DURATION));
            @SuppressLint("Range") String formLink = cursor.getString(cursor.getColumnIndex(COLUMN_FORM_LINK));
            @SuppressLint("Range") String degree = cursor.getString(cursor.getColumnIndex(COLUMN_DEGREE_PAPER));
            // Assuming Paper class constructor takes these parameters
            Paper paper = new Paper(paperId, subject, year, timeDuration, formLink, degree);
            papers.add(paper);
        }
        cursor.close();
        return papers;
    }

    public List<Paper> getPapersForCandidate(String batch, String degree) {
        List<Paper> papers = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_PAPERS + " WHERE " + COLUMN_YEAR + " = ? AND " + COLUMN_DEGREE_PAPER + " = ?", new String[]{batch, degree});
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int paperId = cursor.getInt(cursor.getColumnIndex(COLUMN_PAPER_ID));
            @SuppressLint("Range") String subject = cursor.getString(cursor.getColumnIndex(COLUMN_SUBJECT));
            @SuppressLint("Range") String timeDuration = cursor.getString(cursor.getColumnIndex(COLUMN_TIME_DURATION));
            @SuppressLint("Range") String formLink = cursor.getString(cursor.getColumnIndex(COLUMN_FORM_LINK));
            // Assuming Paper class constructor takes these parameters
            Paper paper = new Paper(paperId, subject, batch, timeDuration, formLink, degree);
            papers.add(paper);
        }
        cursor.close();
        return papers;
    }

}

//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//import androidx.annotation.Nullable;
//
//public class DBHelper extends SQLiteOpenHelper {
//
//    public static final String DATABASE_NAME = "Ems.db"; // Replace with your desired database name
//
//    private static final String TABLE_USERS = "users";
//    private static final String TABLE_EXAMS = "exams";
//
//    //USER TABLE
//    private static final String COLUMN_ID_USER = "_id";
//    private static final String COLUMN_USERNAME = "username";
//    private static final String COLUMN_PASSWORD = "password";
//    private static final String COLUMN_BATCH = "batch";
//    private static final String COLUMN_DEGREE = "degree";
//    private static final String COLUMN_USER_TYPE = "user_type";
//
//    // Constants for user types
//    public static final String USER_TYPE_CANDIDATE = "candidate";
//    public static final String USER_TYPE_EXAMINER = "examiner";
//
//
//    //EXAM TABLE
//    private static final String COLUMN_ID_EXAM = "_idExam";
//    private static final String COLUMN_USERNAME_DUPLICATE = "username";
//    private static final String COLUMN_EXAM_BATCH = "batchExam";
//    private static final String COLUMN_EXAM_DEGREE = "degree_exam";
//    private static final String COLUMN_EXAM_TIME = "exam_time";
//
//    public DBHelper(Context context) {
//        super(context, DATABASE_NAME, null, 1);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String createTableuser = "CREATE TABLE " + TABLE_USERS + " (" +
//                COLUMN_ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_USERNAME + " TEXT NOT NULL UNIQUE, " + // Unique username constraint
//                COLUMN_PASSWORD + " TEXT NOT NULL, " +
//                COLUMN_USER_TYPE + " TEXT NOT NULL, " +
//                COLUMN_BATCH + " TEXT, " +
//                COLUMN_DEGREE + " TEXT)";
//        db.execSQL(createTableuser);
//
//        String createTableexam = "CREATE TABLE " + TABLE_USERS + " (" +
//                COLUMN_ID_EXAM + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
//                COLUMN_USERNAME_DUPLICATE + " TEXT NOT NULL UNIQUE, " + // Unique username constraint
//                COLUMN_EXAM_BATCH + " TEXT, " +
//                COLUMN_EXAM_TIME + " TEXT, " +
//                COLUMN_EXAM_DEGREE + " TEXT)";
//        db.execSQL(createTableexam);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
//        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);
//        onCreate(db);
//    }
//
//    public long insertUser(String username, String password, String userType, String batch, String degree) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COLUMN_USERNAME, username);
//        contentValues.put(COLUMN_PASSWORD, password);
//        contentValues.put(COLUMN_USER_TYPE, userType);
//        contentValues.put(COLUMN_BATCH, batch); // Add batch and degree columns
//        contentValues.put(COLUMN_DEGREE, degree);
//
//        long result = db.insert(TABLE_USERS, null, contentValues);
//        db.close();
//        return result;
//    }
//
//    public long insertExam(String username, String password, String userType, String batch, String degree) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COLUMN_USERNAME, username);
//        contentValues.put(COLUMN_PASSWORD, password);
//        contentValues.put(COLUMN_USER_TYPE, userType);
//        contentValues.put(COLUMN_BATCH, batch); // Add batch and degree columns
//        contentValues.put(COLUMN_DEGREE, degree);
//
//        long result = db.insert(TABLE_USERS, null, contentValues);
//        db.close();
//        return result;
//    }
//
//    public Boolean checkusername(String username) {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
//        if (cursor.getCount() > 0)
//            return true;
//        else
//            return false;
//    }
//
//
//
//    public Boolean checkusernamepassword(String username, String password) {
//        SQLiteDatabase MyDB = this.getWritableDatabase();
//        Cursor cursor = MyDB.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, password});
//        if (cursor.moveToFirst()) {
//            String userType = cursor.getString(cursor.getColumnIndex(COLUMN_USER_TYPE));
//            if (userType.equals(USER_TYPE_CANDIDATE)) {
//                return true; // Candidate
//            } else if (userType.equals(USER_TYPE_EXAMINER)) {
//                return false; // Examiner
//            }
//        }
//        return false; // Default to false if user not found or type not recognized
//    }
//
//
//}
