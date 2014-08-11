package net.mobindustry.calendarsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Den Drobiazko on 11.08.14.
 */
public class CalendarFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        ViewPager pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);

        SlidingMonthAdapter adapter = new SlidingMonthAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(SlidingMonthAdapter.OFFSET, false);
        PagerTabStrip tabStrip = (PagerTabStrip) rootView.findViewById(R.id.calendar_pager_tab_strip);
        tabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,
            getActivity().getResources().getDimension(R.dimen.calendar_tab_strip_textsize));
        return rootView;
    }
}
