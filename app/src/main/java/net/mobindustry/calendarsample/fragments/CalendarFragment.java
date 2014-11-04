package net.mobindustry.calendarsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import net.mobindustry.calendarsample.R;
import net.mobindustry.calendarsample.adapters.SlidingMonthAdapter;
import net.mobindustry.calendarsample.model.HolidayModel;
import net.mobindustry.calendarsample.model.HolidayModelRaw;
import net.mobindustry.calendarsample.utils.ConnectivityCheck;

import org.apache.http.Header;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * The most outer fragment that holds the whole calendar.
 */
public class CalendarFragment extends Fragment {

    private static final String URL_HOLIDAYS = "http://www.kayaposoft.com/enrico/json/v1.0/?action=getPublicHolidaysForDateRange&fromDate=%1$s&toDate=%2$s&country=eng";
    private static final String DATE_OUTPUT_PATTERN = "dd-MM-yyyy";

    private static final int MONTH_LEFT_OFFSET = 5;
    private static final int MONTH_RIGHT_OFFSET = 24;

    /**
     * Pager that is responsible for swiping calendar months and recycling fragments.
     */
    private ViewPager pager;
    private SlidingMonthAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);

        PagerTabStrip tabStrip = (PagerTabStrip) rootView.findViewById(R.id.calendar_pager_tab_strip);
        tabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, getActivity().getResources().getDimension(R.dimen.calendar_tab_strip_textsize));

        setAdapter();

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (ConnectivityCheck.isInternetAvailable(getActivity())) {
            obtainHolidays();
        }
    }

    /**
     * This method will be called after loading holidays is done.
     * Updates our adapter with holidays
     *
     * @param holidayModels list of holidays
     */
    private void onHolidaysLoaded(List<HolidayModel> holidayModels) {
        adapter.setHolidays(holidayModels);
    }

    /**
     * This method assigns the adapter to our view pager
     */
    private void setAdapter() {
        adapter = new SlidingMonthAdapter(getActivity().getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(SlidingMonthAdapter.OFFSET, false);
    }

    /**
     * Queries for holidays, parses them to ArrayList of {@link net.mobindustry.calendarsample.model.HolidayModel} objects.
     * On success or fail - calls onHolidaysLoaded.
     */
    private void obtainHolidays() {
        // format next string with two dates (from - till), format = "dd-mm-yyyy"
        DateTimeFormatter mFormatter = DateTimeFormat.forPattern(DATE_OUTPUT_PATTERN);
        DateTime dateTimeFrom = DateTime.now().minusMonths(MONTH_LEFT_OFFSET).withDayOfMonth(1);
        DateTime dateTimeTill = DateTime.now().plusMonths(MONTH_RIGHT_OFFSET).withDayOfMonth(1);

        String queryUrl = String.format(URL_HOLIDAYS, dateTimeFrom.toString(mFormatter),
                dateTimeTill.toString(mFormatter));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(queryUrl, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                if (statusCode == HttpStatus.SC_OK) {
                    HolidayModelRaw[] holidaysRaw = new Gson().fromJson(byteArrayToString(response),
                            HolidayModelRaw[].class);

                    if (holidaysRaw != null) {
                        List<HolidayModel> holidaysList = new ArrayList<HolidayModel>();
                        for (HolidayModelRaw temp : holidaysRaw) {
                            holidaysList.add(new HolidayModel(temp));
                        }
                        onHolidaysLoaded(holidaysList);
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
            }

        });
    }

    public String byteArrayToString(byte[] array) {
        String mString;
        try {
            mString = new String(array, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            mString = null;
        }
        return mString;
    }

}
