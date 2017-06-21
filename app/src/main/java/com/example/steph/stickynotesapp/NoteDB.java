package com.example.steph.stickynotesapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Date;
import java.util.ArrayList;

/**
 * handles Database operations
 */
public class NoteDB {
    private Context context;
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private final static String TABLE_NAME = "note";
    private final static String DATABASE_NAME = "notes.db";

    private final static int DATABASE_VERSION = 1;

    private final static String COLUMN_ID = "_id";
    private final static String COLUMN_TITLE = "title";
    private final static String COLUMN_DESCRIPTION = "description";
    private final static String COLUMN_CONTENT = "content";
    private final static String COLUMN_BUILDDATE = "buildate";
    private final static String COLUMN_LASTMODIFIEDDATE = "lastmodified";
    private final static String COLUMN_LOCKED = "locked";

    /**
     * Instantiates a new Note db.
     *
     * @param context the context
     */
    public NoteDB(Context context) {
        this.context = context;
        openHelper = new MyOpenHelper(context);

    }


    private final static String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TITLE + " TEXT NOT NULL UNIQUE, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_CONTENT + " TEXT, " +
                    COLUMN_LASTMODIFIEDDATE + " DATE, " +
                    COLUMN_LOCKED + " INTEGER NOT NULL , " +
                    COLUMN_BUILDDATE + " DATE NOT NULL)";

    /**
     * deletes note from the database @param note the note
     *
     * @return the int
     */
    public int removeNotes(Note note) {
        String selection = COLUMN_TITLE + " = ? ";
        String[] selectionArgs = {note.getTitle()};
        database = openHelper.getWritableDatabase();
        int numberOfRows = database.delete(TABLE_NAME, selection, selectionArgs);
        database.close();
        return numberOfRows;
    }

    /**
     * inserts note in the database @param note the note
     *
     * @return the long
     */
    public long insertNote(Note note) {

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_DESCRIPTION, note.getDescription());

        cv.put(COLUMN_CONTENT, note.getContent());
        cv.put(COLUMN_BUILDDATE, note.getCreateDate().toString());
        cv.put(COLUMN_LASTMODIFIEDDATE, note.getLastModified().toString());
        cv.put(COLUMN_LOCKED, note.getLocked());


        database = openHelper.getWritableDatabase();

        long id = database.insert(TABLE_NAME, null, cv);

        database.close();
        return id;

    }

    /**
     * edits existent note  @param note the note
     *
     * @param oldtilte the oldtilte
     * @return the long
     */
    public long editNote(Note note, String oldtilte) {
        String selection = COLUMN_TITLE + " = ?";
        String[] selectionArgs = {oldtilte};

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TITLE, note.getTitle());
        cv.put(COLUMN_DESCRIPTION, note.getDescription());
        cv.put(COLUMN_CONTENT, note.getContent());
        cv.put(COLUMN_BUILDDATE, note.getCreateDate().toString());
        cv.put(COLUMN_LASTMODIFIEDDATE, note.getLastModified().toString());
        cv.put(COLUMN_LOCKED, note.getLocked());

        database = openHelper.getWritableDatabase();

        long id = database.update(TABLE_NAME, cv, selection, selectionArgs);

        database.close();
        return id;

    }

    /**
     * returns the array of all the notes that the user is currently looking for @param title the title
     *
     * @return the note
     */
    public ArrayList<Note> getNote(String title) {
        Cursor cursor = null;
        if (title != null) {
            String selection = COLUMN_TITLE + " = ?";
            String[] selectionArgs = {title};

            database = openHelper.getReadableDatabase();

            cursor = database.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);
        } else {

            database = openHelper.getReadableDatabase();

            cursor = database.query(TABLE_NAME,null, null, null, null, null, null);


        }
        ArrayList<Note> noteList = new ArrayList<Note>();
        Note note = null;
if (cursor!=null){
        while (cursor.moveToNext()) {
            String noteTitle = cursor.getString(cursor.getColumnIndex(COLUMN_TITLE));
            String noteDescription = cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION));
            String noteContent = cursor.getString(cursor.getColumnIndex(COLUMN_CONTENT));
            Date noteBuildDate = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_BUILDDATE)) * 1000);
            Date noteModifiedDate = new Date(cursor.getLong(cursor.getColumnIndex(COLUMN_LASTMODIFIEDDATE)) * 1000);
           int locked=cursor.getInt(cursor.getColumnIndex(COLUMN_LOCKED));

            note = new Note(noteTitle, noteDescription, noteContent, noteBuildDate, noteModifiedDate,locked);
            noteList.add(note);
        }}
        database.close();

        return noteList;
    }


    private class MyOpenHelper extends SQLiteOpenHelper {
        /**
         * Instantiates a new My open helper.
         *
         * @param context the context
         */
        public MyOpenHelper(Context context) {

            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXIST " + TABLE_NAME);
            onCreate(db);
        }
    }

}
