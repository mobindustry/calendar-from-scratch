package net.mobindustry.calendarsample.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Toast;

import net.mobindustry.calendarsample.R;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);

        findViewById(R.id.standardCalendarButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //supported only from API 11
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    showCalendarActivity(CalendarActivity.CalendarType.STANDARD);
                } else {
                    Toast.makeText(MainActivity.this, "This is unsupported API Level!", Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.customCalendarButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendarActivity(CalendarActivity.CalendarType.CUSTOM_MOBINDUSTRY);
            }
        });

        findViewById(R.id.anotherCalendarButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CaldroidSampleActivity.class));
            }
        });

    }

    private void showCalendarActivity(CalendarActivity.CalendarType calendarType) {
        Intent intentCalendarActivity = new Intent(MainActivity.this, CalendarActivity.class);
        intentCalendarActivity.putExtra(CalendarActivity.CALENDAR_TYPE_PARAM, calendarType);
        startActivity(intentCalendarActivity);
    }
}
