package net.mobindustry.calendarsample;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Locale;

/**
 * Adapter to create, switch and recycle properly "Month" fragments containing grid with days.
 * Created by Den Drobiazko on 11.08.14.
 */
public class SlidingMonthAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 30;
    public static final int OFFSET = 5;

    public SlidingMonthAdapter(FragmentManager mFragmentManager) {
        super(mFragmentManager);
    }

    @Override
    public Fragment getItem(int i) {
        return createFragment(getPositionWithOffset(i));
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return calculateDateTime(getPositionWithOffset(position)).toString("MMMM YYYY", Locale.getDefault());
    }

    private SlidingMonthFragment createFragment(int month) {
        SlidingMonthFragment mFragment = new SlidingMonthFragment();
        mFragment.setDateTime(calculateDateTime(month));
        mFragment.setWeekdayNames(buildWeekdayNames());
        return mFragment;
    }

    private int getPositionWithOffset(int adapterPosition) {
        return adapterPosition - OFFSET;
    }

    private DateTime calculateDateTime(int positionWithOffset) {
        DateTime mDateTime = DateTime.now();
        if(positionWithOffset < 0) {
            mDateTime = mDateTime.minusMonths(-1 * positionWithOffset);
        } else if (positionWithOffset > 0) {
            mDateTime = mDateTime.plusMonths(positionWithOffset);
        }
        return mDateTime;
    }

    private String[] buildWeekdayNames() {
        String[] weekNames = new String[7];
        for(int i = 0; i < weekNames.length; i++) {
            DateTime dt = new DateTime().withDayOfWeek(i + 1);
            DateTimeFormatter fmt = DateTimeFormat.forPattern("EEE");
            weekNames[i] = fmt.print(dt);
        }

        return weekNames;
    }
}
