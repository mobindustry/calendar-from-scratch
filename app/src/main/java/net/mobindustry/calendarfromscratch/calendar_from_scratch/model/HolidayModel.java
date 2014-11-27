package net.mobindustry.calendarfromscratch.calendar_from_scratch.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.joda.time.DateTime;

/**
 * Data model for holiday essence.
 */
public class HolidayModel implements Parcelable {

    private final DateTime date;
    private final String englishName;

    public HolidayModel(HolidayModelRaw raw) {
        HolidayModelRaw.HolidayDateModel holidayDate = raw.getDate();
        date = new DateTime(holidayDate.getYear(), holidayDate.getMonth(), holidayDate.getDay(), 0, 0, 0);
        englishName = raw.getEnglishName();
    }

    // конструктор, считывающий данные из Parcel
    private HolidayModel(Parcel parcel) {
        englishName = parcel.readString();
        date = new DateTime(parcel.readLong());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(englishName);
        parcel.writeLong(date.getMillis());
    }

    public static final Parcelable.Creator<HolidayModel> CREATOR = new Parcelable.Creator<HolidayModel>() {

        public HolidayModel createFromParcel(Parcel in) {
            return new HolidayModel(in);
        }

        public HolidayModel[] newArray(int size) {
            return new HolidayModel[size];
        }
    };


}
