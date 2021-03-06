package com.umb.cs682.projectlupus.db.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.umb.cs682.projectlupus.db.dao.ActivitySenseDao;
import com.umb.cs682.projectlupus.db.dao.MedicineDao;
import com.umb.cs682.projectlupus.db.dao.MoodLevelDao;
import com.umb.cs682.projectlupus.db.dao.ProfileDao;
import com.umb.cs682.projectlupus.db.dao.ReminderDao;

import de.greenrobot.dao.AbstractDaoMaster;
import de.greenrobot.dao.identityscope.IdentityScopeType;

public class DaoMaster extends AbstractDaoMaster {
    public static final int SCHEMA_VERSION = 4;
    public static final String DATABASE_NAME = "LupusMateDb";

    /** Creates underlying database table using DAOs. */
    public static void createAllTables(SQLiteDatabase db, boolean ifNotExists) {
        ActivitySenseDao.createTable(db, ifNotExists);
        ProfileDao.createTable(db, ifNotExists);
        MoodLevelDao.createTable(db, ifNotExists);
        ReminderDao.createTable(db, ifNotExists);
        MedicineDao.createTable(db, ifNotExists);
    }
    
    /** Drops underlying database table using DAOs. */
    public static void dropAllTables(SQLiteDatabase db, boolean ifExists) {
        ActivitySenseDao.dropTable(db, ifExists);
        ProfileDao.dropTable(db, ifExists);
        MoodLevelDao.dropTable(db, ifExists);
        ReminderDao.dropTable(db, ifExists);
        MedicineDao.dropTable(db, ifExists);
    }
    
    public static abstract class OpenHelper extends SQLiteOpenHelper {
        public OpenHelper(Context context, CursorFactory factory) {
            super(context, DATABASE_NAME, factory, SCHEMA_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i("greenDAO", "Creating tables for schema version " + SCHEMA_VERSION);
            createAllTables(db, false);
        }
    }
    
    public static class DevOpenHelper extends OpenHelper {
        public DevOpenHelper(Context context, CursorFactory factory) {
            super(context, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion + " by dropping all tables");
            dropAllTables(db, true);
            onCreate(db);
        }
    }

    public static class DBOpenHelper extends OpenHelper {
        public DBOpenHelper(Context context, CursorFactory factory) {
            super(context, factory);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i("greenDAO", "Upgrading schema from version " + oldVersion + " to " + newVersion);
            onCreate(db);
        }
    }

    public DaoMaster(SQLiteDatabase db) {
        super(db, SCHEMA_VERSION);
        registerDaoClass(ActivitySenseDao.class);
        registerDaoClass(ProfileDao.class);
        registerDaoClass(MoodLevelDao.class);
        registerDaoClass(ReminderDao.class);
        registerDaoClass(MedicineDao.class);
    }
    
    public DaoSession newSession() {
        return new DaoSession(db, IdentityScopeType.Session, daoConfigMap);
    }
    
    public DaoSession newSession(IdentityScopeType type) {
        return new DaoSession(db, type, daoConfigMap);
    }
    
}
