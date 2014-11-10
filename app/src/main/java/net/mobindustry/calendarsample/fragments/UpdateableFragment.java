package net.mobindustry.calendarsample.fragments;

import net.mobindustry.calendarsample.model.HolidayModel;

import org.joda.time.DateTime;

import java.util.ArrayList;

public interface UpdateableFragment {
    public void update(ArrayList<HolidayModel> holidays);

    public DateTime getDateTime();
}