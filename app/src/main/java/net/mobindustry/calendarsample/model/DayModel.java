package net.mobindustry.calendarsample.model;

import org.joda.time.DateTime;

/**
 * A class representing set of data for one day at month's grid.
 */
public class DayModel {

    private boolean isEmptyCell = false;
    private boolean isToday;
    private boolean isHoliday;
    private HolidayModel holiday;

    /**
     * DateTime for this particular day. Is null by default, will remain null if now explicitly set by setter.
     */
    private DateTime dateTime;

    public DayModel() {
        isEmptyCell = true;
        isToday = false;
        isHoliday = false;
        dateTime = null;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public DayModel setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
        this.isEmptyCell = false;
        DateTime now = DateTime.now();
        if(dateTime.getDayOfMonth() == now.getDayOfMonth() &&
            dateTime.getMonthOfYear() == now.getMonthOfYear()
            && dateTime.getYear() == now.getYear()) {
            isToday = true;
        }
        return this;
    }

    public int getDayNumber() {
        if(!isEmptyCell()) {
            return dateTime.getDayOfMonth();
        } else {
            return -1;
        }
    }

    public boolean isEmptyCell() {
        return isEmptyCell;
    }

    public DayModel setEmptyCell(boolean isEmptyCell) {
        this.isEmptyCell = isEmptyCell;
        return this;
    }

    public boolean isToday() {
        return isToday;
    }

    public DayModel setToday(boolean isToday) {
        this.isToday = isToday;
        return this;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public DayModel setHoliday(HolidayModel holiday) {
        this.isHoliday = true;
        this.holiday = holiday;
        return this;
    }

    public HolidayModel getHoliday() {
        return holiday;
    }
}
