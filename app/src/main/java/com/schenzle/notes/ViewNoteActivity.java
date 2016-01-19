package com.schenzle.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.SQLException;


public class ViewNoteActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher
{
    Toolbar toolbar;
    DBHandler dbHandler;
    Intent intent;
    EditText titleEditText, noteEditText;
    Button deleteBtn, updateBtn;
    long noteId;
    String title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startValidation();

        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        updateBtn = (Button) findViewById(R.id.updateBtn);
        dbHandler = new DBHandler(this);

        deleteBtn.setOnClickListener(this);
        updateBtn.setOnClickListener(this);

        getIntentData();
        setDataIntoView();
    }

    /**
     * Get the intent data
     */
    private void getIntentData()
    {
        intent = getIntent();
        noteId = intent.getLongExtra("id", 0);
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
    }

    /**
     * Fill view with data
     */
    private void setDataIntoView()
    {
        titleEditText = (EditText) findViewById(R.id.editTitle);
        noteEditText = (EditText) findViewById(R.id.editNote);
        titleEditText.setText(title);
        noteEditText.setText(note);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.deleteBtn:
                deleteNote();
                break;
            case R.id.updateBtn:
                updateNote();
        }
    }

    /**
     * Delete the note
     */
    private void deleteNote()
    {
        openDb();

        if(dbHandler.deleteNoteById(noteId)) {
            Toast.makeText(getApplicationContext(), R.string.delete_note, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), R.string.delete_note_fail, Toast.LENGTH_SHORT).show();
        }

        dbHandler.close();
    }

    /**
     * Updates a note
     */
    private void updateNote()
    {
        openDb();

        int message;

        title = titleEditText.getText().toString();
        note = noteEditText.getText().toString();

        if(dbHandler.updateNote(noteId, title, note)){
            message = R.string.update_note;
        } else {
            message = R.string.update_failed;
        }

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

        dbHandler.close();

        finish();
    }

    /**
     * Start the form validation
     */
    private void startValidation()
    {
        titleEditText = (EditText) findViewById(R.id.editTitle);
        noteEditText = (EditText) findViewById(R.id.editNote);

        titleEditText.addTextChangedListener(this);
        noteEditText.addTextChangedListener(this);
    }

    /**
     * Open the data base connection
     */
    private void openDb()
    {
        try {
            dbHandler.open();
        } catch (SQLException ex) {
            Toast.makeText(getApplicationContext(), R.string.open_db_failed, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {

    }

    @Override
    public void afterTextChanged(Editable s)
    {
        String actualTitle = titleEditText.getText().toString();
        String actualNote = noteEditText.getText().toString();

        if(!StringValidation.isFieldEmpty(actualTitle) && !StringValidation.isFieldEmpty(actualNote)) {
            updateBtn.setEnabled(true);
            deleteBtn.setEnabled(true);
        } else {
            updateBtn.setEnabled(false);
            deleteBtn.setEnabled(false);
        }
    }
}
