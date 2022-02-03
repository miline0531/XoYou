package kr.co.genericit.mybase.xoyou2.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.model.WeYouUnDataListObj;


public class WeUnListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    ArrayList<WeYouUnDataListObj> items;
    CommonUtil dataSet = CommonUtil.getInstance();

    public WeUnListAdapter(Activity a, ArrayList<WeYouUnDataListObj> m_board) {
        activity = a;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        items = m_board;

    }

    public int getCount() {
        return items.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        ImageView manage_image_view;
        TextView item_name;
        TextView item_age;
        TextView item_info;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final WeYouUnDataListObj board = items.get(position);
        ViewHolder vh = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_weunlist, null);
            vh.manage_image_view         = (ImageView) convertView.findViewById(R.id.manage_image_view);
            vh.item_name         = (TextView) convertView.findViewById(R.id.item_name);
            vh.item_age         = (TextView) convertView.findViewById(R.id.item_age);
            vh.item_info         = (TextView) convertView.findViewById(R.id.item_info);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        Log.e("SKY", "position : " + position);


        String resName = "a_sample_icon"+0;
        Glide.with(activity).load(board.getImage()).thumbnail(0.1f).centerCrop().placeholder(findRes(resName)).into(vh.manage_image_view);

        vh.item_name.setText(board.getName());
        vh.item_age.setText(board.getInfo());
        vh.item_info.setText(board.getUnSimRi());

        return convertView;
    }

    //리소스 아이디참조
    public int findRes(String pVariableName) {
        try {
            return activity.getResources().getIdentifier(pVariableName, "drawable", activity.getPackageName());
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

}