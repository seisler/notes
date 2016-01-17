package com.schenzle.notes;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.sql.SQLException;

public class MainActivity extends AppCompatActivity
{
    FloatingActionButton fab;
    ListView list;
    Toolbar toolbar;
    DBHandler dbHandler;
    Toast toastSqlFeedback, toastDeleteAllFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DBHandler(this);

        loadViewList();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newNote();
            }
        });
    }

    /**
     * Loads de ListView with all the notes from db
     */
    private void loadViewList()
    {
        openDb();
        Cursor allNotes = dbHandler.getAllNotes();

        String[] columns = new String[]{
                DBHandler.KEY_TITLE,
                DBHandler.KEY_NOTE
        };

        int[] to = new int[]{
                R.id.titleLine,
                R.id.noteLine
        };

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.note_adapter,
                allNotes,
                columns,
                to,
                0
        );

        list = (ListView) findViewById(R.id.list);
        list.setAdapter(cursorAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), ViewNoteActivity.class);
                i.putExtra("id", id);

                Log.d("cheater", parent.getAdapter().getView(position, view, parent).toString());
                startActivity(i);
            }
        });

        dbHandler.close();
    }

    /**
     * Open DB
     */
    private void openDb()
    {
        try {
            dbHandler.open();
        } catch (SQLException ex) {
            toastSqlFeedback = Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT);
            toastSqlFeedback.show();
        }
    }

    /**
     * Initialize new note activity
     */
    private void newNote()
    {
        Intent intent = new Intent(this, NewNoteActivity.class);
        startActivity(intent);
    }

    /**
     * Delete all notes
     */
    private void deleteAllNotes()
    {
        openDb();

        int message;
        if(dbHandler.deleteAllNOtes()) {
            message = R.string.all_notes_deleted;
        } else {
            message = R.string.no_notes_to_delete;
        }

        dbHandler.close();

        toastDeleteAllFeedBack = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG);
        toastDeleteAllFeedBack.show();
        loadViewList();
    }

    @Override
    public void onResume()
    {
        super.onResume();

        loadViewList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deleteAllNotes) {
            deleteAllNotes();
        }

        return super.onOptionsItemSelected(item);
    }
}
