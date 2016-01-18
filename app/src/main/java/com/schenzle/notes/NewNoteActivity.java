package com.schenzle.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;

public class NewNoteActivity extends AppCompatActivity
{
    EditText titleEditText, noteEditText;
    DBHandler dbHandler;
    Toolbar toolbar;
    Button saveBtn;
    Toast toastSqlFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveBtn = (Button) findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveNote();
                finish();
            }
        });
    }

    /**
     * Save a note
     */
    private void saveNote()
    {
        dbHandler = new DBHandler(this);
        openDb();
        createNote();
        closeDb();
    }

    /**
     * Create new note
     */
    private void createNote()
    {
        titleEditText = (EditText) findViewById(R.id.editTitle);
        noteEditText = (EditText) findViewById(R.id.editNote);

        dbHandler.createNote(
                titleEditText.getText().toString(),
                noteEditText.getText().toString()
        );
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
     * Close DB
     */
    private void closeDb()
    {
        dbHandler.close();
    }
}
