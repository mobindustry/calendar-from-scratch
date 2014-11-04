package net.mobindustry.calendarsample.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import net.mobindustry.calendarsample.R;
import net.mobindustry.calendarsample.fragments.CalendarFragment;
import net.mobindustry.calendarsample.fragments.StandardCalendarFragment;

public class CalendarActivity extends FragmentActivity {
    public static final String CALENDAR_TYPE_PARAM = "CALENDAR_TYPE_PARAM";

    public static enum CalendarType {
        STANDARD,
        CUSTOM_MOBINDUSTRY
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_activity_layout);
        if (savedInstanceState == null) {
            CalendarType calendarType = (CalendarType) getIntent().getSerializableExtra(CALENDAR_TYPE_PARAM);
            switch (calendarType) {
                case STANDARD:
                    getSupportFragmentManager().beginTransaction().add(R.id.container, new StandardCalendarFragment()).commit();
                    break;
                case CUSTOM_MOBINDUSTRY:
                    getSupportFragmentManager().beginTransaction().add(R.id.container, new CalendarFragment()).commit();
                    break;
                default:
            }

        }
    }
}
