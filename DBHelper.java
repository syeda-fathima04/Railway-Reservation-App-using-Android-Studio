package com.example.railwaymad;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
public class DBHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "RailwayDB";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_TRAINS = "trains";
    private static final String TABLE_STATIONS = "stations";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create trains table
        db.execSQL("CREATE TABLE " + TABLE_TRAINS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, train_name TEXT, train_number TEXT)");

        // Create stations table
        db.execSQL("CREATE TABLE " + TABLE_STATIONS + " (id INTEGER PRIMARY KEY AUTOINCREMENT, station_name TEXT)");

        // Insert sample data into trains
        db.execSQL("INSERT INTO " + TABLE_TRAINS + " (train_name, train_number) VALUES ('Rajdhani Express', '12345')");
        db.execSQL("INSERT INTO " + TABLE_TRAINS + " (train_name, train_number) VALUES ('Shatabdi Express', '54321')");
        db.execSQL("INSERT INTO " + TABLE_TRAINS + " (train_name, train_number) VALUES ('Duronto Express', '11223')");
        db.execSQL("INSERT INTO " + TABLE_TRAINS + " (train_name, train_number) VALUES ('Garib Rath', '33445')");

        // Insert sample data into stations
        db.execSQL("INSERT INTO " + TABLE_STATIONS + " (station_name) VALUES ('Bangalore')");
        db.execSQL("INSERT INTO " + TABLE_STATIONS + " (station_name) VALUES ('Delhi')");
        db.execSQL("INSERT INTO " + TABLE_STATIONS + " (station_name) VALUES ('Mumbai')");
        db.execSQL("INSERT INTO " + TABLE_STATIONS + " (station_name) VALUES ('Chennai')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STATIONS);
        onCreate(db);
    }

    // Fetch all trains
    public ArrayList<String> getAllTrains() {
        ArrayList<String> trainList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT train_name || ' (' || train_number || ')' AS train_info FROM " + TABLE_TRAINS, null);
        if (cursor.moveToFirst()) {
            do {
                trainList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return trainList;
    }

    // Fetch all stations
    public ArrayList<String> getAllStations() {
        ArrayList<String> stationList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT station_name FROM " + TABLE_STATIONS, null);
        if (cursor.moveToFirst()) {
            do {
                stationList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return stationList;
    }
}


