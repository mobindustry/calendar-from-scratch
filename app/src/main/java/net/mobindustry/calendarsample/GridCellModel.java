package net.mobindustry.calendarsample;

import org.joda.time.DateTime;

/**
 * A class representing dataset for one day at month's grid.
 * Created by Den Drobiazko on 11.08.14.
 */
public class GridCellModel {

    private boolean isEmptyCell = false;
    private boolean isToday;
    private boolean isHoliday;

    /**
     * DateTime for this particular day. Is null by default, will remain null
     */
    private DateTime dateTime;

    public GridCellModel() {
        isEmptyCell = true;
        isToday = false;
        isHoliday = false;
        dateTime = null;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public GridCellModel setDateTime(DateTime dateTime) {
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

    public GridCellModel setEmptyCell(boolean isEmptyCell) {
        this.isEmptyCell = isEmptyCell;
        return this;
    }

    public boolean isToday() {
        return isToday;
    }

    public GridCellModel setToday(boolean isToday) {
        this.isToday = isToday;
        return this;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public GridCellModel setHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
        return this;
    }
}
