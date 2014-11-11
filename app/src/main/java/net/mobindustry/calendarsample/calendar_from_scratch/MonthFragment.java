package net.mobindustry.calendarsample.calendar_from_scratch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import net.mobindustry.calendarsample.R;
import net.mobindustry.calendarsample.calendar_from_scratch.model.DayModel;
import net.mobindustry.calendarsample.calendar_from_scratch.model.HolidayModel;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment representing one month. To be managed by SlidingMonthAdapter inside ViewPager.
 */
public class MonthFragment extends Fragment implements UpdateableFragment {

    private static final String CURRENT_TIME = "TIME";
    private static final String WEEKDAYS = "WEEKDAYS";
    private static final String HOLIDAYS = "HOLIDAYS";

    /**
     * {@link org.joda.time.DateTime} object for current month
     */
    private DateTime dateTime;
    private String[] weekdayNames;

    private ArrayList<HolidayModel> monthHolidays;
    private List<DayModel> cellModels;

    private MonthAdapter adapter;
    private CalendarListener calendarListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.month_fragment, container, false);

        if (savedInstanceState != null) {
            dateTime = new DateTime(savedInstanceState.getLong(CURRENT_TIME));
            weekdayNames = savedInstanceState.getStringArray(WEEKDAYS);
            monthHolidays = savedInstanceState.getParcelableArrayList(HOLIDAYS);
        }

        cellModels = initiateCellArray(dateTime);

        // show weekday names above calendar grid
        GridView weekdayGrid = (GridView) rootView.findViewById(R.id.weekday_grid);
        weekdayGrid.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.weekday_tv, weekdayNames));

        GridView monthGrid = (GridView) rootView.findViewById(R.id.month_grid);
        adapter = new MonthAdapter(getActivity(), cellModels);
        monthGrid.setAdapter(adapter);

        monthGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (calendarListener != null) {
                    calendarListener.onDateSelected(cellModels.get(position));
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            CalendarFragment calendarFragment = (CalendarFragment) getFragmentManager().findFragmentByTag(CalendarFragment.TAG);
            this.calendarListener = calendarFragment.getCalendarListener();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(CURRENT_TIME, dateTime.getMillis());
        outState.putStringArray(WEEKDAYS, weekdayNames);
        outState.putParcelableArrayList(HOLIDAYS, monthHolidays);
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    @Override
    public DateTime getDateTime() {
        return dateTime;
    }

    public void setMonthHolidays(ArrayList<HolidayModel> monthHolidays) {
        this.monthHolidays = monthHolidays;
    }

    @Override
    public void update(ArrayList<HolidayModel> monthHolidays) {
        this.monthHolidays = monthHolidays;
        for (HolidayModel holidayModel : monthHolidays) {
            for (DayModel gridCellModel : cellModels) {
                if (holidayModel.getDate().withTimeAtStartOfDay().
                        equals(gridCellModel.getDateTime().withTimeAtStartOfDay())) {
                    gridCellModel.setHoliday(holidayModel);
                    break;
                }
            }
        }
        adapter.notifyDataSetChanged();
    }

    /**
     * Based on this month's DateTime object calculates array of GridCellModel objects, that will be used to build grid.
     *
     * @param dateTime this month's DateTime
     */
    private List<DayModel> initiateCellArray(DateTime dateTime) {
        int emptyCellsAtStart, daysInMonth, emptyCellsInTheEnd;
        List<DayModel> days = new ArrayList<DayModel>();

        DateTime monthDateTime = new DateTime(dateTime.getYear(), dateTime.getMonthOfYear(), dateTime.getDayOfMonth(),
                0, 0, 0);
        // how many "empty" days there are in the first week that pertain to previous month
        emptyCellsAtStart = monthDateTime.withDayOfMonth(1).getDayOfWeek() - 1;

        daysInMonth = monthDateTime.dayOfMonth().getMaximumValue();

        // how many "empty" days there are after last month's day in the last week that pertain to next month
        emptyCellsInTheEnd = 7 - monthDateTime.withDayOfMonth(daysInMonth).getDayOfWeek();

        for (int i = 0; i < emptyCellsAtStart; i++) {
            // for every "empty" cell create empty cell model
            days.add(new DayModel());
        }

        for (int i = 0; i < daysInMonth; i++) {
            // create cell for day of month and populate it with holiday if present
            DayModel mModel = new DayModel().setDateTime(dateTime.withDayOfMonth(i + 1));

            if (null != monthHolidays && !monthHolidays.isEmpty()) {
                for (HolidayModel mHolidayModel : monthHolidays) {
                    if (mHolidayModel.getDate().withTimeAtStartOfDay()
                            .equals(mModel.getDateTime().withTimeAtStartOfDay())) {
                        mModel.setHoliday(mHolidayModel);
                        break;
                    }
                }
            }

            days.add(mModel);
        }

        for (int i = 0; i < emptyCellsInTheEnd; i++) {
            // for every "empty" cell create empty cell model
            days.add(new DayModel());
        }

        return days;
    }

    public void setWeekdayNames(String[] weekdayNames) {
        this.weekdayNames = weekdayNames;
    }


}
