package edu.montclair.sqliteproject.view;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import edu.montclair.sqliteproject.R;
import edu.montclair.sqliteproject.database.DatabaseHelper;
import edu.montclair.sqliteproject.database.model.Course;
import edu.montclair.sqliteproject.database.model.Reservation;

public class studentActivity extends AppCompatActivity {

    private ReservationsAdapter mAdapter;
    private List<Reservation> resList = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView noCoursesView;

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
