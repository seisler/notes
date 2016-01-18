package com.schenzle.notes;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;


public class DBHandler
{

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_NOTE = "note";

    public static final String TAG = "DBHandler";

    public static final String DB_NAME = "BDNotes";
    public static final String DB_TABLE = "notes";

    public static final int VERSION = 1;

    public static final String DB_CREATE =
            "create table " + DB_TABLE + "( " +
                    KEY_ID + " integer primary key " + "autoincrement," +
                    KEY_TITLE + " text not null, " +
                    KEY_NOTE + " text not null);";

    private final Context context;
    private NotesOpenHelper DBHelper;
    private SQLiteDatabase bd;

    /**
     * Constructor
     *
     * @param con Context
     */
    public DBHandler(Context con)
    {
        this.context = con;
        DBHelper = new NotesOpenHelper(context);
    }

    /**
     * Open DB
     *
     * @return DBhandler
     * @throws SQLException
     */
    public DBHandler open() throws SQLException
    {
        bd = DBHelper.getWritableDatabase();

        return this;
    }

    /**
     * Close DB
     */
    public void close()
    {
        DBHelper.close();
    }

    /**
     * Create a new value
     *
     * @param title String
     * @param note  String
     * @return long
     */
    public long createNote(String title, String note)
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_NOTE, note);

        return bd.insert(DB_TABLE, null, initialValues);
    }

    /**
     * Delete a note
     *
     * @param noteId long
     * @return boolean
     */
    public boolean deleteNoteById(long noteId)
    {
        return bd.delete(DB_TABLE, KEY_ID + " = " + noteId, null) > 0;
    }

    /**
     * Delete all notes
     *
     * @return boolean
     */
    public boolean deleteAllNOtes() {
        return bd.delete(DB_TABLE, null, null) > 0;
    }

    /**
     * Get a note by Id
     *
     * @param noteId long
     * @return Cursor
     * @throws SQLException
     */
    public Cursor getNoteById(long noteId) throws SQLException
    {
        Cursor mCursor = bd.query(
                true,
                DB_TABLE,
                new String[]{KEY_ID, KEY_TITLE, KEY_NOTE},
                KEY_ID + " = " + noteId,
                null,
                null,
                null,
                null,
                null
        );

        if (mCursor != null) mCursor.moveToFirst();

        return mCursor;
    }

    /**
     * Get all notes
     *
     * @return Cursor
     */
    public Cursor getAllNotes()
    {
        return bd.query(
                DB_TABLE,
                new String[]{KEY_ID, KEY_TITLE, KEY_NOTE},
                null,
                null,
                null,
                null,
                null
        );
    }

    /**
     * Update a note
     *
     * @param noteId long
     * @param title  String
     * @param note   String
     * @return boolean
     */
    public boolean updateNote(long noteId, String title, String note)
    {
        ContentValues args = new ContentValues();
        args.put(KEY_TITLE, title);
        args.put(KEY_NOTE, note);

        return bd.update(DB_TABLE, args, KEY_ID + " = " + noteId, null) > 0;
    }


    /**
     * Notes DB Helper
     */
    private static class NotesOpenHelper extends SQLiteOpenHelper
    {
        /**
         * Constructor
         *
         * @param con Context
         */
        public NotesOpenHelper(Context con)
        {
            super(con, DB_NAME, null, VERSION);
        }


        @Override
        public void onCreate(SQLiteDatabase db)
        {
            try {
                db.execSQL(DB_CREATE);
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int VersioAntiga, int VersioNova)
        {
            Log.w(TAG, "Upgrading DB from version " + VersioAntiga + " to " + VersioNova + ". This will destroy all data");
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE);
            onCreate(db);
        }
    }
}


