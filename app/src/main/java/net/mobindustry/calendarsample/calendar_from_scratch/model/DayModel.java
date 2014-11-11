package net.mobindustry.calendarsample.calendar_from_scratch.model;

import org.joda.time.DateTime;

/**
 * A class representing set of data for one day at month's grid.
 */
public class DayModel {

    private final static int SATURDAY = 6;
    private final static int SUNDAY = 7;

    private boolean isToday;
    private boolean isHoliday;
    private boolean isPreviousOrLast = false;
    private HolidayModel holiday;

    /**
     * DateTime for this particular day. Is null by default, will remain null if now explicitly set by setter.
     */
    private DateTime dateTime;

    public DayModel() {
        isToday = false;
        isHoliday = false;
        dateTime = null;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public DayModel setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        DateTime now = DateTime.now();
        if (dateTime.getDayOfMonth() == now.getDayOfMonth() &&
                dateTime.getMonthOfYear() == now.getMonthOfYear()
                && dateTime.getYear() == now.getYear()) {
            isToday = true;
        }
        return this;
    }

    public int getDayNumber() {
        return dateTime.getDayOfMonth();
    }

    public boolean isToday() {
        return isToday;
    }

    public DayModel setToday(boolean isToday) {
        this.isToday = isToday;
        return this;
    }

    public boolean isPreviousOrLast() {
        return isPreviousOrLast;
    }

    public void setPreviousOrLast(boolean isPreviousOrLast) {
        this.isPreviousOrLast = isPreviousOrLast;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public DayModel setHoliday(HolidayModel holiday) {
        this.isHoliday = true;
        this.holiday = holiday;
        return this;
    }

    public boolean isWeekend() {
        return dateTime.getDayOfWeek() == SATURDAY || dateTime.getDayOfWeek() == SUNDAY;

    }

    public HolidayModel getHoliday() {
        return holiday;
    }
}
