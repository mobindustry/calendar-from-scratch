package net.mobindustry.calendarsample.activities;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import net.mobindustry.calendarsample.R;
import net.mobindustry.calendarsample.fragments.StandardCalendarFragment;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity_layout);



        Button standardCalendarButton = (Button) findViewById(R.id.standardCalendarButton);
        standardCalendarButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            //supported only from API 11
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
              showCalendarActivity(CalendarActivity.CalendarType.STANDARD);
            }
            else{
              Toast.makeText(MainActivity.this, "This is unsupported API Level!",Toast.LENGTH_LONG).show();
            }
          }
        });

        Button customCalendarButton = (Button) findViewById(R.id.customCalendarButton);
        customCalendarButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            showCalendarActivity(CalendarActivity.CalendarType.CUSTOM_MOBINDUSTRY);
          }
        });

    }

  private void showCalendarActivity(CalendarActivity.CalendarType calendarType){
    Intent intentCalendarActivity  = new Intent(MainActivity.this, CalendarActivity.class);
    intentCalendarActivity.putExtra(CalendarActivity.CALENDAR_TYPE_PARAM, calendarType);
    startActivity(intentCalendarActivity);

  }
}
