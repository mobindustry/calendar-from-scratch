package net.mobindustry.calendarsample.calendar_from_scratch;

import net.mobindustry.calendarsample.calendar_from_scratch.model.HolidayModel;
import org.joda.time.DateTime;

import java.util.ArrayList;

public interface UpdateableFragment {
    public void update(ArrayList<HolidayModel> holidays);

    public DateTime getDateTime();
}