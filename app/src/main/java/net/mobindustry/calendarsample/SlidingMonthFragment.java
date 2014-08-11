package net.mobindustry.calendarsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import org.joda.time.DateTime;

/**
 * Fragment representing one month. To be managed by SlidingMonthAdapter.
 * Created by Den Drobiazko on 11.08.14.
 */
public class SlidingMonthFragment extends Fragment {

    public String LOG_TAG;
    private DateTime dateTime;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LOG_TAG = this.getClass().getSimpleName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.month_fragment, container, false);

        GridView grid = (GridView) rootView.findViewById(R.id.month_grid);
        MonthGridAdapter adapter = new MonthGridAdapter(getActivity());
        adapter.setDateTime(dateTime);
        grid.setAdapter(adapter);

        return rootView;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }
}
