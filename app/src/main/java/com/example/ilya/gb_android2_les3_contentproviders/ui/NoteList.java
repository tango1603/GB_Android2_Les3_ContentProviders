package com.example.ilya.gb_android2_les3_contentproviders.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ilya.gb_android2_les3_contentproviders.R;
import com.example.ilya.gb_android2_les3_contentproviders.models.tables.TblNotes;
import com.example.ilya.gb_android2_les3_contentproviders.models.tables.TblNotes_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import java.util.ArrayList;
import java.util.List;

public class NoteList extends AppCompatActivity {

    private static final String TAG = "DEBUGGG";

    private ListView lv_notes;
    private ArrayAdapter<String> mStringArrayAdapter;
    private List<String> elements = new ArrayList<>();
    private Intent mIntent;
    private static final int REQUEST_CODE_NOTE_ADD = 1;
    private static final int REQUEST_CODE_NOTE_EDIT = 2;
    private String noteName;
    private String noteText;
    private int notePosition;
    private List<TblNotes> tblNotesList;

    /**
     * Загружаем список из БД
     */
    private void loadActState() {
        tblNotesList = new Select().from(TblNotes.class).queryList();
        for (int i = 0; i < tblNotesList.size(); i++) {
            if (!tblNotesList.get(i).noteName.equals("")) {
                elements.add(i, tblNotesList.get(i).noteName);
            } else {
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        lv_notes = (ListView) findViewById(R.id.list_notes);
        loadActState();
        mStringArrayAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_activated_1, elements);
        lv_notes.setAdapter(mStringArrayAdapter);
        lv_notes.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                //обработчик выделения пунктов списка ActionMode
                notePosition = position;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // обработка нажатия на пункт ActionMode
                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        mIntent = new Intent(NoteList.this, Note.class);
                        mIntent.putExtra("NodePosition", notePosition);
                        startActivityForResult(mIntent, REQUEST_CODE_NOTE_EDIT);
                        mode.finish();
                        return true;
                    case R.id.menu_Delete:
                        removeNote();
                        mode.finish();
                        return true;
                }
                mode.finish();
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // Устанавливаем для ActionMode меню
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.context_menu, menu);
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // вызывается при закрытии ActionMode
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // вызывается при обновлении ActionMode
                // true если меню или ActionMode обновлено иначе false
                return false;
            }
        });
        lv_notes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add: {
                mIntent = new Intent(NoteList.this, Note.class);
                startActivityForResult(mIntent, REQUEST_CODE_NOTE_ADD);
                return true;
            }
            case R.id.menu_clear: {
                noteClear();
                return true;
            }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_NOTE_ADD: {
                    noteName = data.getStringExtra("NoteNameAdd");
                    noteText = data.getStringExtra("NoteTextAdd");
                    if (!noteName.isEmpty()) {
                        addElement(noteName, noteText);
                    }
                    break;
                }

                case REQUEST_CODE_NOTE_EDIT: {
                    noteName = data.getStringExtra("NoteNameEdited");
                    noteText = data.getStringExtra("NoteTextEdited");
                    notePosition = data.getIntExtra("NotePosition", -10);
                    if (!noteName.isEmpty() && notePosition != -10) {
                        editElement(notePosition, noteName, noteText);
                    }
                    break;
                }
            }
        }
    }

    /**
     * Сохранение в БД, обновление списка
     *
     * @param noteName имя заметки
     * @param noteText текст заметки
     */
    private void addElement(String noteName, String noteText) {
        elements.add(noteName);
        TblNotes tblNotes = new TblNotes();
        tblNotes.setNoteName(noteName);
        tblNotes.setNoteText(noteText);
        tblNotes.setListPosition(elements.size() - 1);
        tblNotes.save();
        mStringArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Редактируем заметку
     *
     * @param notePosition позиция в списке
     * @param noteName     имя заметки
     * @param noteText     текст заметки
     */
    private void editElement(int notePosition, String noteName, String noteText) {
        elements.set(notePosition, noteName);
        SQLite.update(TblNotes.class)
                .set(TblNotes_Table.noteName.eq(noteName), TblNotes_Table.noteText.eq(noteText))
                .where(TblNotes_Table.listPosition.is(notePosition))
                .async()
                .execute();
        mStringArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Удаляем все записи
     */
    private void noteClear() {
        elements.clear();
        SQLite.delete(TblNotes.class)
                .async()
                .execute();
        mStringArrayAdapter.notifyDataSetChanged();
    }


    /**
     * Удаляем выбранную запись
     */
    private void removeNote() {
        elements.remove(notePosition);
        SQLite.delete(TblNotes.class)
                .where(TblNotes_Table.listPosition.is(notePosition))
                .async()
                .execute();
        mStringArrayAdapter.notifyDataSetChanged();
    }
}
