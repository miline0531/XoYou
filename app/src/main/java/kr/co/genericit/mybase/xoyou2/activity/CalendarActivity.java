package kr.co.genericit.mybase.xoyou2.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import org.threeten.bp.DayOfWeek;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import kr.co.genericit.mybase.xoyou2.adapter.CalendarAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.databinding.ActivityCalendarBinding;
import kr.co.genericit.mybase.xoyou2.utils.Keys;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.BottomSheetDefaultListFragment;
import kr.co.genericit.mybase.xoyou2.view.bottomsheet.model.BottomSheetData;
import me.jlurena.revolvingweekview.WeekView;
import me.jlurena.revolvingweekview.WeekViewEvent;

public class CalendarActivity extends CommonActivity {
    public int mTodayPosition;
    public int mCenterPosition;
    public ArrayList<Object> mCalendarList = new ArrayList<>();

    private CalendarAdapter mAdapter;
    private StaggeredGridLayoutManager manager;

    private ArrayList<BottomSheetData> mCalEventData, mCalTypeData;

    private ActivityCalendarBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityCalendarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);

        initTestTypeData();
        initView();
        initSet();
        setRecycler();
    }

    private void initView() {
        //현재 월로 이동
        binding.btnToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((StaggeredGridLayoutManager)binding.rcvCalendarList.getLayoutManager()).scrollToPositionWithOffset(mCenterPosition,0);
            }
        });


        binding.btnCalendarEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDefaultListFragment bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
                bottomSheetDLFragment.setData(mCalEventData);
                bottomSheetDLFragment.setListener(mBottomSheetEventListener);
                bottomSheetDLFragment.setCancelable(false);
                bottomSheetDLFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });

        binding.btnCalendarType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDefaultListFragment bottomSheetDLFragment = BottomSheetDefaultListFragment.newInstance();
                bottomSheetDLFragment.setData(mCalTypeData);
                bottomSheetDLFragment.setListener(mBottomSheetTypeListener);
                bottomSheetDLFragment.setCancelable(false);
                bottomSheetDLFragment.show(getSupportFragmentManager(), "Bottom Sheet Dialog Fragment");
            }
        });

        testWeekView();
    }

    private void testWeekView(){
        // Set an WeekViewLoader to draw the events on load.
        binding.revolvingWeekview.setWeekViewLoader(new WeekView.WeekViewLoader() {

            @Override
            public List<? extends WeekViewEvent> onWeekViewLoad() {
                List<WeekViewEvent> events = new ArrayList<>();
                // Add some events
                return events;
            }
        });

        binding.revolvingWeekview.setNumberOfVisibleDays(7);
        binding.revolvingWeekview.setScrollListener(new WeekView.ScrollListener() {
            @Override
            public void onFirstVisibleDayChanged(DayOfWeek newFirstVisibleDay, DayOfWeek oldFirstVisibleDay) {

            }
        });
        binding.revolvingWeekview.setDateTimeInterpreter(new me.jlurena.revolvingweekview.DateTimeInterpreter() {
            @Override
            public String interpretDate(DayOfWeek day) {
                Date calendar = new Date();
                SimpleDateFormat weekdayNameFormat = new SimpleDateFormat("EEE", Locale.getDefault());
                String weekday = weekdayNameFormat.format(calendar.getTime());
                SimpleDateFormat format = new SimpleDateFormat(" M/d", Locale.getDefault());

                weekday = String.valueOf(weekday.charAt(0));
                return weekday.toUpperCase() + format.format(calendar.getTime());
//                return day.getValue()+"";
            }

            @Override
            public String interpretTime(int hour, int minutes) {
                return hour > 11 ?  "오후 " + (hour - 12) : (hour == 0 ? "오전 12" : "오전 " + hour);
            }
        });


    }

    private void initTestTypeData(){
        mCalEventData = new ArrayList<>();
        BottomSheetData item = new BottomSheetData(0,"모두",true);
        mCalEventData.add(item);
        item = new BottomSheetData(1,"길몽",false);
        mCalEventData.add(item);
        item = new BottomSheetData(2,"영몽",false);
        mCalEventData.add(item);
        item = new BottomSheetData(3,"평몽",false);
        mCalEventData.add(item);
        item = new BottomSheetData(4,"악몽",false);
        mCalEventData.add(item);
        item = new BottomSheetData(5,"흉몽",false);
        mCalEventData.add(item);

        mCalTypeData = new ArrayList<>();
        BottomSheetData typeItem = new BottomSheetData(0,"월",true);
        mCalTypeData.add(typeItem);
        typeItem = new BottomSheetData(0,"주",false);
        mCalTypeData.add(typeItem);
        typeItem = new BottomSheetData(0,"일",false);
        mCalTypeData.add(typeItem);
    }

    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetEventListener = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                mCalEventData.get(prev).setCheck(false);

            mCalEventData.get(type).setCheck(true);
            binding.txtCalendarEvent.setText(mCalEventData.get(type).getItemTitle());

        }
    };
    private BottomSheetDefaultListFragment.bottomSheetListener mBottomSheetTypeListener = new BottomSheetDefaultListFragment.bottomSheetListener() {
        @Override
        public void onSelectItem(int type, int prev) {
            if(prev != -1)
                mCalTypeData.get(prev).setCheck(false);

            mCalTypeData.get(type).setCheck(true);

            Log.v("ifeelbluu", "type  :: " + type);
//            layout_day_calendar
            if(type == 0){ //월간
                binding.layoutMonthCalendar.setVisibility(View.VISIBLE);
                binding.layoutWeekCalendar.setVisibility(View.GONE);
                binding.layoutDayCalendar.setVisibility(View.GONE);
            }else if(type == 1){ //주간
                binding.layoutWeekCalendar.setVisibility(View.VISIBLE);
                binding.layoutMonthCalendar.setVisibility(View.GONE);
                binding.layoutDayCalendar.setVisibility(View.GONE);
            }else{ //일간
                binding.layoutDayCalendar.setVisibility(View.VISIBLE);
                binding.layoutWeekCalendar.setVisibility(View.GONE);
                binding.layoutMonthCalendar.setVisibility(View.GONE);
            }
            binding.txtCalendarType.setText(mCalTypeData.get(type).getItemTitle());

        }
    };


    public void initSet(){

        initCalendarList();

    }

    public void initCalendarList() {
        GregorianCalendar cal = new GregorianCalendar();
        setCalendarList(cal);
    }

    private void setRecycler() {

        if (mCalendarList == null) {
            Log.w("ifeelbluu", "No Query, not initializing RecyclerView");
        }

        manager = new StaggeredGridLayoutManager(7, StaggeredGridLayoutManager.VERTICAL);

        mAdapter = new CalendarAdapter(this, mCalendarList);

        mAdapter.setCalendarList(mCalendarList);
        binding.rcvCalendarList.setLayoutManager(manager);
        binding.rcvCalendarList.setAdapter(mAdapter);

        if (mCenterPosition >= 0) {
            binding.rcvCalendarList.scrollToPosition(mCenterPosition);
        }
    }

    public void setCalendarList(GregorianCalendar cal) {

        setTitle(cal.getTimeInMillis() + "");

        ArrayList<Object> calendarList = new ArrayList<>();

        for (int i = -300; i < 300; i++) {
            try {
                GregorianCalendar calendar = new GregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + i, 1, 0, 0, 0);
                if (i == 0) {
                    mCenterPosition = calendarList.size();
                    mTodayPosition = calendarList.size();
                    Log.v("ifeelbluu", "mCenterPosition :: " + mCenterPosition);
                }

                // 타이틀인듯
                calendarList.add(calendar.getTimeInMillis());

                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //해당 월에 시작하는 요일 -1 을 하면 빈칸을 구할 수 있겠죠 ?
                int max = calendar.getActualMaximum(Calendar.DAY_OF_MONTH); // 해당 월에 마지막 요일

                // EMPTY 생성
                for (int j = 0; j < dayOfWeek; j++) {
                    calendarList.add(Keys.EMPTY);
                }
                for (int j = 1; j <= max; j++) {
                    calendarList.add(new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), j));
                }

                // TODO : 결과값 넣을떄 여기다하면될듯

            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        mCalendarList = calendarList;
    }
}