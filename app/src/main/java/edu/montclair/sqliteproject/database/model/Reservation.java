package edu.montclair.sqliteproject.database.model;

public class Reservation {
    public static final String TABLE_NAME = "waitingList";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_CLASSCOURSENUM = "classCourseNum"; // CSIT 507-01
    public static final String COLUMN_STUDENTID = "studentID"; // M21709653
    public static final String COLUMN_TIMESTAMP = "timestamp"; //timestamp

    private int id;
    private Student student;
    private Course course;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_CLASSCOURSENUM + " TEXT,"
                    + COLUMN_STUDENTID + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Reservation() {
    }

    public Reservation(int id, Student student, Course course, String timestamp) {
        this.id = id;
        this.student = student;
        this.course = course;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
