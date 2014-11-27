package net.mobindustry.calendarfromscratch.calendar_from_scratch;

import net.mobindustry.calendarfromscratch.calendar_from_scratch.model.DayModel;

/**
 * Created by Edward on 11.11.14.
 * listens calendar events
 */
public interface CalendarListener {

    public void onDateSelected(DayModel dayModel);
}
