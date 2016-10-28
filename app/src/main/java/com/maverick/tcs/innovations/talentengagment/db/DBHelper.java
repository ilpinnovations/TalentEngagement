package com.maverick.tcs.innovations.talentengagment.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.maverick.tcs.innovations.talentengagment.beans.ActivityBean;

import java.util.ArrayList;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EmployeeActivities.db";
    public static final String TABLE_NAME = "activities";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMPLOYEE_ID = "emp_id";
    public static final String COLUMN_ACTIVITY_ID = "activity_id";
    public static final String COLUMN_TIME = "time";




    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table activities " +
                        "(id integer primary key, emp_id text,activity_id text,time text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean insertActivity(String empId, String activityId, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ACTIVITY_ID, activityId);
        contentValues.put(COLUMN_EMPLOYEE_ID, empId);
        contentValues.put(COLUMN_TIME, time);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        return numRows;
    }


    public Integer deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, null, null);
    }

    public ArrayList<ActivityBean> getAllActivities() {
        ArrayList<ActivityBean> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            String activityId = res.getString(res.getColumnIndex(COLUMN_ACTIVITY_ID));
            String employeeId = res.getString(res.getColumnIndex(COLUMN_EMPLOYEE_ID));
            String time = res.getString(res.getColumnIndex(COLUMN_TIME));

            ActivityBean activityBean = new ActivityBean(activityId, employeeId, time);
            array_list.add(activityBean);
            res.moveToNext();
        }

        res.close();
        db.close();
        return array_list;
    }
}
