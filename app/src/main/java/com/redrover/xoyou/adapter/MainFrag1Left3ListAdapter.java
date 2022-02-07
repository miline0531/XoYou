package com.redrover.xoyou.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.redrover.xoyou.R;
import com.redrover.xoyou.common.SkyLog;
import com.redrover.xoyou.model.QaListObj;


public class MainFrag1Left3ListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    ArrayList<QaListObj> items;

    public MainFrag1Left3ListAdapter(Activity a, ArrayList<QaListObj> m_board) {
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
        TextView txt1;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        QaListObj board = items.get(position);
        ViewHolder vh = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mainfrag1left_item, null);
            vh.txt1         = (TextView) convertView.findViewById(R.id.txt1);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        SkyLog.d("position11 : " + position);

        vh.txt1.setText(board.getMenuName());

        return convertView;
    }


}