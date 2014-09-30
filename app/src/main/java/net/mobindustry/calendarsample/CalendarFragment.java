package net.mobindustry.calendarsample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import net.mobindustry.calendarsample.model.HolidayModel;
import net.mobindustry.calendarsample.model.HolidayModelRaw;
import org.apache.http.Header;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * The most outer fragment that holds the whole calendar.
 */
public class CalendarFragment extends Fragment {

    private String LOG_TAG;
    private ViewPager pager;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_TAG = this.getClass().getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);

        PagerTabStrip tabStrip = (PagerTabStrip) rootView.findViewById(R.id.calendar_pager_tab_strip);
        tabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP,
            getActivity().getResources().getDimension(R.dimen.calendar_tab_strip_textsize));

        obtainHolidays();
        return rootView;
    }

    /**
     * This method will be called after loading holidays is done.
     * Continues calendar creation process: creates adapter and assigns it to pager.
     * @param holidayModels list of holidays
     */
    private void onHolidaysLoaded(ArrayList<HolidayModel> holidayModels) {
        SlidingMonthAdapter adapter = new SlidingMonthAdapter(getActivity().getSupportFragmentManager());
        adapter.setHolidays(holidayModels);
        pager.setAdapter(adapter);
        pager.setCurrentItem(SlidingMonthAdapter.OFFSET, false);
    }

    /**
     * Queries for holidays, parses them to ArrayList of HolidayModel objects.
     * On success or fail - calls onHolidaysLoaded.
     */
    private void obtainHolidays() {
        // format next string with two dates (from - till), format = "dd-mm-yyyy"
        final String holidayUrlStr = "http://www.kayaposoft.com/enrico/json/v1.0/?action=getPublicHolidaysForDateRange&fromDate=%1$s&toDate=%2$s&country=eng";
        DateTimeFormatter mFormatter = DateTimeFormat.forPattern("dd-MM-yyyy");
        DateTime dateTimeFrom = DateTime.now().minusMonths(5).withDayOfMonth(1);
        DateTime dateTimeTill = DateTime.now().plusMonths(24).withDayOfMonth(1);
        String queryUrl = String.format(holidayUrlStr,
            dateTimeFrom.toString(mFormatter), dateTimeTill.toString(mFormatter));

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(queryUrl, new AsyncHttpResponseHandler() {
            @Override
            public void onStart() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("Loading holidays data...");
                progressDialog.setCancelable(false);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                HolidayModelRaw[] holidaysRaw = new Gson().fromJson(byteArrayToString(response), HolidayModelRaw[].class);
                HolidayModel[] holidays = new HolidayModel[holidaysRaw.length];
                ArrayList<HolidayModel> holidaysList = new ArrayList<HolidayModel>();
                for(int i = 0; i < holidaysRaw.length; i++) {
                    holidaysList.add(new HolidayModel(holidaysRaw[i]));
                    holidays[i] = new HolidayModel(holidaysRaw[i]);
                }
                dismissProgressDialog();
                onHolidaysLoaded(holidaysList);
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // when error occured - no holidays obtained. To show calendar without holidays - add one, empty holiday.
                Log.e(LOG_TAG, "Obtaining data resulted in bad code: " + statusCode);
                dismissProgressDialog();
                ArrayList<HolidayModel> emptyHolidaysList = new ArrayList<HolidayModel>();
                emptyHolidaysList.add(new HolidayModel());
                onHolidaysLoaded(emptyHolidaysList);
            }
            @Override
            public void onRetry(int retryNo) {
                Log.e(LOG_TAG, "Obtaining data was restarted. retryNo = " + retryNo);
                if(progressDialog != null) {
                    progressDialog.setMessage("Loading holidays data problem. Retry #" + retryNo);
                }
            }
        });
    }

    public String byteArrayToString(byte[] array) {
        String mString;
        try {
            mString = new String(array, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "byteArrayToString: input has bad encoding - not a UTF-8");
            mString = "[]";
        }
        return mString;
    }

    private void dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
