package kr.co.genericit.mybase.xoyou2.activity.calendar;

import java.util.Calendar;

import kr.co.genericit.mybase.xoyou2.utils.DateUtil;

public class Day extends ViewModel {

    String day;

    public Day() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    // TODO : day에 달력일값넣기
    public void setCalendar(Calendar calendar){

        day = DateUtil.getDate(calendar.getTimeInMillis(), DateUtil.DAY_FORMAT);

    }



}
