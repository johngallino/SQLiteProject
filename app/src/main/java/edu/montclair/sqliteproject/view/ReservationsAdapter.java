package edu.montclair.sqliteproject.view;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.montclair.sqliteproject.R;
import edu.montclair.sqliteproject.database.model.Course;
import edu.montclair.sqliteproject.database.model.Reservation;

public class ReservationsAdapter extends RecyclerView.Adapter<ReservationsAdapter.MyViewHolder> {

    private Context context;
    private List<Reservation> resList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView sName; // student name
        public TextView dot;
        public TextView sID; // student ID
        public TextView sYear; // student school year

        public MyViewHolder(View view) {
            super(view);
            sName = view.findViewById(R.id.sName_row);
            dot = view.findViewById(R.id.dot);
            sID = view.findViewById(R.id.sID_row);
            sYear = view.findViewById(R.id.sYear_row);
        }
    }

    public ReservationsAdapter(Context context, List<Reservation> resList) {
        this.context = context;
        this.resList = resList;
    }

    @Override
    public ReservationsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list_row, parent, false);

        return new ReservationsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Reservation reservation = resList.get(position);

        holder.sName.setText(reservation.getStudent().getsName());

        // Displaying dot from HTML character code
        holder.dot.setText(Html.fromHtml("&#8226;"));

        holder.sID.setText(reservation.getStudent().getsID());
        holder.sYear.setText(reservation.getStudent().getsStatus());
    }

    @Override
    public int getItemCount() {
        return resList.size();
    }

}
