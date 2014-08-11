package net.mobindustry.calendarsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter to fill month grid properly based on given collection of GridCellModel.
 * Created by Den Drobiazko on 11.08.14.
 */
public class MonthGridAdapter extends BaseAdapter {

    private String LOG_TAG;

    private Context context;
    private ArrayList<GridCellModel> days = new ArrayList<GridCellModel>();

    public MonthGridAdapter(Context context, ArrayList<GridCellModel> daysList) {
        LOG_TAG = this.getClass().getSimpleName();
        this.context = context;
        this.days = daysList;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        GridCellModel day = days.get(position);
        LayoutInflater mInflater = LayoutInflater.from(context);
        ViewGroup cellView = (ViewGroup) mInflater.inflate(R.layout.grid_cell, viewGroup, false);

        TextView dateTv = (TextView) cellView.findViewById(R.id.grid_cell_tv);
        if(day.isEmptyCell()) {
            cellView.setBackgroundResource(R.drawable.gridcell_inactive_selector);
        } else {
            dateTv.setText(Integer.toString(day.getDayNumber()));
            if(day.isToday()) {
                cellView.setBackgroundResource(R.drawable.gridcell_today_selector);
            } else {
                cellView.setBackgroundResource(R.drawable.gridcell_selector);
            }
            if(day.isHoliday()) {
                dateTv.setTextColor(context.getResources().getColor(android.R.color.holo_red_light));
            }
        }
        return cellView;
    }

    @Override
    public int getCount() {
        return days.size();
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
}
