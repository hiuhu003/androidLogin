package com.example.kcau;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginData extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "login.db";
    private static final String TABLE_NAME = "users";
    private static final String COL1 = "name";

    private static final String COL2 = "username";

    private static final String COL3 = "password";
    private static final int DATABASE_VERSION = 1;

    public LoginData(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, username TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor validate(String name, String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE name=? AND username = ? AND password = ?",
                new String[] {name, username, password });
    }
}
