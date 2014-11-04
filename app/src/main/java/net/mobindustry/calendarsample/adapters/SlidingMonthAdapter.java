package net.mobindustry.calendarsample.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import net.mobindustry.calendarsample.fragments.SlidingMonthFragment;
import net.mobindustry.calendarsample.fragments.UpdateableFragment;
import net.mobindustry.calendarsample.model.HolidayModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter to create, switch and recycle properly "Month" fragments containing grid with days.
 */
public class SlidingMonthAdapter extends FragmentStatePagerAdapter {

    /**
     * Total number of available pages(months).<br>Increasing does not result in significant bigger ram usage
     * since only 3 fragments are in memory at a time.
     */
    private static final int NUM_PAGES = 30;
    private static final int DAYS_OF_WEEK_COUNT = 7;

    /**
     * Number of months that can be scrolled back after opening calendar on default month.
     * This gives ability to list calendar back.
     */
    public static final int OFFSET = 5;

    private static final String DATE_HEADER_FORMAT = "MMMM YYYY";

    private List<HolidayModel> holidays;

    public SlidingMonthAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        return createFragment(getPositionWithOffset(i));
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof UpdateableFragment) {
            UpdateableFragment updateableFragment = (UpdateableFragment) object;
            updateableFragment.update(prepareHolidaysForMonth(updateableFragment.getDateTime()));
        }
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // this will create a formatted title for requested month
        return calculateDateTime(getPositionWithOffset(position)).toString(DATE_HEADER_FORMAT, Locale.getDefault());
    }

    private SlidingMonthFragment createFragment(int month) {
        SlidingMonthFragment mFragment = new SlidingMonthFragment();
        mFragment.setDateTime(calculateDateTime(month));
        mFragment.setWeekdayNames(buildWeekdayNames());
        mFragment.setMonthHolidays(prepareHolidaysForMonth(mFragment.getDateTime()));
        return mFragment;
    }

    /**
     * Returns position calculated considering given offset. To be used everywhere instead of adapter's position.
     *
     * @param adapterPosition position given by adapter.
     * @return position with offset.
     */
    private int getPositionWithOffset(int adapterPosition) {
        return adapterPosition - OFFSET;
    }

    /**
     * @param positionWithOffset position with offset.
     * @return DateTime object for that month.
     */
    private DateTime calculateDateTime(int positionWithOffset) {
        DateTime mDateTime = DateTime.now();
        if (positionWithOffset < 0) {
            mDateTime = mDateTime.minusMonths(-1 * positionWithOffset);
        } else if (positionWithOffset > 0) {
            mDateTime = mDateTime.plusMonths(positionWithOffset);
        }
        return mDateTime;
    }

    /**
     * Creates a String array with weekday names for default device's locale.
     *
     * @return array of weekday names
     */
    private String[] buildWeekdayNames() {
        String[] weekNames = new String[DAYS_OF_WEEK_COUNT];
        for (int i = 0; i < weekNames.length; i++) {
            DateTime dt = new DateTime().withDayOfWeek(i + 1);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE");
            weekNames[i] = fmt.print(dt);
        }

        return weekNames;
    }

    public void setHolidays(List<HolidayModel> holidays) {
        this.holidays = holidays;
        notifyDataSetChanged();
    }

    /**
     * Looks through all holidays array and returns only those that are for given month.
     *
     * @param currentDateTime DateTime from given month.
     * @return ArrayList of HolidayModel objects for that month.
     */
    private List<HolidayModel> prepareHolidaysForMonth(DateTime currentDateTime) {
        List<HolidayModel> monthHolidays = new ArrayList<HolidayModel>();
        if (holidays != null) {
            int currentYear, currentMonth;
            currentYear = currentDateTime.getYear();
            currentMonth = currentDateTime.getMonthOfYear();
            for (HolidayModel mHolidayModel : holidays) {
                if (mHolidayModel.getDate().getYear() == currentYear
                        && mHolidayModel.getDate().getMonthOfYear() == currentMonth) {
                    monthHolidays.add(mHolidayModel);
                }
            }
        }
        return monthHolidays;
    }
}
