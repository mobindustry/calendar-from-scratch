package net.mobindustry.calendarsample.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;
import net.mobindustry.calendarsample.R;

public class StandardCalendarFragment extends Fragment {

  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

    View view = inflater.inflate(R.layout.standard_calendar_fragment,null,false);

    CalendarView calendar = (CalendarView) view.findViewById(R.id.calendar);

    // sets whether to show the week number.
    calendar.setShowWeekNumber(false);

    // sets the first day of week according to Calendar.
    // here we set Monday as the first day of the Calendar
    calendar.setFirstDayOfWeek(2);

    //The background color for the selected week.
    calendar.setSelectedWeekBackgroundColor(getResources().getColor(android.R.color.holo_green_light));

    //sets the color for the dates of an unfocused month.
    calendar.setUnfocusedMonthDateColor(getResources().getColor(android.R.color.transparent));

    //sets the color for the separator line between weeks.
    calendar.setWeekSeparatorLineColor(getResources().getColor(android.R.color.transparent));

    //sets the color for the vertical bar shown at the beginning and at the end of the selected date.
    calendar.setSelectedDateVerticalBar(android.R.color.holo_green_dark);

    //sets the listener to be notified upon selected date change.
    calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
      //show the selected date as a toast
      @Override
      public void onSelectedDayChange(CalendarView view, int year, int month, int day) {
        Toast.makeText(getActivity(), day + "/" + month + "/" + year, Toast.LENGTH_LONG).show();
      }
    });
    return view;
  }

}
