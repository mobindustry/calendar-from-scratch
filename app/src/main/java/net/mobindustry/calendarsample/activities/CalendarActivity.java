package net.mobindustry.calendarsample.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import net.mobindustry.calendarsample.R;
import net.mobindustry.calendarsample.calendar_from_scratch.CalendarFragment;
import net.mobindustry.calendarsample.calendar_from_scratch.CalendarListener;
import net.mobindustry.calendarsample.calendar_from_scratch.model.DayModel;

import org.joda.time.DateTime;

import java.util.Calendar;

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
                case CUSTOM_MOBINDUSTRY:
                    CalendarFragment calendarFragment = new CalendarFragment();

                    calendarFragment.setCalendarListener(listener);

                    Bundle args = new Bundle();
                    args.putBoolean(CalendarFragment.HOLIDAYS_ENABLED, true);
                    calendarFragment.setArguments(args);

                    getSupportFragmentManager().beginTransaction().add(R.id.container, calendarFragment, CalendarFragment.TAG).commit();
                    break;
                default:
            }
        } else {
            CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager()
                    .findFragmentByTag(CalendarFragment.TAG);
            calendarFragment.setCalendarListener(listener);
        }
    }

    private CalendarListener listener = new CalendarListener() {
        @Override
        public void onDateSelected(DayModel dayModel) {
            showDialogOnCellTouch(dayModel);
        }
    };

    /**
     * Build and shows dialog on cell touch.
     *
     * @param touchedCell GridCellModel object for that day.
     */
    private void showDialogOnCellTouch(DayModel touchedCell) {
        if (!touchedCell.isEmptyCell()) {
            DateTime cellDateTime = touchedCell.getDateTime();
            String title, message;
            title = cellDateTime.toString("dd.MM.yyyy");
            message = cellDateTime.toString("dd MMMM yyyy");
            if (touchedCell.isToday()) {
                message += "\nToday";
            }
            if (touchedCell.isHoliday()) {
                message += "\n" + touchedCell.getHoliday().getEnglishName();
            }

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle(title)
                    .setMessage(message)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        } else {
            Toast.makeText(this, "Empty cell", Toast.LENGTH_SHORT).show();
        }
    }
}
