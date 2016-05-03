package com.umb.cs682.projectlupus.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.umb.cs682.projectlupus.db.helpers.DaoSession;
import com.umb.cs682.projectlupus.domain.ReminderBO;

import java.util.List;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

public class ReminderDao extends AbstractDao<ReminderBO, Long> {

    public static final String TABLENAME = "REMINDER";


    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property TypeId = new Property(1, Integer.class, "typeId", false, "TYPE_ID");
        public final static Property MedId = new Property(2, long.class, "medId", false, "MED_ID");
        public final static Property ReminderDayOrDate = new Property(3, String.class, "reminderDayOrDate", false, "REMINDER_DAY_DATE");
        public final static Property ReminderTime = new Property(4, java.util.Date.class, "reminderTime", false, "REMINDER_TIME");
        public final static Property Status = new Property(5, String.class, "status", false, "STATUS");
    }

    private DaoSession daoSession;

    private Query<ReminderBO> medicine_MedRemindersQuery;

    public ReminderDao(DaoConfig config) {
        super(config);
    }
    
    public ReminderDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'REMINDER' (" +
                "'_id' INTEGER PRIMARY KEY ," +
                "'TYPE_ID' INTEGER," +
                "'MED_ID' INTEGER NOT NULL ," +
                "'REMINDER_DAY_DATE' TEXT," +
                "'REMINDER_TIME' INTEGER NOT NULL ," +
                "'STATUS' TEXT NOT NULL );");
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'REMINDER'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, ReminderBO entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Integer typeId = entity.getTypeId();
        if (typeId != null) {
            stmt.bindLong(2, typeId);
        }
        stmt.bindLong(3, entity.getMedId());
 
        String reminderDayOrDate = entity.getReminderDayOrDate();
        if (reminderDayOrDate != null) {
            stmt.bindString(4, reminderDayOrDate);
        }
        stmt.bindLong(5, entity.getReminderTime().getTime());
        stmt.bindString(6, entity.getStatus());
    }

    @Override
    protected void attachEntity(ReminderBO entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public ReminderBO readEntity(Cursor cursor, int offset) {
        ReminderBO entity = new ReminderBO(
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
            cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1),
            cursor.getLong(offset + 2),
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3),
            new java.util.Date(cursor.getLong(offset + 4)),
            cursor.getString(offset + 5)
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, ReminderBO entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTypeId(cursor.isNull(offset + 1) ? null : cursor.getInt(offset + 1));
        entity.setMedId(cursor.getLong(offset + 2));
        entity.setReminderDayOrDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setReminderTime(new java.util.Date(cursor.getLong(offset + 4)));
        entity.setStatus(cursor.getString(offset + 5));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(ReminderBO entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(ReminderBO entity) {
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
    
    /** Internal query to resolve the "medReminders" to-many relationship of Medicine. */
    public List<ReminderBO> _queryMedicine_MedReminders(long medId) {
        synchronized (this) {
            if (medicine_MedRemindersQuery == null) {
                QueryBuilder<ReminderBO> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.MedId.eq(null));
                medicine_MedRemindersQuery = queryBuilder.build();
            }
        }
        Query<ReminderBO> query = medicine_MedRemindersQuery.forCurrentThread();
        query.setParameter(0, medId);
        return query.list();
    }

}
