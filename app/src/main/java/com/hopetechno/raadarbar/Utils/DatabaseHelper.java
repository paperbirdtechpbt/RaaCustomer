package com.hopetechno.raadarbar.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.hopetechno.raadarbar.Modal.GooglePlaces;
import com.hopetechno.raadarbar.Modal.Note;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "raa.db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(Note.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Note.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertNote(String mainText,String description) {

        SQLiteDatabase db = this.getWritableDatabase();

        ////star
        long id=0;
        if(getNotesCount(mainText) == 0) {
            // get writable database as we want to write data
            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Note.COLUMN_MAIN_TEXT, mainText);
            values.put(Note.COLUMN_DESCRIPTION, description);

            Log.e("DatabaseHelper","Inser Data Call ");
            // insert row
             id = db.insert(Note.TABLE_NAME, null, values);

            // close db connection
            db.close();
        }
        // return newly inserted row id
        return id;
    }

    public Note getNote(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Note.TABLE_NAME,
                new String[]{Note.COLUMN_ID, Note.COLUMN_MAIN_TEXT, Note.COLUMN_DESCRIPTION},
                Note.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        Note note = new Note(
                cursor.getInt(cursor.getColumnIndex(Note.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_MAIN_TEXT)),
                cursor.getString(cursor.getColumnIndex(Note.COLUMN_DESCRIPTION)));

        cursor.close();

        return note;
    }

    public GooglePlaces getAllNotes(String name) {
        List<GooglePlaces.PredictionsBean> notes = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + Note.TABLE_NAME + " Where " +Note.COLUMN_MAIN_TEXT+" LIKE "+"'%"+name+"%'"+" ORDER BY "+ Note.COLUMN_MAIN_TEXT+" COLLATE LOCALIZED ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        GooglePlaces googlePlaces = new GooglePlaces();
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                GooglePlaces.PredictionsBean note =  new GooglePlaces.PredictionsBean();

                note.setDescription(cursor.getString(cursor.getColumnIndex(Note.COLUMN_DESCRIPTION)));
                note.setId(String.valueOf((cursor.getColumnIndex(Note.COLUMN_ID))));

                GooglePlaces.PredictionsBean.StructuredFormattingBean  structuredFormattingBean = new GooglePlaces.PredictionsBean.StructuredFormattingBean();
                structuredFormattingBean.setMain_text(cursor.getString(cursor.getColumnIndex(Note.COLUMN_MAIN_TEXT)));
                note.setStructured_formatting(structuredFormattingBean);

                notes.add(note);
            } while (cursor.moveToNext());

            googlePlaces.setPredictions(notes);
        }

        // close db connection
        db.close();
        // return notes list
        return googlePlaces;
    }

    String getSqlValue(String input){
        return input.replace("\'","\'\'");
    }

    public int getNotesCount(String text) {

        String countQuery = "SELECT  * FROM " + Note.TABLE_NAME+  " Where " +Note.COLUMN_MAIN_TEXT +"="+"'"+getSqlValue(text)+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }
}