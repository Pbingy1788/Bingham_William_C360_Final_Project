
package com.cs360.williambingham.bingham_william_c360_final_project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ParkDBHandler extends SQLiteOpenHelper {

    // database name and version
    private static final int DB_VER = 1;

    // Update the parksDB# to the next iteration when updating database
    private static final String DB_NAME = "parksDB51.db";

    //table
    public static final String TABLE_PARKS = "parks";

    // columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_FEATURE = "feature";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_RATE = "rate";

    // constructor
    public ParkDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DB_NAME, factory, DB_VER);
    }

    // This method creates the Parks table when the DB is initialized
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PARK_TABLE = "CREATE TABLE " +
                TABLE_PARKS + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT," +
                COLUMN_FEATURE + " TEXT," +
                COLUMN_CITY + " TEXT," +
                COLUMN_RATE + " INTEGER " + ")";
        db.execSQL(CREATE_PARK_TABLE);

        addPark(db, new Parks(10, "Glacier Park", "Wilderness", "Flathead"));
        addPark(db, new Parks(5, "Gyoen Park", "Inner City", "Tokyo"));
        addPark(db, new Parks(8, "Meiji Park", "Architecture", "Tokyo"));
        addPark(db, new Parks(6, "Olympic Park", "Rain Forest", "Washington"));
        addPark(db, new Parks(3, "Redwood Park", "Hiking", "Crescent"));
        addPark(db, new Parks(7, "Shiba Park", "Sight See", "Tokyo"));
        addPark(db, new Parks(9, "Sumida Park", "Nature", "Tokyo"));
        addPark(db, new Parks(2, "Yellowstone Park", "Old Faithful", "Wyoming"));
        addPark(db, new Parks(1, "Yosemite Park", "Explore", "California"));
        addPark(db, new Parks(1, "Yoyogi Park", "Relax", "Tokyo"));
    }

    // This method closes an open DB if a new one is created.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PARKS);
        onCreate(db);
    }

    // This method is used to add a park record to the database
    public void addPark(SQLiteDatabase db, Parks parks)
    {
        String parkName = parks.getName();
        String query = "SELECT * FROM " +
                TABLE_PARKS + " WHERE " + COLUMN_NAME +
                " = \"" + parkName + "\"";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String tempParkName = "";

        // Checks temporary Park name in column 1
        try
        {
            tempParkName = cursor.getString(1);
        }

        // catch exceptions
        catch (Exception e)
        {
        }

        // Add park condition statement
        if (tempParkName != null && !tempParkName.equals(""))
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, parks.getName());
            values.put(COLUMN_FEATURE, parks.getFeature());
            values.put(COLUMN_CITY, parks.getCity());
            values.put(COLUMN_RATE, parks.getRate());

            deletePark(tempParkName);

            //Inserts new park into database
            db.insert(TABLE_PARKS, null, values);
        }

        else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, parks.getName());
            values.put(COLUMN_FEATURE, parks.getFeature());
            values.put(COLUMN_CITY, parks.getCity());
            values.put(COLUMN_RATE, parks.getRate());

            db.insert(TABLE_PARKS, null, values);
        }
    }

    // Add Park to database logic
    public void addPark(Parks parks)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String parkName = parks.getName();
        String query = "SELECT * FROM " +
                TABLE_PARKS + " WHERE " + COLUMN_NAME +
                " = \"" + parkName + "\"";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        String tempParkName = "";

        try
        {
            tempParkName = cursor.getString(1);
        }
        catch (Exception e)
        {

        }

        // Add park logic added to database
        if (tempParkName != null && !tempParkName.equals(""))
        {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, parks.getName());
            values.put(COLUMN_FEATURE, parks.getFeature());
            values.put(COLUMN_CITY, parks.getCity());
            values.put(COLUMN_RATE, parks.getRate());

            deletePark(tempParkName);

            //Insert new data into database
            db.insert(TABLE_PARKS, null, values);
        }

        else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME, parks.getName());
            values.put(COLUMN_FEATURE, parks.getFeature());
            values.put(COLUMN_CITY, parks.getCity());
            values.put(COLUMN_RATE, parks.getRate());

            db.insert(TABLE_PARKS, null, values);
        }

        db.close();
    }

    // implements the search/find functionality
    public Parks searchPark(String parkName, String parkFeature, String parkCity)
    {
        String query = "";
        if (!parkName.equals("") && (parkFeature.equals("") || parkFeature == null && (parkCity.equals("") || parkCity == null)))
        {
            query = "SELECT * FROM " +
                    TABLE_PARKS + " WHERE " + COLUMN_NAME +
                    " = \"" + parkName + "\"";
        }

        else if((parkName.equals("") || parkName == null) && !parkFeature.equals("") && (parkCity.equals("") || parkCity == null))
        {
            query = "SELECT * FROM " +
                    TABLE_PARKS + " WHERE " + COLUMN_FEATURE +
                    " = \"" + parkFeature + "\"";
        }

        else if((parkName.equals("") || parkName == null) && (parkFeature.equals("") || parkFeature == null) && !parkCity.equals(""))
        {
            query = "SELECT * FROM " +
                    TABLE_PARKS + " WHERE " + COLUMN_CITY +
                    " = \"" + parkCity + "\"";
        }

        else {
            query = "SELECT * FROM " +
                    TABLE_PARKS + " WHERE " + COLUMN_NAME +
                    " = \"" + parkName + "\"";
        }


        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        Parks parks = new Parks();

        if (cursor.moveToFirst())
        {
            parks.setID(Integer.parseInt(cursor.getString(0)));
            parks.setName(cursor.getString(1));
            parks.setFeature(cursor.getString(2));
            parks.setCity(cursor.getString(3));
            parks.setRate(Integer.parseInt(cursor.getString(4)));
            cursor.close();

        } else {
            parks = null;
        }

        // close database and return
        db.close();
        return parks;
    }

    // Implements logic to update parks functionality
    public boolean updatePark (int id, int rate)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATE, rate);
        int value = db.update(TABLE_PARKS, values,"id=" + id, null);

        //close database and return
        db.close();
        return true;
    }

    // implements the delete park functionality
    public boolean deletePark (String parkName)
    {
        boolean result = false;
        String query = "SELECT * FROM " + TABLE_PARKS +
                " WHERE " + COLUMN_NAME + " = \"" + parkName + "\"";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Parks parks = new Parks();

        // Delete function logic
        if (cursor.moveToFirst())
        {
            parks.setID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_PARKS, COLUMN_ID + " = ?",
                    new String[] { String.valueOf(parks.getID())});
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}