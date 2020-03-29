package edu.montclair.sqliteproject.database.model;

public class Student {

    public static final String TABLE_NAME = "studentsTable";

    public static final String COLUMN_STUDENTNAME = "studentName"; // John Gallino
    public static final String COLUMN_STUDENTID = "studentID"; // M21709653
    public static final String COLUMN_STUDENTYEAR = "studentYear"; // 2022
    public static final String COLUMN_STUDENTGRAD = "studentGrad"; // Graduate Student? 0 or 1
    public static final String COLUMN_PRIORITY = "sPriority"; // A through F (lowest)

    private String sName;
    private String sID;
    private String sYear;
    private String sGrad;
    private String sPriority;
    private String sStatus;

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_STUDENTID + " TEXT PRIMARY KEY,"
                    + COLUMN_STUDENTNAME + " TEXT,"
                    + COLUMN_STUDENTYEAR + " INTEGER,"
                    + COLUMN_STUDENTGRAD + " INTEGER,"
                    + COLUMN_PRIORITY + " TEXT"
                    + ")";

    public Student() {
    }

    public Student(String sName, String sID, String sYear, String sGrad) {
        this.sName = sName;
        this.sID = sID;
        this.sYear = sYear;
        this.sGrad = sGrad;
        // Assign priority based on expected year of graduation
        if (sGrad.equals("1")) { this.sPriority = "A";}
        else if (sYear.equals("2020")){ this.sPriority = "B";}
        else if (sYear.equals("2021")){ this.sPriority = "C";}
        else if (sYear.equals("2022")){ this.sPriority = "D";}
        else if (sYear.equals("2023")){ this.sPriority = "E";}
        else {this.sPriority = "F";};

        // Build student status String
        if (sGrad.equals("1")) { this.sStatus = "Graduate Student";}
        else if (sYear.equals("2020")){ this.sStatus = "Senior Undergrad";}
        else if (sYear.equals("2021")){ this.sStatus = "3rd Year Undergrad";}
        else if (sYear.equals("2022")){ this.sStatus = "2nd Year Undergrad";}
        else if (sYear.equals("2023")){ this.sStatus = "Freshman Undergrad";}
        else { this.sStatus = "Graduating " + sYear;}

    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsYear() {
        return sYear;
    }

    public void setsYear(String sYear) {
        this.sYear = sYear;
    }

    public String getsGrad() {
        return sGrad;
    }

    public void setsGrad(String sGrad) {
        this.sGrad = sGrad;
    }

    public String getsPriority() {
        return sPriority;
    }

    public String getsStatus() {
        return sStatus;
    }
}
