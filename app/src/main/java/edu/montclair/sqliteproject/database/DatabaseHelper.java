package edu.montclair.sqliteproject.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.montclair.sqliteproject.database.model.Course;
import edu.montclair.sqliteproject.database.model.Reservation;
import edu.montclair.sqliteproject.database.model.Student;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "waiting_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Courses table
        db.execSQL(Course.CREATE_TABLE);
        Log.d("coursesTable", "created");

        // populate Courses table
        db.execSQL(Course.POPULATE_COURSES);
        Log.d("coursesTable", "populated with courses");

        // create Students table
        db.execSQL(Student.CREATE_TABLE);
        Log.d("studentsTable", "created");

        // create Reservation table
        db.execSQL(Reservation.CREATE_TABLE);
        Log.d("waitingList", "created");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + Reservation.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    // Add a course
    public long insertCourse(String cName, String cID) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Course.COLUMN_CLASSID, cID);
        values.put(Course.COLUMN_CLASSNAME, cName);

        // insert row
        long id = db.insert(Course.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    // Add a student
    public long insertStudent(String sName, String sID, String sYear, String sGrad) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Student.COLUMN_STUDENTNAME, sName);
        values.put(Student.COLUMN_STUDENTID, sName);
        values.put(Student.COLUMN_STUDENTYEAR, sYear);
        values.put(Student.COLUMN_STUDENTGRAD, sGrad);

        // insert row
        long id = db.insert(Student.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    // Add a course reservation
    public long insertReservation(Student student, Course course) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put(Reservation.COLUMN_STUDENTID, student.getsID());
        values.put(Reservation.COLUMN_CLASSCOURSENUM, course.getcID());


        // insert row
        long id = db.insert(Reservation.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public Reservation getReservation(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        String idString = String.valueOf(id);
        // This query joins reservation table, student table, and courses table
        String myquery = "SELECT * "
                + "FROM " + Reservation.TABLE_NAME + " INNER JOIN " + Student.TABLE_NAME + " ON " + Reservation.TABLE_NAME + ".COLUMN_STUDENTID = " + Student.TABLE_NAME + ".COLUMN_STUDENTID "
                + "INNER JOIN coursesTable ON waitingList.COLUMN_CLASSCOURSENUM = coursesTable.COLUMN_CLASSID "
                + "WHERE COLUMN_ID =?";

        //rawQuery(String sql, String[] selectionArgs, CancellationSignal cancellationSignal)

        // build db query searching for particular reservation ID
        Cursor cursor = db.rawQuery(myquery, new String[]{idString});

        if (cursor != null)
            cursor.moveToFirst();

        Student student = new Student (
                // Priority generated automatically from year and grad fields
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTNAME)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTID)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTYEAR)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTGRAD))
        );

        Course course = new Course (
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSID)),
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSNAME))
        );


        // prepare class reservation object
        Reservation reservation = new Reservation(
                cursor.getInt(cursor.getColumnIndex(Reservation.COLUMN_ID)),
                student,
                course,
                cursor.getString(cursor.getColumnIndex(Reservation.COLUMN_TIMESTAMP)));

        // close the db connection
        cursor.close();

        return reservation;
    }

    public Course getCourse(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        String idString = String.valueOf(id);
        // This query searches for a particular course by its ID
        String myquery = "SELECT * "
                + "FROM " + Course.TABLE_NAME
                + " WHERE "
                + Course.COLUMN_ID +" =?";

        // build db query searching for particular course ID
        Cursor cursor = db.rawQuery(myquery, new String[]{idString});

        if (cursor != null)
            cursor.moveToFirst();

        Course course = new Course (
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSID)),
                cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSNAME))
        );

        // close the db connection
        cursor.close();

        return course;
    }

    public List<Reservation> getAllReservations() {
        List<Reservation> entries = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Reservation.TABLE_NAME + " INNER JOIN " + Student.TABLE_NAME + " ON " + Reservation.TABLE_NAME + ".COLUMN_STUDENTID = " + Student.TABLE_NAME + ".COLUMN_STUDENTID "
                + "INNER JOIN coursesTable ON waitingList.COLUMN_CLASSCOURSENUM = coursesTable.COLUMN_CLASSID "
                + " ORDER BY " +
                Student.COLUMN_PRIORITY + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Student student = new Student (
                        // Priority generated automatically from year and grad fields
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTNAME)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTID)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTYEAR)),
                        cursor.getString(cursor.getColumnIndex(Student.COLUMN_STUDENTGRAD))
                );

                Course course = new Course (
                        cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSID)),
                        cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSNAME))
                );

                Reservation reservation = new Reservation();
                reservation.setId(cursor.getInt(cursor.getColumnIndex(Reservation.COLUMN_ID)));
                reservation.setStudent(student);
                reservation.setCourse(course);
                reservation.setTimestamp(cursor.getString(cursor.getColumnIndex(Reservation.COLUMN_TIMESTAMP)));

                entries.add(reservation);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return crs list
        return entries;
    }

    public List<Course> getAllCourses() {
        List<Course> allCourses = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + Course.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Course course = new Course();
                course.setcID(cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSID)));
                course.setcName(cursor.getString(cursor.getColumnIndex(Course.COLUMN_CLASSNAME)));
                allCourses.add(course);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return crs list
        return allCourses;
    }

    public int getReservationCount() {
        String countQuery = "SELECT  * FROM " + Reservation.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int getCoursesCount() {
        String countQuery = "SELECT  * FROM " + Course.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();

        // return count
        return count;
    }

    public int updateReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Reservation.COLUMN_CLASSCOURSENUM, reservation.getCourse().getcID());
        values.put(Reservation.COLUMN_STUDENTID, reservation.getStudent().getsID());

        // updating row
        return db.update(Reservation.TABLE_NAME, values, Reservation.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reservation.getId())});
    }

    public void deleteReservation(Reservation reservation) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Reservation.TABLE_NAME, Reservation.COLUMN_ID + " = ?",
                new String[]{String.valueOf(reservation.getId())});
        db.close();
    }

    public int updateCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Course.COLUMN_CLASSNAME, course.getcName());
        values.put(Course.COLUMN_CLASSID, course.getcID());

        // updating row
        return db.update(Course.TABLE_NAME, values, Course.COLUMN_CLASSID + " = ?",
                new String[]{String.valueOf(course.getcID())});
    }

    public void deleteCourse(Course course) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Course.TABLE_NAME, Course.COLUMN_CLASSID + " = ?",
                new String[]{String.valueOf(course.getcID())});
        db.close();
    }
}
