package net.mobindustry.calendarfromscratch.calendar_from_scratch;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import net.mobindustry.calendarfromscratch.calendar_from_scratch.model.HolidayModel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Adapter to create, switch and recycle properly "Month" fragments containing grid with days.
 */
public class CalendarAdapter extends FragmentStatePagerAdapter {

    private static final int DAYS_OF_WEEK_COUNT = 7;
    private static final String DATE_HEADER_FORMAT = "MMMM YYYY";

    private List<HolidayModel> holidays;

    /**
     * array to store instantiated fragments in order to restore them and update
     */
    private SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    /**
     * Total count of months
     */
    private int mPagesCount;

    /**
     * Count of months that can be scrolled back after opening calendar on default month.
     * This gives ability to list calendar back.
     */
    private int mOffset;

    private CalendarListener calendarListener;

    public CalendarAdapter(FragmentManager mFragmentManager, int pagesCount, int offset) {
        super(mFragmentManager);
        this.mPagesCount = pagesCount;
        this.mOffset = offset;
    }

    public void setCalendarListener(CalendarListener calendarListener) {
        this.calendarListener = calendarListener;
    }

    @Override
    public Fragment getItem(int i) {
        return createFragment(getPositionWithOffset(i));
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    @Override
    public int getCount() {
        return mPagesCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // this will create a formatted title for requested month
        return calculateDateTime(getPositionWithOffset(position)).toString(DATE_HEADER_FORMAT, Locale.getDefault());
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    private MonthFragment createFragment(int month) {
        MonthFragment mFragment = new MonthFragment();
        mFragment.setDateTime(calculateDateTime(month));
        mFragment.setWeekdayNames(generateWeekdayNames());
        mFragment.setCalendarListener(calendarListener);
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
        return adapterPosition - mOffset;
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
    private String[] generateWeekdayNames() {
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

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        for (int i = 0; i < registeredFragments.size(); i++) {
            // get the object by the key.
            Fragment f = registeredFragments.valueAt(i);
            //try to update current fragments
            if (f instanceof UpdateableFragment) {
                ((UpdateableFragment) f).update(prepareHolidaysForMonth(((UpdateableFragment) f).getDateTime()));
            }
        }
    }

    /**
     * Looks through all holidays array and returns only those that are for given month.
     *
     * @param currentDateTime DateTime from given month.
     * @return ArrayList of HolidayModel objects for that month.
     */
    private ArrayList<HolidayModel> prepareHolidaysForMonth(DateTime currentDateTime) {
        ArrayList<HolidayModel> monthHolidays = new ArrayList<HolidayModel>();
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
