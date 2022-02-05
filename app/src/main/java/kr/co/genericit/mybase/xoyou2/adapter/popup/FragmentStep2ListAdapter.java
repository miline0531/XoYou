package kr.co.genericit.mybase.xoyou2.adapter.popup;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.model.QaListObj;


public class FragmentStep2ListAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    ArrayList<QaListObj> items;
    CommonUtil dataSet = CommonUtil.getInstance();

    public FragmentStep2ListAdapter(Activity a, ArrayList<QaListObj> m_board) {
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
        final QaListObj board = items.get(position);
        ViewHolder vh = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.fragment2_item, null);
            vh.txt1         = (TextView) convertView.findViewById(R.id.txt1);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        Log.e("SKY", "position : " + position);



        vh.txt1.setText(board.getMenuName());

        return convertView;
    }


}