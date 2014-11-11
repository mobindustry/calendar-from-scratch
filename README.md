Sometimes things develop into a situation where you need some kind of a calendar for your application. I'm not talking about the app which IS a calendar, with one and only goal to show weeks/months, remember events for particular days and fire notifications. This is about your application working with some data, which - on one of the sreens - you might want to represent as set of events on month's grid.

As long as calendar is a quite complex element, you might face performance issues while using some of ready-for-use libraries - just wait untill you put quite a lot of elements on it and it won't be so smooth as user want's it. Also, library projects always offer lower lever of customization compared to calendar that was created from scratch for your app specifically.

In this article I will create a simple yet highly-customizable calendar, from scratch. I will show sample application's structure, explain main elements that you need to create to make everything work and highlight places in code, which you want to enhance for further developing.

Note that I did not intend to create a calendar that can show you months from now and till the ends of time. Let's be honest - you hardly ever swipe to some date like 2 years from now in you calendar app. Or imagine that you are creating an app that shows your son's school timetable - do you need to see what will the schedule be in 5 years? I guess no.

For me - throwing away this super-flexibility was a relief, because it made my code much simpler.
Now to the point!

<img src="https://raw.githubusercontent.com/mobindustry/calendar-from-scratch/master/device-2014-11-11-164456.png" width="270" style="margin-left:10px;">

## How to use

First of all, we expect our app to contain not just this calendar, so we have it wrapped to CalendarFragment, that is shown in MainActivity and can be replaced by any other screen:

```java
  CalendarFragment calendarFragment = new CalendarFragment();
  getSupportFragmentManager().beginTransaction().add(R.id.container, calendarFragment, CalendarFragment.TAG).commit();
```
Also, you could add some arguments to the fragment, so that to customize it a little. For example, we have an argument, that defines wether we will load holidays or not.

```java
  Bundle args = new Bundle();
  args.putBoolean(CalendarFragment.HOLIDAYS_ENABLED, true);
  calendarFragment.setArguments(args);
```
Of course, you could set listener to get click events from calendar.

```java
    private CalendarListener listener = new CalendarListener() {
        @Override
        public void onDateSelected(DayModel dayModel) {
            showDialogOnCellTouch(dayModel);
        }
    };
    
    
    
    calendarFragment.setCalendarListener(listener);
```


## Structure

First of all, we expect our app to contain not just this calendar, so we have it wrapped to CalendarFragment, that is shown in MainActivity and can be replaced by any other screen:

```java
public class CalendarFragment extends Fragment {

     /**
     * Pager that is responsible for swiping calendar months and recycling fragments.
     */
    private ViewPager pager;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar, container, false);

        pager = (ViewPager) rootView.findViewById(R.id.calendar_pager);

        PagerTabStrip tabStrip = (PagerTabStrip) rootView.findViewById(R.id.calendar_pager_tab_strip);
        tabStrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, getActivity().getResources().getDimension(R.dimen.calendar_tab_strip_textsize));

        setAdapter();

        return rootView;
    }

    
}
```

As you can see it has only one element - ViewPager, that will switch and recycle views representing months.
ViewPager needs an adapter, and that's where one of main elements steps in - FragmentStatePagerAdapter. It's a native Android element, it's easy to use and it shows great performance.

I ended up extending this FragmentStatePagerAdapter into a CalendarAdapter class. Here some things that you should note:

FragmentStatePagerAdapter is not an infinite pager. It needs some fixed int as element's count. For me - 30 was enough, so I defined

`private static final int DEFAULT_PAGES_COUNT = 30;`

You can put any number that meets your needs and it won't issue huge memory usage - that's the beauty of FragmentStatePagerAdapter. By default it holds only 3 fragments at a time - current, one on the right and one on the left.

One more requirement that I faced during developing calendar - user expects to see current month when he opens calendar but he also wants to be able to scroll back for a couple of months or even a year. To reach that, I created

`public static final int DEFAULT_OFFSET = 5;`

variable. Setting it to 5 means that by default I will have 5 screens available on the left after opening calendar. To make everything work correctly I created method

```java
private int getPositionWithOffset(int adapterPosition) {
    return adapterPosition - mOffset;
}
```

and used it whereever I needed return current position:

```java
@Override
public Fragment getItem(int i) {
    return createFragment(getPositionWithOffset(i));
}

@Override
public CharSequence getPageTitle(int position) {
    return calculateDateTime(getPositionWithOffset(position)).toString(DATE_HEADER_FORMAT, Locale.getDefault());
}
```

Also - when creating a new month grid fragment - I wanted to put correct month number to it. This method gets right month based on position with offset:

```java
private DateTime calculateDateTime(int positionWithOffset) {
    DateTime mDateTime = DateTime.now();
    if(positionWithOffset < 0) {
    mDateTime = mDateTime.minusMonths(-1 * positionWithOffset);
    } else if (positionWithOffset > 0) {
        mDateTime = mDateTime.plusMonths(positionWithOffset);
    }
    return mDateTime;
}
```

So - you start screen with calendar, pager receives adapter and creates fragments that hold month grid directly. In sample application it is called MonthFragment and, beside standard onCreateView method, has one significant part of calendar - initiateCellArray method. Basicly, this is the core place where you shoud put your code that calculates all necessary dataset for your calendar. For now, data model (class called DayModel) holds two fields that are used by grid adapter for building correct calendar grid:

```java
private boolean isEmptyCell = false;
private boolean isToday;
```

This is the place where you can code more fields to hold your data. With supplementary changes to grid adapter's getView and initiateCellArray in MonthFragment - allows you to enrich your calendar with any kind of events.

Now let's take a step back to MonthFragment. For now, initiateCellArray accepts [joda](http://www.joda.org/joda-time/)'s DateTime object, which points somewhere to current month and builds up an ArrayList with DayModel - one for each day in cell:

```java
     /**
     * Based on this month's DateTime object calculates array of DayModel objects, that will be used to build grid.
     *
     * @param dateTime this month's DateTime
     */
    private List<DayModel> initiateCellArray(DateTime dateTime) {
        int previousDaysInMonth, daysInMonth, nextDaysInMonth;
        List<DayModel> days = new ArrayList<DayModel>();

        int year = dateTime.getYear();
        int month = dateTime.getMonthOfYear();
        int day = dateTime.getDayOfMonth();

        DateTime monthDateTime = new DateTime(year, month, day,
                0, 0, 0);

        // how many "empty" days there are in the first week that pertain to previous month
        previousDaysInMonth = monthDateTime.withDayOfMonth(1).getDayOfWeek() - 1;

        daysInMonth = monthDateTime.dayOfMonth().getMaximumValue();

        // how many "empty" days there are after last month's day in the last week that pertain to next month
        nextDaysInMonth = 7 - monthDateTime.withDayOfMonth(daysInMonth).getDayOfWeek();

        for (int i = 0; i < previousDaysInMonth; i++) {
            // for every "empty" cell create empty cell model
            DayModel mModel = new DayModel().setDateTime(dateTime.withDayOfMonth(1).plusDays(i - previousDaysInMonth));
            mModel.setPreviousOrLast(true);
            days.add(mModel);
        }

        for (int i = 0; i < daysInMonth; i++) {
            // create cell for day of month and populate it with holiday if present
            DayModel mModel = new DayModel().setDateTime(dateTime.withDayOfMonth(i + 1));

            if (null != monthHolidays && !monthHolidays.isEmpty()) {
                for (HolidayModel mHolidayModel : monthHolidays) {
                    if (mHolidayModel.getDate().withTimeAtStartOfDay()
                            .equals(mModel.getDateTime().withTimeAtStartOfDay())) {
                        mModel.setHoliday(mHolidayModel);
                        break;
                    }
                }
            }

            days.add(mModel);
        }

        for (int i = 0; i < nextDaysInMonth; i++) {
            // for every "empty" cell create empty cell model
            DayModel mModel = new DayModel().setDateTime(dateTime.withDayOfMonth(daysInMonth).plusDays(i + 1));
            mModel.setPreviousOrLast(true);
            days.add(mModel);
        }

        return days;
    }
```

As you can see, with the help of joda time library it pretty trivial: we get monthDateTime that points to first day in month, get the weekday number from it and calculate number of empty cells before the first day of month. Same way is for empty cells at the end.

Then we use these ints in loops and create DayModel items.

The last point is the getView method in grid adapter (MonthAdapter):

```java
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.grid_cell, viewGroup, false);

            holder = new ViewHolder();
            holder.textViewDate = (TextView) convertView.findViewById(R.id.grid_cell_tv);
            holder.viewGroup = (ViewGroup) convertView;
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DayModel day = getItem(position);

        holder.textViewDate.setText(Integer.toString(day.getDayNumber()));

        if (day.isPreviousOrLast()) {
            holder.textViewDate.setTextColor(context.getResources().getColor(R.color.newcal_gridcell_stroke));
            holder.viewGroup.setBackgroundResource(R.drawable.gridcell_inactive_selector);
        } else {
            if (day.isWeekend()) {
                holder.textViewDate.setTextColor(context.getResources().getColor(R.color.holo_red_light));
            }

            if (day.isToday()) {
                holder.viewGroup.setBackgroundResource(R.drawable.gridcell_today_selector);
            } else {
                holder.viewGroup.setBackgroundResource(R.drawable.gridcell_selector);
            }

            if (day.isHoliday()) {
                holder.textViewDate.setTextColor(context.getResources().getColor(android.R.color.white));
                holder.viewGroup.setBackgroundResource(R.drawable.gridcell_holiday_selector);
            }
        }

        holder.viewGroup.setMinimumHeight(expectedMinimumHeight);

        return convertView;
    }

```

Nothing complex as you can see: having the list of the days and current position we analyse current day's state (isEmptyCell(), day.isToday(), etc) and do corresponding changes to grid view.

## Putting data to grid

What't the point of having calendar screen without some custom data and events on it?
Let's implement something to show for particular days.
A good example will be to show United Kingdom public holidays on it.

We will get holidays in json format from [Enrico Service](http://www.kayaposoft.com/enrico/json/). To simplify network-related stuff I used [AsyncHttpClient](http://loopj.com/android-async-http/).

So, in CalendarFragment I created obtainHolidays method, that queried Enrico and deserialized it to list of HolidayModel objects, that I created (I'm not providing more details since this is not related to calendar creation. You can see details in sample app code).

Next, via setter-method I put obtained holidays to pager adapter, which are then used during creating a MonthFragment - I put into month's fragment only holidays for that month.

In MonthFragment I need to process that data to use it in grid.
First, in DayModel I added holiday field of HolidayModel type.
Next, changed second for loop in initiateCellArray:

```java
if(!monthHolidays.isEmpty()) {
    for(HolidayModel mHolidayModel : monthHolidays) {
        if(mHolidayModel.getDate().withTimeAtStartOfDay()
            .equals(mModel.getDateTime().withTimeAtStartOfDay())) {
            mModel.setHoliday(mHolidayModel);
        }
    }
}
```

Now some DayModels will have info about holiday inside. We need to use that in GridView adapter's getView method. It was implemented with the following:

```java
  if (day.isHoliday()) {
     holder.textViewDate.setTextColor(context.getResources().getColor(android.R.color.white));
     holder.viewGroup.setBackgroundResource(R.drawable.gridcell_holiday_selector);
  }

```

Now some days display in red bold and have a holiday name displayed under date.

## Handle grid cell clicks

We have created interface CalendarListener:
```java
public interface CalendarListener {

    public void onDateSelected(DayModel dayModel);
}
```
It hase one method, which called when user clicks the item on the grid view.

In MonthFragment's onCreateView we add this:

```java
monthGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      if (calendarListener != null) {
          calendarListener.onDateSelected(cellModels.get(position));
      }
   }
});
```

We use FragmentStatePagerAdapter to use memory efficiently, so we should add some code to our activity class, so that our listener will restore after user rotates the phone.

```java
CalendarFragment calendarFragment = (CalendarFragment) getSupportFragmentManager()
            .findFragmentByTag(CalendarFragment.TAG);
calendarFragment.setCalendarListener(listener);
```
## Almost "The end"

So, to sum everything up - this is the way you can implement a calendar for your app. Remember that this is not a ready-to-use library, where you can extend and override here and there and forget about hard work. This is more like a set of milestones that I've met on the way from bare idea of building a calendar from scratch to the finished sample.

You can use the whole idea or peek some key moments from here. I hope it will be useful.

## Why not library? ("The end")

You might think that a good thing would be to wrap this into complete library, so that one could just extend CalendarFragment, override initiateCellArray and getView methods and be happy. Well, that's true. The reason why I didn't do so - trivial lack of time.
Maybe this will be implemented later.