package kr.co.genericit.mybase.xoyou2.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.model.Contactobj;


public class ContactAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    ArrayList<Contactobj> items;

    public ContactAdapter(Activity a, ArrayList<Contactobj> m_board) {
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
        TextView name , number;
        RelativeLayout default_img;
        TextView idid_txt;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        final Contactobj board = items.get(position);
        ViewHolder vh = new ViewHolder();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.contact_item, null);
            vh.name         = (TextView) convertView.findViewById(R.id.name);
            vh.number         = (TextView) convertView.findViewById(R.id.number);
            vh.default_img         = (RelativeLayout) convertView.findViewById(R.id.default_img);
            vh.idid_txt         = (TextView) convertView.findViewById(R.id.idid_txt);

            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
//        Log.e("SKY", "position : " + position);



        vh.name.setText(board.getName());
        vh.number.setText(board.getNumber());
        String val = "";
        if(board.getName().length() > 1){
            val = board.getName().substring(0,2);
        }else{
            val = board.getName();
        }
        vh.idid_txt.setText(val);

        backColor(vh.default_img , ("" + (int)(Math.random()*6)));


        return convertView;
    }


    private void backColor(RelativeLayout rd , String val){
        if(val.equals("0")){
            rd.setBackgroundResource(R.drawable.img_profile_box1);
        }else if(val.equals("1")){
            rd.setBackgroundResource(R.drawable.img_profile_box2);
        }else if(val.equals("2")){
            rd.setBackgroundResource(R.drawable.img_profile_box3);
        }else if(val.equals("3")){
            rd.setBackgroundResource(R.drawable.img_profile_box4);
        }else if(val.equals("4")){
            rd.setBackgroundResource(R.drawable.img_profile_box5);
        }else if(val.equals("5")){
            rd.setBackgroundResource(R.drawable.img_profile_box6);
        }else if(val.equals("6")){
            rd.setBackgroundResource(R.drawable.img_profile_box7);
        }
    }


}