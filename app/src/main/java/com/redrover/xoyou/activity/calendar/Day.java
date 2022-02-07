package com.redrover.xoyou.activity.calendar;

import java.util.Calendar;

import com.redrover.xoyou.utils.DateUtil;

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
