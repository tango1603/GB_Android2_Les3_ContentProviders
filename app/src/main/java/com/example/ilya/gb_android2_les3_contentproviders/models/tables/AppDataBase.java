package com.example.ilya.gb_android2_les3_contentproviders.models.tables;

import com.raizlabs.android.dbflow.annotation.Database;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.migration.BaseMigration;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;

/**
 * Created by Ilya on 04.03.2017.
 */
@Database(name = AppDataBase.NAME, version = AppDataBase.VERSION)
public class AppDataBase {
    public static final String NAME = "DB_Note";
    public static final int VERSION = 1;

    @Migration(version = 2, database = AppDataBase.class)
    public static class Migration2 extends BaseMigration {

        @Override
        public void migrate(DatabaseWrapper database) {
//// TODO: 04.03.2017 сделать миграцию с изменением структуры базы: добавится поле дата создания заметки
        }
    }
}
