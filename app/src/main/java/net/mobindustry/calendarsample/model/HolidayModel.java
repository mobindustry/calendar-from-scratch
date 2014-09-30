package net.mobindustry.calendarsample.model;

import org.joda.time.DateTime;

/**
 * Data model for holiday essence.
 */
public class HolidayModel {

    private DateTime date;
    private String englishName;

    public HolidayModel(HolidayModelRaw raw) {
        HolidayModelRaw.HolidayDateModel holidayDate = raw.getDate();
        date = new DateTime(holidayDate.getYear(), holidayDate.getMonth(), holidayDate.getDay(), 0, 0, 0);
        englishName = raw.getEnglishName();
    }

    /**
     * Constructor to have a non-empty holidays array in case if holidays loading failed.
     */
    public HolidayModel() {
        date = DateTime.now().minusYears(100);
        englishName = "";
    }

    public DateTime getDate() {
        return date;
    }

    public String getEnglishName() {
        return englishName;
    }
}
