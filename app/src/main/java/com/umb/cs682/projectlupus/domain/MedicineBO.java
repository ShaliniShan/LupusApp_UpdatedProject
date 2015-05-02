package com.umb.cs682.projectlupus.domain;

import java.util.List;

import com.umb.cs682.projectlupus.db.dao.MedicineDao;
import com.umb.cs682.projectlupus.db.dao.ReminderDao;
import com.umb.cs682.projectlupus.db.helpers.DaoSession;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table MEDICINE.
 */
public class MedicineBO {

    private Long id;
    /** Not-null value. */
    private String medName;
    private int dosage;
    /** Not-null value. */
    private String interval;
    private String notes;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient MedicineDao myDao;

    private List<ReminderBO> medReminders;

    public MedicineBO() {
    }

    public MedicineBO(Long id) {
        this.id = id;
    }

    public MedicineBO(Long id, String medName, int dosage, String interval, String notes) {
        this.id = id;
        this.medName = medName;
        this.dosage = dosage;
        this.interval = interval;
        this.notes = notes;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMedicineDao() : null;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /** Not-null value. */
    public String getMedName() {
        return medName;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setMedName(String medName) {
        this.medName = medName;
    }

    public int getDosage() {
        return dosage;
    }

    public void setDosage(int dosage) {
        this.dosage = dosage;
    }

    /** Not-null value. */
    public String getInterval() {
        return interval;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setInterval(String interval) {
        this.interval = interval;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    /** To-many relationship, resolved on first access (and after reset). Changes to to-many relations are not persisted, make changes to the target entity. */
    public List<ReminderBO> getMedReminders() {
        if (medReminders == null) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ReminderDao targetDao = daoSession.getReminderDao();
            List<ReminderBO> medRemindersNew = targetDao._queryMedicine_MedReminders(id);
            synchronized (this) {
                if(medReminders == null) {
                    medReminders = medRemindersNew;
                }
            }
        }
        return medReminders;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    public synchronized void resetMedReminders() {
        medReminders = null;
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

}
