package net.mobindustry.calendarsample.fragments;

import net.mobindustry.calendarsample.model.HolidayModel;

import org.joda.time.DateTime;

import java.util.List;

public interface UpdateableFragment {
    public void update(List<HolidayModel> holidays);

    public DateTime getDateTime();
}