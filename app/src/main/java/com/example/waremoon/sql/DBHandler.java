package com.example.waremoon.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "Waremoon";

    // below int is our database version
    private static final int DB_VERSION = 2;

    // below variable is for our table name.
    private static final String TABLE_USER_DATA = "userData";
    private static final String TABLE_USER_IMAGES = "userImages";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our email column.
    private static final String EMAIL_COL = "email";

    // below variable is for our username column.
    private static final String USERNAME_COL = "username";

    // below variable is for our password column.
    private static final String PASSWORD_COL = "password";

    private static final String IMAGE_ID_COL = "imageId";
    private static final String USER_ID_COL = "userId";
    private static final String IMAGE_DATA_COL = "imageData";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_USER_DATA + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + EMAIL_COL + " TEXT,"
                + USERNAME_COL + " TEXT,"
                + PASSWORD_COL + " TEXT)";

        db.execSQL(query);

        String userImagesQuery = "CREATE TABLE " + TABLE_USER_IMAGES + " ("
                + IMAGE_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + USER_ID_COL + " INTEGER, "
                + IMAGE_DATA_COL + " BLOB, "
                + "FOREIGN KEY(" + USER_ID_COL + ") REFERENCES " + TABLE_USER_DATA + "(" + ID_COL + "))";
        db.execSQL(userImagesQuery);

        Log.d("DBHandler", "Tables created.");
    }

    public void registerUser(String email, String userName, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EMAIL_COL, email);
        values.put(USERNAME_COL, userName);
        values.put(PASSWORD_COL, password);
        db.insert(TABLE_USER_DATA, null, values);
        db.close();
    }

    public boolean checkUserCredentials(String userName, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {ID_COL};
        String selection = USERNAME_COL + "=? AND " + PASSWORD_COL + "=?";
        String[] selectionArgs = {userName, password};
        Cursor cursor = db.query(TABLE_USER_DATA, columns, selection, selectionArgs, null, null, null);

        int count = cursor.getCount();
        cursor.close();
        return count > 0;
    }

    public int getUserId(String username) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT id FROM userData WHERE username=?", new String[]{username});
        int id = -1;
        if (cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return id;
    }

    public String getUserName(int userId) {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT username FROM userData WHERE id=?", new String[]{String.valueOf(userId)});
        String userName = null;
        if (cursor.moveToFirst()) {
            userName = cursor.getString(0);
        }
        cursor.close();
        sqLiteDatabase.close();
        return userName;
    }

    public void insertUserImage(int userId, byte[] imageData) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID_COL, userId);
        values.put(IMAGE_DATA_COL, imageData);

        long result = db.insert(TABLE_USER_IMAGES, null, values);
        Log.d("DBHandler", "insertUserImage result: " + result);

        db.close();
    }

    public List<byte[]> getUserImages(int userId) {
        List<byte[]> userImages = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {IMAGE_DATA_COL};
        String selection = USER_ID_COL + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USER_IMAGES, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int imageDataColumnIndex = cursor.getColumnIndex(IMAGE_DATA_COL);
                if (imageDataColumnIndex != -1) {
                    byte[] imageData = cursor.getBlob(imageDataColumnIndex);
                    userImages.add(imageData);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return userImages;
    }

    public List<Integer> getImageIds(int userId) {
        List<Integer> imageIds = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {IMAGE_ID_COL};
        String selection = USER_ID_COL + "=?";
        String[] selectionArgs = {String.valueOf(userId)};
        Cursor cursor = db.query(TABLE_USER_IMAGES, columns, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int imageIdColumnIndex = cursor.getColumnIndex(IMAGE_ID_COL);
                if (imageIdColumnIndex != -1) {
                    int imageId = cursor.getInt(imageIdColumnIndex);
                    imageIds.add(imageId);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return imageIds;
    }

    public void deleteUserImage(int userId, int imageId) {
        SQLiteDatabase db = this.getWritableDatabase();
        String whereClause = USER_ID_COL + "=? AND " + IMAGE_ID_COL + "=?";
        String[] whereArgs = {String.valueOf(userId), String.valueOf(imageId)};
        db.delete(TABLE_USER_IMAGES, whereClause, whereArgs);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // this method is called to check if the table exists already.
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DATA);
        onCreate(db);
    }
}