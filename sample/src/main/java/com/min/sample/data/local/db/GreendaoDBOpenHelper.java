package com.min.sample.data.local.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.min.sample.data.local.db.dao.DaoMaster;
import com.min.sample.data.local.db.dao.SearchBeanDao;

import org.greenrobot.greendao.database.StandardDatabase;

/**
 * Created by minyangcheng on 2016/8/5.
 */
public class GreendaoDBOpenHelper extends DaoMaster.OpenHelper {

    private static final String TAG = "GreendaoDBOpenHelper";

    public GreendaoDBOpenHelper(Context context, String dbName) {
        super(context, dbName, null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        StandardDatabase database = new StandardDatabase(db);
        MigrationHelper.migrate(database, SearchBeanDao.class);
    }

}
