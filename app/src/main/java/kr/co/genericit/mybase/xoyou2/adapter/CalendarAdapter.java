package kr.co.genericit.mybase.xoyou2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.Calendar;
import java.util.List;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.activity.calendar.CalendarHeader;
import kr.co.genericit.mybase.xoyou2.activity.calendar.Day;
import kr.co.genericit.mybase.xoyou2.activity.calendar.EmptyDay;
import kr.co.genericit.mybase.xoyou2.utils.DateUtil;

public class CalendarAdapter extends RecyclerView.Adapter{
    private Context mContext;

    private final int HEADER_TYPE = 0;
    private final int EMPTY_TYPE = 1;
    private final int DAY_TYPE = 2;

    private List<Object> mCalendarList;

    public CalendarAdapter(Context context, List<Object> data){
        mContext = context;
        this.mCalendarList = data;
    }

    public void setCalendarList(List<Object> calendarList) {
        mCalendarList = calendarList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) { //뷰타입 나누기
        Object item = mCalendarList.get(position);
        if (item instanceof Long) {
            return HEADER_TYPE; //날짜 타입
        } else if (item instanceof String) {
            return EMPTY_TYPE; // 비어있는 일자 타입
        } else {
            return DAY_TYPE; // 일자 타입

        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        // 날짜 타입
        if (viewType == HEADER_TYPE) {

            HeaderViewHolder viewHolder = new HeaderViewHolder(inflater.inflate(R.layout.item_calendar_header, parent, false));

            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams)viewHolder.itemView.getLayoutParams();
            params.setFullSpan(true); //Span을 하나로 통합하기
            viewHolder.itemView.setLayoutParams(params);

            return viewHolder;

            //비어있는 일자 타입
        } else if (viewType == EMPTY_TYPE) {
            return new EmptyViewHolder(inflater.inflate(R.layout.item_day_empty, parent, false));

        }
        // 일자 타입
        else {
            return new DayViewHolder(inflater.inflate(R.layout.item_day, parent, false));

        }


//        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manage_list_horizontal,parent,false);
//        return new CalendarAdapter.MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);

        /**날짜 타입 꾸미기*/
        /** EX : 2018년 8월*/
        if (viewType == HEADER_TYPE) {
            HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            CalendarHeader model = new CalendarHeader();

            // long type의 현재시간
            if (item instanceof Long) {
                // 현재시간 넣으면, 2017년 7월 같이 패턴에 맞게 model에 데이터들어감.
                model.setHeader((Long) item);
            }
            // view에 표시하기
            holder.bind(model);
        }
        /** 비어있는 날짜 타입 꾸미기 */
        /** EX : empty */
        else if (viewType == EMPTY_TYPE) {
            EmptyViewHolder holder = (EmptyViewHolder) viewHolder;
            EmptyDay model = new EmptyDay();
            holder.bind(model);
        }
        /** 일자 타입 꾸미기 */
        /** EX : 22 */
        else if (viewType == DAY_TYPE) {
            DayViewHolder holder = (DayViewHolder) viewHolder;
            Object item = mCalendarList.get(position);
            Day model = new Day();
            if (item instanceof Calendar) {

                // Model에 Calendar값을 넣어서 몇일인지 데이터 넣기
                model.setCalendar((Calendar) item);


            }
            // Model의 데이터를 View에 표현하기
            holder.bind(model);
            if(position%2==1){
                holder.schedule1(View.VISIBLE);
            }else if(position%3 == 0){
                holder.schedule2(View.VISIBLE);
            }else if(position%10 == 0){
                holder.schedule3(View.VISIBLE);
            }else{
                holder.schedule1(View.INVISIBLE);
                holder.schedule2(View.INVISIBLE);
                holder.schedule3(View.INVISIBLE);
            }

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String day = DateUtil.getDate(((Calendar) item).getTimeInMillis(), DateUtil.DAY_FORMAT);
                    Toast.makeText(mContext,day ,Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    // 개수구하기
    @Override
    public int getItemCount() {
        if (mCalendarList != null) {
            return mCalendarList.size();
        }
        return 0;
    }


    /** ViewHolder **/
    private class HeaderViewHolder extends RecyclerView.ViewHolder { //날짜 타입 ViewHolder

        TextView itemHeaderTitle;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }


        public void initView(View v){

            itemHeaderTitle = (TextView)v.findViewById(R.id.item_header_title);

        }

        public void bind(CalendarHeader model){

            // 일자 값 가져오기
            String header = ((CalendarHeader)model).getHeader();

            // header에 표시하기, ex : 2018년 8월
            itemHeaderTitle.setText(header);


        };
    }


    private class EmptyViewHolder extends RecyclerView.ViewHolder { // 비어있는 요일 타입 ViewHolder


        public EmptyViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);
        }

        public void initView(View v){

        }

        public void bind(EmptyDay model){


        };
    }

    // TODO : item_day와 매칭
    private class DayViewHolder extends RecyclerView.ViewHolder {// 요일 입 ViewHolder
        RelativeLayout itemLayout;
        TextView itemDay;
        TextView txt_schedule_1,txt_schedule_2,txt_schedule_3;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            initView(itemView);

        }

        public void initView(View v){
            itemLayout = v.findViewById(R.id.item_layout);
            itemDay = (TextView)v.findViewById(R.id.item_day);
            txt_schedule_1 = (TextView)v.findViewById(R.id.txt_schedule_1);
            txt_schedule_2 = (TextView)v.findViewById(R.id.txt_schedule_2);
            txt_schedule_3 = (TextView)v.findViewById(R.id.txt_schedule_3);

        }

        public void bind(Day model){

            // 일자 값 가져오기
            String day = ((Day)model).getDay();

            // 일자 값 View에 보이게하기
            itemDay.setText(day);

        };

        public void schedule1(int visible){
            txt_schedule_1.setVisibility(visible);
        }
        public void schedule2(int visible){
            txt_schedule_2.setVisibility(visible);
        }
        public void schedule3(int visible){
            txt_schedule_3.setVisibility(visible);
        }
    }





}
