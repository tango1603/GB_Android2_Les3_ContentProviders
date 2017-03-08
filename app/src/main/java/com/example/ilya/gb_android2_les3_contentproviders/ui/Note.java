package com.example.ilya.gb_android2_les3_contentproviders.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.ilya.gb_android2_les3_contentproviders.R;
import com.example.ilya.gb_android2_les3_contentproviders.models.tables.TblNotes;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.List;

public class Note extends AppCompatActivity {

    private static final String TAG = "DEBUGGG";
    private EditText noteName;
    private EditText noteText;
    private Intent mIntent;
    private String nodeNameStr;
    private String nodeTextStr;
    private int position = -10;
    private List<TblNotes> tblNotesList;

    private void findEditNode(int nodePosition) {
        if (nodePosition != -10) {
            tblNotesList = new Select().from(TblNotes.class).queryList();
            nodeNameStr = tblNotesList.get(nodePosition).noteName;
            nodeTextStr = tblNotesList.get(nodePosition).noteText;
        } else return;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        noteName = (EditText) findViewById(R.id.etName);
        noteText = (EditText) findViewById(R.id.etText);
        noteName.setFocusableInTouchMode(true);
        noteName.setFocusable(true);
        noteName.setCursorVisible(true);
        mIntent = getIntent();
        position = mIntent.getIntExtra("NodePosition", -10);
        findEditNode(position);
    }

    @Override
    protected void onResume() {
        noteName.setText(nodeNameStr);
        noteText.setText(nodeTextStr);
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.menu_save: {
                if (position == -10) {
                    if (noteName.getText().toString() != "") {
                        Log.d(TAG, "onOptionsItemSelected position: " + position);
                        intent.putExtra("NoteNameAdd", noteName.getText().toString());
                        intent.putExtra("NoteTextAdd", noteText.getText().toString());
                        setResult(RESULT_OK, intent);
                    } else {
                        Log.d(TAG, "RESULT_CANCELED" + position);
                        setResult(RESULT_CANCELED, intent);
                    }
                } else {
                    if (noteName.getText().toString() != "") {
                        Log.d(TAG, "onOptionsItemSelected position: " + position);
                        intent.putExtra("NoteNameEdited", noteName.getText().toString());
                        intent.putExtra("NoteTextEdited", noteText.getText().toString());
                        intent.putExtra("NotePosition", position);
                        setResult(RESULT_OK, intent);
                    } else {
                        Log.d(TAG, "RESULT_CANCELED" + position);
                        setResult(RESULT_CANCELED, intent);
                    }
                }
                finish();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
