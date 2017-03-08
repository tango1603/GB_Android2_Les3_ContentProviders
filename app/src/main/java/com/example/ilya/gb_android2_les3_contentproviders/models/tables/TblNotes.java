package com.example.ilya.gb_android2_les3_contentproviders.models.tables;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by Ilya on 04.03.2017.
 * База данных приложения
 */
@Table(database = AppDataBase.class)
public class TblNotes extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = true)
    public long id;
    @Column
    public String noteName;
    @Column
    public String noteText;
    @Column
    public int listPosition;

    public TblNotes(String noteName, String noteText, int listNotePosition) {
        this.noteName = noteName;
        this.noteText = noteText;
        this.listPosition = listNotePosition;
    }

    public long getId() {
        return id;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }

    public TblNotes() {
    }

}
