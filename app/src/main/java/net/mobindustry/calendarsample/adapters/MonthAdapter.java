package net.mobindustry.calendarsample.adapters;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import net.mobindustry.calendarsample.R;
import net.mobindustry.calendarsample.model.DayModel;

import java.util.List;

/**
 * Adapter to fill month grid properly based on given collection of GridCellModel.
 */
public class MonthAdapter extends BaseAdapter {

    /**
     * max amount of lines for holiday text view
     */
    private final static int MAX_LINES = 2;

    private final static int DAYS_COUNT = 7;

    private List<DayModel> days;
    private LayoutInflater mInflater;
    private Context context;

    /**
     * Cells will be of this height by default - to make them square.
     */
    private int expectedMinimumHeight;

    public MonthAdapter(Context context, List<DayModel> daysList) {
        this.mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.days = daysList;
        expectedMinimumHeight = calculateMinimumHeight();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_cell, viewGroup, false);

            holder = new ViewHolder();
            holder.textViewDate = (TextView) convertView.findViewById(R.id.grid_cell_tv);
            holder.viewGroup = (ViewGroup) convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DayModel day = getItem(position);

        if (day.isEmptyCell()) {
            holder.viewGroup.setBackgroundResource(R.drawable.gridcell_inactive_selector);
        } else {
            holder.textViewDate.setText(Integer.toString(day.getDayNumber()));
            if (day.isToday()) {
                holder.viewGroup.setBackgroundResource(R.drawable.gridcell_today_selector);
            } else {
                holder.viewGroup.setBackgroundResource(R.drawable.gridcell_selector);
            }
            if (day.isHoliday()) {
                holder.textViewDate.setTextColor(context.getResources().getColor(R.color.holo_red_light));
                holder.textViewDate.setTypeface(holder.textViewDate.getTypeface(), Typeface.BOLD);
                TextView holidayName = new TextView(context);
                holidayName.setText(day.getHoliday().getEnglishName());
                holidayName.setMaxLines(MAX_LINES);
                holidayName.setEllipsize(TextUtils.TruncateAt.END);
                holder.viewGroup.addView(holidayName);
            }
        }
        holder.viewGroup.setMinimumHeight(expectedMinimumHeight);

        return convertView;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public DayModel getItem(int i) {
        return days.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Calculates minimum cell height - to make them close to square.
     *
     * @return cell's minimum height, pixels.
     */
    private int calculateMinimumHeight() {
        int orientation = context.getResources().getConfiguration().orientation;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int padding = orientation == Configuration.ORIENTATION_PORTRAIT ?
                context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin) :
                context.getResources().getDimensionPixelSize(R.dimen.activity_land_horizontal_margin);
        int dpWidth = displayMetrics.widthPixels - (2 * padding);
        return dpWidth / DAYS_COUNT;
    }

    private static class ViewHolder {
        private TextView textViewDate;
        private ViewGroup viewGroup;
    }
}
