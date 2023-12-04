package com.example.waremoon;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Waremoon";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "userData";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our email column.
    private static final String EMAIL_COL = "email";

    // below variable is for our username column.
    private static final String USERNAME_COL = "username";

    // below variable is for our password column.
    private static final String PASSWORD_COL = "password";

    // modify the constructor to include email, username, and password columns
    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // modify the onCreate method to include email, username, and password columns
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL_COL + " TEXT,"
                + USERNAME_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        db.execSQL(query);
    }

    // modify the addNewCourse method to add registration details (email, username, password)
    public void registerUser(String email, String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL_COL, email);
        values.put(USERNAME_COL, userName);
        values.put(PASSWORD_COL, password);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Add this method to check user credentials
    public boolean checkUserCredentials(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID_COL};
        String selection = USERNAME_COL + "=? AND " + PASSWORD_COL + "=?";
        String[] selectionArgs = {userName, password};
        Cursor cursor = db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
