package kr.co.genericit.mybase.xoyou2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.Mong;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;

public class ManageVerticalRecyclerviewAdapter extends RecyclerView.Adapter<ManageVerticalRecyclerviewAdapter.MyViewHolder>{
    private Context mContext;
    private ArrayList<Mong> data;
    private boolean isManage = false;

    public void setManageType(boolean value){
        isManage = value;
    }
    public ManageVerticalRecyclerviewAdapter(Context context, ArrayList<Mong> data){
        mContext = context;
        this.data = data;

    }

    @NonNull
    @Override
    public ManageVerticalRecyclerviewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manage_list_vertical_bak,parent,false);
        return new ManageVerticalRecyclerviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageVerticalRecyclerviewAdapter.MyViewHolder holder, int position) {

        /*String auth = "";
        if(data.get(position).getTRANS_STATUS()==1){
            auth = "인증완료";
        }else{
            auth = "인증하기";
        }
        holder.text_mong_auth.setText("auth:"+data.get(position).getTRANS_STATUS());

        holder.text_mong_kind.setText(data.get(position).getTHEME_TYPE());*/

        String date = data.get(position).getREG_DATE().substring(0,10);

        holder.text_top_1.setText(CommandUtil.getInstance().getStr(R.string.mong_manage_vertical_reg_date));
        holder.text_top_2.setText(date+getDayOfweek(date));
        holder.text_middle.setText(data.get(position).getTITLE());
        if(isManage){
            holder.text_mong_type.setVisibility(View.VISIBLE);
            holder.text_mong_type.setText(data.get(position).getTHEME_TYPE());
            holder.layout_store.setVisibility(View.GONE);
        }else{
            holder.text_mong_type.setVisibility(View.GONE);
            holder.layout_store.setVisibility(View.VISIBLE);
        }

        holder.text_sell_type.setVisibility(View.GONE);

        if(!data.get(position).getSELL_TYPE().equals("null")){
            holder.text_recommend.setText(data.get(position).getSELL_TYPE());
        }

        Log.v("ifeelbluu", "data.get(position).getSELL_TYPE : " + data.get(position).getSELL_TYPE());
        String mongType = data.get(position).getMONG_TYPE();
        if(mongType == null || mongType.equals("null")){
            holder.text_mong_type.setText("[TYPE]");
        }else{
            holder.text_mong_type.setText(data.get(position).getTHEME_TYPE()+", ");
        }

        if(data.get(position).getTRANS_TYPE()==1){ //고정가
            holder.text_bottom.setText(CommandUtil.getInstance().getStr(R.string.mong_manage_vertical_recommend) + data.get(position).getMONG_PRICE());
        }else{ //경매가
//            String bidValueStr = "";
//            float bidValue = Float.parseFloat(data.get(position).getBID_VALUE());
//            if(bidValue != 0f)
//                bidValueStr = bidValue;

            String d_day ="";
            if(data.get(position).getEND_DATE()!=null&&!data.get(position).getEND_DATE().equals("")){
                d_day = getD_Day(data.get(position).getEND_DATE());
            }

            if(d_day.equals("0")){
                d_day = "";
            }else{
                d_day = "D-" + d_day + ", ";

            }
            holder.text_bottom.setText(d_day + data.get(position).getBID_VALUE() + ", " + data.get(position).getBID_COUNT());
        }


        String resName = "a_sample_icon"+position;
        //holder.image.setBackgroundResource(findRes(resName));
        Glide.with(mContext).load(data.get(position).getIMAGE_URL()).thumbnail(0.1f).centerCrop().placeholder(findRes(resName)).into(holder.image);


        final int onClickIndex = position;
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(mListener != null)
                mListener.onClickItem(onClickIndex,0);
            }
        });
        //CommandUtil.getInstance().getStr(R.string.fragment_home_frag_recommend) + " " + priceInt/100 + "." + priceInt%100;
        int priceInt = Math.round(Float.parseFloat(data.get(position).getMONG_PRICE())*100);
        String price =CommandUtil.getInstance().getStr(R.string.fragment_home_frag_recommend)+" " + priceInt/100 + "." + priceInt%100;
        holder.price.setText(price);
        holder.text_mong_kind.setText(data.get(position).getMONG_TYPE() + CommandUtil.getInstance().getStr(R.string.dream));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public String getD_Day(String date){
        Log.v("ifeelbluu", "date :: " + date);
        String endDate1 = date.split(" ")[0];
        String[] endDate2 = endDate1.split("/");
        int year = Integer.parseInt(endDate2[2]);
        int month = Integer.parseInt(endDate2[0]);
        int day = Integer.parseInt(endDate2[1]);

        int dCount = countdday(year,month,day);

        return ""+dCount;
    }
    public int countdday(int myear, int mmonth, int mday) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Calendar todaCal = Calendar.getInstance(); //오늘날자 가져오기
            Calendar ddayCal = Calendar.getInstance(); //오늘날자를 가져와 변경시킴

            mmonth -= 1; // 받아온날자에서 -1을 해줘야함.
            ddayCal.set(myear,mmonth,mday);// D-day의 날짜를 입력

            long today = todaCal.getTimeInMillis()/86400000; //->(24 * 60 * 60 * 1000) 24시간 60분 60초 * (ms초->초 변환 1000)
            long dday = ddayCal.getTimeInMillis()/86400000;
            long count = dday - today; // 오늘 날짜에서 dday 날짜를 빼주게 됩니다.
            return (int) count;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return -1;
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView text_sell_type, text_top_1,text_top_2,text_middle,text_bottom,
                text_recommend, text_mong_type, text_mong_kind, text_mong_auth,price;
        private ImageView image;
        private RelativeLayout itemLayout;
        private LinearLayout layout_store;
        public MyViewHolder(View itemView) {
            super(itemView);
            text_top_1 = itemView.findViewById(R.id.text_top_1);
            text_top_2 = itemView.findViewById(R.id.text_top_2);
            text_middle = itemView.findViewById(R.id.text_middle);
            text_bottom = itemView.findViewById(R.id.text_bottom);
            image = itemView.findViewById(R.id.manage_image_view);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            text_mong_type = itemView.findViewById(R.id.text_mong_type);
            text_recommend = itemView.findViewById(R.id.text_recommend);
            text_sell_type = itemView.findViewById(R.id.text_sell_type);
            price = itemView.findViewById(R.id.price);
            text_mong_kind = itemView.findViewById(R.id.text_mong_kind);
            layout_store = itemView.findViewById(R.id.layout_store);
           // text_mong_auth = itemView.findViewById(R.id.text_mong_auth);
        }
    }

    public listOnClickListener mListener;

    public void setListOnClickListener(listOnClickListener listener){
        mListener = listener;
    }
    public interface listOnClickListener {
        void onClickItem(int id, int action);
    }


    //리소스 아이디참조
    public int findRes(String pVariableName) {
        Log.v("ifeelbluu","findRes ::: "  + pVariableName);
        try {
            return mContext.getResources().getIdentifier(pVariableName, "drawable", mContext.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public String getDayOfweek(String date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String[] week = mContext.getResources().getStringArray(R.array.str_week);
        Calendar cal = Calendar.getInstance();
        Date getDate; try { getDate = format.parse(date);
            cal.setTime(getDate);
            int w = cal.get(Calendar.DAY_OF_WEEK)-1;
            return "("+week[w]+")";
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }



}

