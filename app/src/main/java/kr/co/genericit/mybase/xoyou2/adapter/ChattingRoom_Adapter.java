package kr.co.genericit.mybase.xoyou2.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import java.sql.Timestamp;
import java.util.ArrayList;

import co.kr.sky.Check_Preferences;
import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonUtil;
import kr.co.genericit.mybase.xoyou2.model.ContractObj;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;


public class ChattingRoom_Adapter extends BaseAdapter {
	CommonUtil dataSet = CommonUtil.getInstance();

	private Activity activity;
	private static LayoutInflater inflater=null;
	ArrayList<ContractObj> items;
	private String Left_Nickname;
	private String RECIEVE_IMG;
	private int type;
	private int size;
	public ChattingRoom_Adapter(Activity a, ArrayList<ContractObj> m_board) {
		activity = a;

		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		items = m_board;
		this.type = type;
		this.size = size;
		this.RECIEVE_IMG = RECIEVE_IMG;
		this.Left_Nickname = Left_Nickname;

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
		RelativeLayout left_view_msg , right_view_msg;
		ImageView left_item_img;
		TextView left_item_name;
		TextView left_item_msg;
		TextView right_item_msg;
		TextView left_item_center;
		TextView left_item_date;
		TextView right_item_name;
		TextView right_item_date;
	}
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ContractObj board = items.get(position);
		ViewHolder vh = new ViewHolder();
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_chat,null);

			vh.left_view_msg = (RelativeLayout) convertView.findViewById(R.id.left_view_msg);
			vh.right_view_msg = (RelativeLayout) convertView.findViewById(R.id.right_view_msg);
			vh.left_item_name = (TextView) convertView.findViewById(R.id.left_item_name);
			vh.left_item_msg = (TextView) convertView.findViewById(R.id.left_item_msg);
			vh.right_item_msg = (TextView) convertView.findViewById(R.id.right_item_msg);
			vh.left_item_center = (TextView) convertView.findViewById(R.id.left_item_center);
			vh.left_item_date = (TextView) convertView.findViewById(R.id.left_item_date);
			vh.right_item_name = (TextView) convertView.findViewById(R.id.right_item_name);
			vh.right_item_date = (TextView) convertView.findViewById(R.id.right_item_date);

			convertView.setTag(vh);
		}else {
			vh = (ViewHolder) convertView.getTag();
		}

		//받은건 flag = 0(왼쪽) , 보낸건 flag = 1(오른쪽)
		if(board.getSend_Flag().equals("0")){
			//왼쪽
			vh.right_view_msg.setVisibility(View.GONE);
			vh.left_view_msg.setVisibility(View.VISIBLE);
			vh.right_item_msg.setVisibility(View.GONE);

			vh.left_item_name.setText(board.getName());
			vh.left_item_msg.setText(board.getBody());
			vh.left_item_center.setText(board.getSuggestion());

			//SimpleDateFormat sdf = new SimpleDateFormat( "yyyy년 MM월 dd일 HH시 mm분" , Locale.KOREA );
			SimpleDateFormat sdf = new SimpleDateFormat( "HH-mm-yyyy" , Locale.KOREA );
			String date = sdf.format( new Date( Long.parseLong(board.getTimestamp()) ) );
			vh.left_item_date.setText(date);

		}else{
			vh.left_view_msg.setVisibility(View.GONE);
			vh.right_view_msg.setVisibility(View.VISIBLE);
			vh.right_item_msg.setVisibility(View.VISIBLE);
			JWSharePreference sharePreference = new JWSharePreference();
			String name = sharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NICKNAME,"");

			vh.right_item_msg.setText(board.getBody());
			vh.right_item_name.setText(name);


			SimpleDateFormat sdf = new SimpleDateFormat( "HH-mm-yyyy" , Locale.KOREA );
			String date = sdf.format( new Date( Long.parseLong(board.getTimestamp()) ) );
			vh.right_item_date.setText(date);

		}
//		vh.left_item_msg.setTextSize(size);
//		vh.right_item_msg.setTextSize(size);



		return convertView;
	}
}