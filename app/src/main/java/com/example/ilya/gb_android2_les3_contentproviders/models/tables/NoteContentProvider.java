package com.example.ilya.gb_android2_les3_contentproviders.models.tables;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.ModelAdapter;


public class NoteContentProvider extends ContentProvider {

    public static final String AUTHORITY = "com.example.ilya.gb_android2_les3_contentproviders";
    private static final String ENDPOOINT = "Notes";
    // @ContentUri(path = ENDPOOINT, type = ContentUri.ContentType.VND_MULTIPLE + ENDPOOINT)
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + ENDPOOINT);
    private static final int NOTES_CONTENT_URI = 0;
    private static final UriMatcher MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        MATCHER.addURI(AUTHORITY, ENDPOOINT, NOTES_CONTENT_URI);
    }

    @Override
    public final String getType(Uri uri) {
        String type = null;
        switch (MATCHER.match(uri)) {
            case NOTES_CONTENT_URI: {
                type = "vnd.android.cursor.dir/" + ENDPOOINT;
                break;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI" + uri);
            }
        }
        return type;
    }

    @Override
    public boolean onCreate() {
        return false;
    }

    @Override
    public final Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        android.database.Cursor cursor = null;
        switch (MATCHER.match(uri)) {
            case NOTES_CONTENT_URI: {
                cursor = FlowManager.getDatabase("AppDataBase")
                        .getWritableDatabase()
                        .query("TblNotes", projection, selection, selectionArgs, null, null, sortOrder);
                break;
            }
        }
        if (cursor != null) {
            cursor.setNotificationUri(getContext().getContentResolver(), uri);
        }
        return cursor;
    }

    @Override
    public final Uri insert(Uri uri, ContentValues values) {
        switch (MATCHER.match(uri)) {
            case NOTES_CONTENT_URI: {
                ModelAdapter adapter = FlowManager.getModelAdapter(FlowManager.getTableClassForName("AppDataBase", "TblNotes"));
                final long id = FlowManager.getDatabase("AppDataBase")
                                           .getWritableDatabase()
                                           .insertWithOnConflict("TblNotes", null, values, ConflictAction
                                                   .getSQLiteDatabaseAlgorithmInt(adapter.getInsertOnConflictAction()));
                getContext().getContentResolver().notifyChange(uri, null);
                return ContentUris.withAppendedId(uri, id);
            }
            default: {
                throw new IllegalStateException("Unknown Uri" + uri);
            }
        }
    }


    @Override
    public final int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case NOTES_CONTENT_URI: {
                long count = FlowManager.getDatabase("AppDataBase")
                                        .getWritableDatabase()
                                        .delete("TblNotes", selection, selectionArgs);
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return (int) count;
            }
            default: {
                throw new IllegalArgumentException("Unknown URI" + uri);
            }
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (MATCHER.match(uri)) {
            case NOTES_CONTENT_URI: {
                ModelAdapter adapter = FlowManager.getModelAdapter(FlowManager.getTableClassForName("AppDataBase", "TblNotes"));
                long count = FlowManager
                        .getDatabase("AppDataBase")
                        .getWritableDatabase()
                        .updateWithOnConflict("TblNotes", values, selection, selectionArgs,
                                ConflictAction.getSQLiteDatabaseAlgorithmInt(adapter.getUpdateOnConflictAction()));
                if (count > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return (int) count;
            }
            default: {
                throw new IllegalStateException("Unknown Uri" + uri);
            }
        }
    }
}