package net.mobindustry.calendarfromscratch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

import net.mobindustry.calendarsample.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        findViewById(R.id.customCalendarButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarActivity(CalendarActivity.CalendarType.CUSTOM_MOBINDUSTRY);
            }
        });

    }

    private void showCalendarActivity(CalendarActivity.CalendarType calendarType) {
        Intent intentCalendarActivity = new Intent(MainActivity.this, CalendarActivity.class);
        intentCalendarActivity.putExtra(CalendarActivity.CALENDAR_TYPE_PARAM, calendarType);
        startActivity(intentCalendarActivity);
    }
}
