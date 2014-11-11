package net.mobindustry.calendarsample.calendar_from_scratch.model;

import com.google.gson.annotations.SerializedName;

/**
 * Data model for holiday essence that represents holiday as it is obtained from json.
 */
public class HolidayModelRaw {

    @SerializedName("date")
    private HolidayDateModel date;

    @SerializedName("englishName")
    private String englishName;

    public HolidayDateModel getDate() {
        return date;
    }

    public String getEnglishName() {
        return englishName;
    }

    public class HolidayDateModel {

        @SerializedName("day")
        private int day;

        @SerializedName("month")
        private int month;

        @SerializedName("year")
        private int year;

        public int getDay() {
            return day;
        }

        public int getMonth() {
            return month;
        }

        public int getYear() {
            return year;
        }
    }
}
