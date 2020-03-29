package edu.montclair.sqliteproject.view;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import edu.montclair.sqliteproject.R;
import edu.montclair.sqliteproject.database.DatabaseHelper;
import edu.montclair.sqliteproject.database.model.Course;
import edu.montclair.sqliteproject.database.utils.MyDividerItemDecoration;
import edu.montclair.sqliteproject.database.utils.RecyclerTouchListener;

public class MainActivity extends AppCompatActivity implements CoursesAdapter.OnItemClickListener{

    private CoursesAdapter mAdapter;
    private List<Course> courseList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noCoursesView;

    private DatabaseHelper db;

    @Override
    public void onItemClick(int position) {
        courseList.get(position);
        Intent intent = new Intent (this, studentActivity.class );
        intent.putExtra("course", )
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycler_view);
        noCoursesView = findViewById(R.id.empty_notes_view);


        db = new DatabaseHelper(this);

        courseList.addAll(db.getAllCourses());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewCourseDialog(false, null, -1);
            }
        });

        mAdapter = new CoursesAdapter(this, courseList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(mAdapter);

        toggleEmptyNotes();

        /**
         * On long press on RecyclerView item, open alert dialog
         * with options to choose
         * Edit and Delete
         * */
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                showCourseActionDialog(position);
            }
        }));
    }

    /**
     * Inserting new course in db
     * and refreshing the list
     */
    private void createCourse(String cName, String cID) {
        // inserting course in db and getting
        // newly inserted course id
        Long id = db.insertCourse(cName, cID);

        // get the newly inserted course from db
        Course course = db.getCourse(id);

        if (course != null) {
            // adding new note to array list at 0 position
            courseList.add(0, course);

            // refreshing the list
            mAdapter.notifyDataSetChanged();

            toggleEmptyNotes();
        }
    }

    /**
     * Updating note in db and updating
     * item in the list by its position
     */
    private void updateCourse(String cID, String cName, int position) {
        Course course = courseList.get(position);
        // updating note text
        course.setcID(cID);
        course.setcName(cName);


        // updating note in db
        db.updateCourse(course);

        // refreshing the list
        courseList.set(position, course);
        mAdapter.notifyItemChanged(position);

        toggleEmptyNotes();
    }

    /**
     * Deleting course from SQLite and removing the
     * item from the list by its position
     */
    private void deleteCourse(int position) {
        // deleting the note from db
        db.deleteCourse(courseList.get(position));

        // removing the note from the list
        courseList.remove(position);
        mAdapter.notifyItemRemoved(position);

        toggleEmptyNotes();
    }

    /**
     * Opens dialog with Edit - Delete options for courses
     * Edit - 0
     * Delete - 0
     */
    private void showCourseActionDialog(final int position) {
        CharSequence colors[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose option");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showNewCourseDialog(true, courseList.get(position), position);
                } else {
                    deleteCourse(position);
                }
            }
        });
        builder.show();
    }


    /**
     * Shows alert dialog with EditText options to enter / edit
     * a note.
     * when shouldUpdate=true, it automatically displays old note and changes the
     * button text to UPDATE
     */
    private void showNewCourseDialog(final boolean shouldUpdate, final Course course, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getApplicationContext());
        View view = layoutInflaterAndroid.inflate(R.layout.new_course_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilderUserInput.setView(view);

        final EditText inputCourseName = view.findViewById(R.id.cID_box);
        final EditText inputCourseID = view.findViewById(R.id.cName_box);
        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_course_title) : getString(R.string.lbl_edit_course_title));

        if (shouldUpdate && course != null) {
            inputCourseName.setText(course.getcName());
            inputCourseID.setText(course.getcID());
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show toast message when no text is entered
                if (TextUtils.isEmpty(inputCourseName.getText().toString()) || TextUtils.isEmpty(inputCourseID.getText().toString())) {
                    Toast.makeText(MainActivity.this, "Must fill all fields!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                // check if user updating course
                if (shouldUpdate && course != null) {
                    // update course by it's id
                    updateCourse(inputCourseID.getText().toString(), inputCourseName.getText().toString(), position);
                } else {
                    // create new course
                    createCourse(inputCourseID.getText().toString(), inputCourseName.getText().toString());
                }
            }
        });
    }

    /**
     * Toggling list and empty notes view
     */
    private void toggleEmptyNotes() {
        // you can check notesList.size() > 0

        if (db.getCoursesCount() > 0) {
            noCoursesView.setVisibility(View.GONE);
        } else {
            noCoursesView.setVisibility(View.VISIBLE);
        }
    }
}
