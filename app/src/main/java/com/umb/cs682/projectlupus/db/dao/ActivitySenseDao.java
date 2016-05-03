package com.umb.cs682.projectlupus.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.umb.cs682.projectlupus.db.helpers.DaoSession;
import com.umb.cs682.projectlupus.domain.ActivitySenseBO;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

public class ActivitySenseDao extends AbstractDao<ActivitySenseBO, Long> {

    public static final String TABLENAME = "ACTIVITY_SENSE";

    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property StepCount = new Property(1, int.class, "stepCount", false, "STEP_COUNT");
        public final static Property Date = new Property(2, java.util.Date.class, "date", false, "DATE");
    }


    public ActivitySenseDao(DaoConfig config) {
        super(config);
    }
    
    public ActivitySenseDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'ACTIVITY_SENSE' (" +
                "'_id' INTEGER PRIMARY KEY ," +
                "'STEP_COUNT' INTEGER NOT NULL ," +
                "'DATE' INTEGER NOT NULL );");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'ACTIVITY_SENSE'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ActivitySenseBO entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindLong(2, entity.getStepCount());
        stmt.bindLong(3, entity.getDate().getTime());
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ActivitySenseBO readEntity(Cursor cursor, int offset) {
        ActivitySenseBO entity = new ActivitySenseBO(
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
            cursor.getInt(offset + 1),
            new java.util.Date(cursor.getLong(offset + 2))
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ActivitySenseBO entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setStepCount(cursor.getInt(offset + 1));
        entity.setDate(new java.util.Date(cursor.getLong(offset + 2)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ActivitySenseBO entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ActivitySenseBO entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
