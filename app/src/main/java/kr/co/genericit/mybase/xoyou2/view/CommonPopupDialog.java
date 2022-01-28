package kr.co.genericit.mybase.xoyou2.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;

/**
 * 공통 팝업 다이얼로그 Class
 */
public class CommonPopupDialog implements View.OnClickListener {

    // 다이얼로그 옵션[Start]
    public static final int COMMON_DIALOG_OPTION_ERROR = 0;
    public static final int COMMON_DIALOG_OPTION_CLOSE_APP = 1;
    public static final int COMMON_DIALOG_OPTION_CLOSE_DIALOG = 2;
    public static final int COMMON_DIALOG_OPTION_INSTALL_APP = 3;
    public static final int COMMON_DIALOG_OPTION_CLOSE_ACTIVITY = 4; // 해당 Activity를 종료하기 위한 옵션
    // 다이얼로그 옵션[End]

    // 다이얼로그 버튼 종류[Start]
    public static final int COMMON_DIALOG_LEFT_BUTTON = 1;
    public static final int COMMON_DIALOG_RIGHT_BUTTON = 2;
    // 다이얼로그 버튼 종류[End]

    // Dialog Button Click Listener
    private DialogClickListener mDialogClickListener;
    private RelativeLayout rlRoot;
    private ImageView mCloseImageView;

    private TextView mCommentTextView;
    private TextView mLeftButtonTextView;
    private TextView mRightButtonTextView;

    private String mCommentString;
    private String mLeftButtonString;
    private String mRightButtonString;

    private LinearLayout mLeftButtonLinearLayout;
    private LinearLayout mRightButtonLinearLayout;

    private Context mContext;

    private Window mWindow;

    private String SPLASH_ACTIVITY = "SplashActivity";
    private String MAIN_ACTIVITY = "MainActivity";

    private int mDlgCloseOption = COMMON_DIALOG_OPTION_CLOSE_DIALOG;
    private int mDlgCloseOptionTwo = COMMON_DIALOG_OPTION_CLOSE_DIALOG;

    private AlertDialog dialog;

    public void dismiss() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            if (dialog != null) {
                dialog.dismiss();
            }
        }
    }

    public void show() {
        if (mContext != null && !((Activity) mContext).isFinishing()) {
            if (dialog != null && !dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    public void setCanceledOnTouchOutside(boolean cancel) {
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(cancel);
        }
    }

    public void setCancelable(boolean cancel) {
        if (dialog != null) {
            dialog.setCancelable(cancel);
        }
    }

    public CommonPopupDialog(Context context, String message, String leftButtonString, int leftButtonOption, DialogClickListener dialogClickListener) {
        mContext = context;
        mCommentString = message;
        // 최근 UI 변경에 따라 좌우 내용 변경.
        mLeftButtonString = "";
        mRightButtonString = leftButtonString;
//        mLeftButtonString = leftButtonString;
//        mRightButtonString = "";

        mDlgCloseOptionTwo = leftButtonOption;
        mDialogClickListener = dialogClickListener;
        onCreate();
    }

    public CommonPopupDialog(Context context, String message, String leftButtonText, String rightButtonString, int leftButtonOption, int rightButtonOption, DialogClickListener dialogClickListener) {
        mContext = context;
        mCommentString = message;
        mLeftButtonString = leftButtonText;
        mRightButtonString = rightButtonString;
        mDlgCloseOption = leftButtonOption;
        mDlgCloseOptionTwo = rightButtonOption;
        mDialogClickListener = dialogClickListener;
        onCreate();
    }

    public void onCreate() {
        Activity mActivity = (Activity) mContext;
        LayoutInflater factory = LayoutInflater.from(mActivity);
        View view = factory.inflate(R.layout.view_common_dialog, null);
        dialog = new AlertDialog.Builder(mActivity).create();

        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }

            dialog.setView(view);
            mWindow = dialog.getWindow();
            mWindow.setGravity(Gravity.CENTER);
        }

        rlRoot = view.findViewById(R.id.rl_root);
        mCloseImageView = view.findViewById(R.id.close_image_view);
        mCommentTextView = view.findViewById(R.id.comment_text_view);
        mLeftButtonTextView = view.findViewById(R.id.left_button_text_view);
        mRightButtonTextView = view.findViewById(R.id.right_button_text_view);
        mLeftButtonLinearLayout = view.findViewById(R.id.left_button_linear_layout);
        mRightButtonLinearLayout = view.findViewById(R.id.right_button_linear_layout);

        // 다이얼로그 가로 사이즈 맞추기
        rlRoot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int width = +mContext.getResources().getDimensionPixelSize(R.dimen.common_dialog_width_300);
                mWindow.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
                rlRoot.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        mCloseImageView.setOnClickListener(this);
        mLeftButtonLinearLayout.setOnClickListener(this);
        mRightButtonLinearLayout.setOnClickListener(this);

        initLayout();
    }

    private void initLayout() {
        if (!TextUtils.isEmpty(mCommentString)) {
            mCommentTextView.setText(Html.fromHtml(mCommentString.replace("\n", "<br>")));
        }

        mLeftButtonTextView.setText(mLeftButtonString);

        if (TextUtils.isEmpty(mLeftButtonString)) {
            mLeftButtonTextView.setVisibility(View.GONE);
        } else {
            mLeftButtonTextView.setVisibility(View.VISIBLE);
        }

        mRightButtonTextView.setText(mRightButtonString);
        if (TextUtils.isEmpty(mRightButtonString)) {
            mRightButtonTextView.setVisibility(View.GONE);
        } else {
            mRightButtonTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left_button_linear_layout:
                clickLeftButton();
                break;
            case R.id.right_button_linear_layout:
                clickRightButton();
                break;
            // X 버튼 누르면 닫히게 처리.
            case R.id.close_image_view:
                dismiss();
                break;
        }
    }

    private void clickLeftButton() {
        if (mDialogClickListener != null) {
            mDialogClickListener.onClick(COMMON_DIALOG_LEFT_BUTTON);
        }

        switch (mDlgCloseOption) {
            case COMMON_DIALOG_OPTION_ERROR:
                dismiss();
                break;
            case COMMON_DIALOG_OPTION_CLOSE_APP:

//                CommandUtil.getInstance().finishApplication((Activity) mContext);
                break;
            case COMMON_DIALOG_OPTION_CLOSE_ACTIVITY:
                ((Activity) mContext).finish();
                dismiss();
                break;
            default:
                dismiss();
                break;
        }
    }

    private void clickRightButton() {
        if (mDialogClickListener != null) {
            mDialogClickListener.onClick(COMMON_DIALOG_RIGHT_BUTTON);
        }
        switch (mDlgCloseOptionTwo) {
            case COMMON_DIALOG_OPTION_ERROR:
                dismiss();
                break;
            case COMMON_DIALOG_OPTION_CLOSE_APP:
                String currentClassName = ((Activity) mContext).getLocalClassName();
                if(currentClassName.contains(SPLASH_ACTIVITY)){
//                    CommandUtil.getInstance().finishApplication((Activity) mContext);
                }else if(currentClassName.contains(MAIN_ACTIVITY)){
                    dismiss();
                }else{
                    Activity curActivity = ((Activity) mContext);
                    if(curActivity!= null)
                        ((Activity) mContext).finish();
                }
                break;
            default:
                dismiss();
                break;
        }
    }
}
