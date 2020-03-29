package edu.montclair.sqliteproject.database.model;

public class Course {

    public static final String TABLE_NAME = "coursesTable";
    public static final String COLUMN_ID="_id";
    public static final String COLUMN_CLASSID = "classID"; // CSIT507-01
    public static final String COLUMN_CLASSNAME = "className"; // Machine Learning

    private int _id;
    private String cID;
    private String cName;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CLASSID + " TEXT, "
                    + COLUMN_CLASSNAME+ " TEXT"
                    + ")";

    // Populate course table
    public static final String POPULATE_COURSES =
            "INSERT INTO " + TABLE_NAME + " ("
                    + COLUMN_CLASSID + ", "
                    + COLUMN_CLASSNAME
                    + ") VALUES "
                    + "('CSIT-111', 'Fundamentals of Programming'), ('CSIT-114', 'Python Programming'), ('CSIT-212', 'Data Structures');";

    public Course(){
    }

    public Course(String cID, String cName) {
        this.cID = cID;
        this.cName = cName;
    }

    public int get_id() {
        return _id;
    }

    public String getcID() {
        return cID;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }
}
