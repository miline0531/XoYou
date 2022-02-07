package com.redrover.xoyou.model;


import java.util.ArrayList;

public class CalendarData {
    private int id;
    private int date;
    private ArrayList<scheduleData> schedule;

    public class scheduleData {
        private int scheduleId;
        private int startDate;
        private int endDate;
        private String title;
        private int bgColor;
        private int rowNumber; // max:2
    }
}