package kr.co.genericit.mybase.xoyou2.view;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import kr.co.genericit.mybase.xoyou2.R;

public class CardEditMessageDialog extends Dialog {
	private final OnSelectListener OnSelectListener;


	public CardEditMessageDialog(Context context, String title, String message, int layoutType, OnSelectListener listener) {
		super(context);
		this.OnSelectListener = listener;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_dialog_notice);

		((TextView)findViewById(R.id.notice_title)).setText(title);
//		((TextView)findViewById(R.id.notice_message)).setText(message);

		SetOnClickEvent(((Button)findViewById(R.id.btn_notice_ok)),1);
		SetOnClickEvent(((Button)findViewById(R.id.btn_notice_no)),2);

		if(layoutType == 2){
			((Button)findViewById(R.id.btn_notice_no)).setVisibility(View.GONE);
		}
	}

	public void SetOnClickEvent(final View v,final int type){
		View.OnClickListener addclick = new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				OnSelectListener.onSelected(type);
			}
		};
		v.setOnClickListener(addclick);
	}
	
	
	public interface OnSelectListener {
		public void onSelected(int type);
	}
	
}
