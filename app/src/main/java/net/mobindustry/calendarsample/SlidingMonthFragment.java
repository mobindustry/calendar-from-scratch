package net.mobindustry.calendarsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import net.mobindustry.calendarsample.model.GridCellModel;
import net.mobindustry.calendarsample.model.HolidayModel;
import org.joda.time.DateTime;

import java.util.ArrayList;

/**
 * Fragment representing one month. To be managed by SlidingMonthAdapter.
 * Created by Den Drobiazko on 11.08.14.
 */
public class SlidingMonthFragment extends Fragment {

    public String LOG_TAG;
    private DateTime dateTime;
    private String[] weekdayNames;
    private ArrayList<HolidayModel> monthHolidays;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_TAG = this.getClass().getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.month_fragment, container, false);

        GridView weekdayGrid = (GridView) rootView.findViewById(R.id.weekday_grid);
        weekdayGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.weekday_tv, weekdayNames));

        GridView monthGrid = (GridView) rootView.findViewById(R.id.month_grid);
        MonthGridAdapter adapter = new MonthGridAdapter(getActivity(), initiateCellArray(dateTime));
        monthGrid.setAdapter(adapter);

        return rootView;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setMonthHolidays(ArrayList<HolidayModel> monthHolidays) {
        this.monthHolidays = monthHolidays;
    }

    /**
     * Based on this month's DateTime object calculates array of GridCellModel objects, that will be used to build grid.
     * @param dateTime
     */
    private ArrayList<GridCellModel> initiateCellArray(DateTime dateTime) {
        int emptyCellsAtStart, daysInMonth, emptyCellsInTheEnd;
        ArrayList<GridCellModel> days = new ArrayList<GridCellModel>();

        DateTime monthDateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
            0, 0, 0);
        emptyCellsAtStart = monthDateTime.withDayOfMonth(1).getDayOfWeek() - 1;
        daysInMonth = monthDateTime.dayOfMonth().getMaximumValue();
        emptyCellsInTheEnd = 7 - monthDateTime.withDayOfMonth(daysInMonth).getDayOfWeek();
        for(int i = 0; i < emptyCellsAtStart; i++) {
            days.add(new GridCellModel());
        }
        for(int i = 0; i < daysInMonth; i++) {
            GridCellModel mModel = new GridCellModel().setDateTime(dateTime.withDayOfMonth(i + 1));
            if(!monthHolidays.isEmpty()) {
                for(HolidayModel mHolidayModel : monthHolidays) {
                    if(mHolidayModel.getDate().withTimeAtStartOfDay()
                        .equals(mModel.getDateTime().withTimeAtStartOfDay())) {
                        mModel.setHoliday(mHolidayModel);
                    }
                }
            }
            days.add(mModel);
        }
        for(int i = 0; i < emptyCellsInTheEnd; i++) {
            days.add(new GridCellModel());
        }
        return days;
    }

    public void setWeekdayNames(String[] weekdayNames) {
        this.weekdayNames = weekdayNames;
    }
}
