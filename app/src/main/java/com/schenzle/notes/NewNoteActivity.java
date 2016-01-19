package com.schenzle.notes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.sql.SQLException;
import java.util.ArrayList;

public class NewNoteActivity extends AppCompatActivity implements TextWatcher
{
    private EditText titleEditText, noteEditText;
    private DBHandler dbHandler;
    public Toolbar toolbar;
    private Button saveBtn;
    private LinearLayout form;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setEnabled(false);

        startValidation();

        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveNote();
                finish();
            }
        });
    }

    /**
     * Start the form validation
     *
     */
    public void startValidation()
    {
        titleEditText = (EditText) findViewById(R.id.editTitle);
        noteEditText = (EditText) findViewById(R.id.editNote);

        titleEditText.addTextChangedListener(this);
        noteEditText.addTextChangedListener(this);
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
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Close DB
     */
    private void closeDb()
    {
        dbHandler.close();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s)
    {
        String title = titleEditText.getText().toString();
        String note = noteEditText.getText().toString();

        if(!StringValidation.isFieldEmpty(title) && !StringValidation.isFieldEmpty(note)) {
            saveBtn.setEnabled(true);
        } else {
            saveBtn.setEnabled(false);
        }
    }
}
