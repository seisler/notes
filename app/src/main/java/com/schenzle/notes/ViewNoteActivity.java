package com.schenzle.notes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;


public class ViewNoteActivity extends AppCompatActivity
{
    Toolbar toolbar;
    DBHandler dbHandler;
    Intent intent;
    long noteId;
    String title, note;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHandler = new DBHandler(this);

        getIntentData();
        setDataIntoView();

    }

    private void getIntentData()
    {
        intent = getIntent();
        noteId = intent.getLongExtra("id", 0);
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("note");
    }

    private void setDataIntoView()
    {
        TextView title = (TextView) findViewById(R.id.titleTextView);
        title.setText(String.valueOf(noteId));
    }


}
