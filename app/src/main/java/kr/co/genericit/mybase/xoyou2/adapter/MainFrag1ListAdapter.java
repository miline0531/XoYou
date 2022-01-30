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


public class MainFrag1ListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    ArrayList<SimRi> items;

    public MainFrag1ListAdapter(Activity a, ArrayList<SimRi> m_board) {
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
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final SimRi board = items.get(position);
        ViewHolder vh = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.mainfrag1_item, null);
            vh.txt1         = (TextView) convertView.findViewById(R.id.txt1);
            vh.txt2         = (TextView) convertView.findViewById(R.id.txt2);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        Log.e("SKY", "position : " + position);

        vh.txt1.setText(board.getName());
        vh.txt2.setText(board.getSimRiInfo());

        return convertView;
    }


}