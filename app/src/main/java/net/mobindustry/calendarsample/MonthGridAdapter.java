package net.mobindustry.calendarsample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Adapter to fill month grid properly based on given collection of GridCellModel.
 * Created by Den Drobiazko on 11.08.14.
 */
public class MonthGridAdapter extends BaseAdapter {

    private String LOG_TAG;

    private Context context;
    private DateTime dateTime;
    private int cellsCount, emptyCellsAtStart, daysInMonth, emptyCellsInTheEnd;
    private ArrayList<GridCellModel> days = new ArrayList<GridCellModel>();

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

    public void setDateTime(DateTime dateTime) {
        this.dateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(), 0, 0, 0);
        emptyCellsAtStart = this.dateTime.withDayOfMonth(1).getDayOfWeek() - 1;
        daysInMonth = this.dateTime.dayOfMonth().getMaximumValue();
        emptyCellsInTheEnd = 7 - this.dateTime.withDayOfMonth(daysInMonth).getDayOfWeek();
        cellsCount = emptyCellsAtStart + daysInMonth + emptyCellsInTheEnd;
        initiateCellArray();
    }

    private void initiateCellArray() {
        for(int i = 0; i < emptyCellsAtStart; i++) {
            days.add(new GridCellModel());
        }
        for(int i = 0; i < daysInMonth; i++) {
            GridCellModel mModel = new GridCellModel()
                .setEmptyCell(false)
                .setDateTime(dateTime.withDayOfMonth(i + 1));
            days.add(mModel);
        }
        for(int i = 0; i < emptyCellsInTheEnd; i++) {
            days.add(new GridCellModel());
        }
    }

    public MonthGridAdapter(Context context) {
        LOG_TAG = this.getClass().getSimpleName();
        this.context = context;
    }

    @Override
    public int getCount() {
        return cellsCount;
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
