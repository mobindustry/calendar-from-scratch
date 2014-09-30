package net.mobindustry.calendarsample;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.mobindustry.calendarsample.model.GridCellModel;

import java.util.ArrayList;

/**
 * Adapter to fill month grid properly based on given collection of GridCellModel.
 */
public class MonthGridAdapter extends BaseAdapter {

    private String LOG_TAG;

    private Context context;
    private ArrayList<GridCellModel> days = new ArrayList<GridCellModel>();

    /**
     * Cells will be of this height by default - to make them square.
     */
    private int expectedMinimumHeight;

    public MonthGridAdapter(Context context, ArrayList<GridCellModel> daysList) {
        LOG_TAG = this.getClass().getSimpleName();
        this.context = context;
        this.days = daysList;
        expectedMinimumHeight = calculateMinimumHeight();
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
                dateTv.setTypeface(dateTv.getTypeface(), Typeface.BOLD);
                TextView holidayName = new TextView(context);
                holidayName.setText(day.getHoliday().getEnglishName());
                holidayName.setTextSize(10);
                holidayName.setMaxLines(2);
                holidayName.setEllipsize(TextUtils.TruncateAt.END);
                cellView.addView(holidayName);
            }
        }
        cellView.setMinimumHeight(expectedMinimumHeight);
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

    /**
     * Calculates minimum cell height - to make them close to square.
     * @return cell's minimum height, pixels.
     */
    private int calculateMinimumHeight() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels;
        return ((int) dpWidth + 2) / 7;
    }
}
