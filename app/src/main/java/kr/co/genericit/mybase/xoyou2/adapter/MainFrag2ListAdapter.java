package kr.co.genericit.mybase.xoyou2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.SimRi;
import kr.co.genericit.mybase.xoyou2.model.WeListObj;


public class MainFrag2ListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    ArrayList<WeListObj> items;

    public MainFrag2ListAdapter(Activity a, ArrayList<WeListObj> m_board) {
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
        TextView txt2;
        TextView txt3;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final WeListObj board = items.get(position);
        ViewHolder vh = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mainfrag2_item, null);
            vh.txt1         = (TextView) convertView.findViewById(R.id.txt1);
            vh.txt2         = (TextView) convertView.findViewById(R.id.txt2);
            vh.txt3         = (TextView) convertView.findViewById(R.id.txt3);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        Log.e("SKY", "position : " + position);

        vh.txt1.setText(board.getUnName());
        vh.txt2.setText(board.getMaxName());
        vh.txt3.setText(board.getMinName());

        return convertView;
    }


}