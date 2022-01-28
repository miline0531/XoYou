package kr.co.genericit.mybase.xoyou2.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.interfaces.DialogClickListener;
import kr.co.genericit.mybase.xoyou2.network.action.ActionRuler;
import kr.co.genericit.mybase.xoyou2.network.interfaces.ActionResultListener;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestAddLocation;
import kr.co.genericit.mybase.xoyou2.network.request.ActionRequestUpdateLocation;
import kr.co.genericit.mybase.xoyou2.network.response.AddLocationResult;
import kr.co.genericit.mybase.xoyou2.network.response.UpdateLocationResult;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;
import kr.co.genericit.mybase.xoyou2.utils.CommandUtil;
import kr.co.genericit.mybase.xoyou2.view.CommonPopupDialog;


public class AddLocationActivity extends CommonActivity implements View.OnClickListener {

    private int updateLocation = 0;
    private String mLocationSelectStr="",requestRelation,requestName,requestAddr, requestFloor,
            in_seq,user_id;
    private RelativeLayout btn_location_relation;
    private EditText edt_location_relation, edt_location_name, edt_addr;
    private JWSharePreference jwSharePreference;
    private TextView btn_minus, btn_plus, txt_value, btn_space, txt_tooltip;
    private CheckBox agreeCkb;
    private int mFloorCount = 0,seq = -1;
    private String lat,lng;

    private TextView btn_location_regist, btn_cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting_location);
        super.onCreate(savedInstanceState);
        jwSharePreference = new JWSharePreference();

        initView();

        in_seq = jwSharePreference.getString(JWSharePreference.PREFERENCE_USER_SEQ,"");
        user_id = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_ID,"");

    }

    public void initView(){
        btn_location_relation = findViewById(R.id.btn_location_relation);
        edt_location_name = findViewById(R.id.edt_location_name);
        edt_location_relation = findViewById(R.id.edt_location_relation);
        edt_addr = findViewById(R.id.edt_addr);
        btn_minus = findViewById(R.id.btn_minus);
        btn_plus = findViewById(R.id.btn_plus);
        txt_value = findViewById(R.id.txt_value);
        btn_space = findViewById(R.id.btn_space);
        btn_location_regist = findViewById(R.id.btn_location_regist);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_minus.setOnClickListener(this);
        btn_plus.setOnClickListener(this);
        txt_tooltip = findViewById(R.id.txt_tooltip);
        String addLocationTooltip = getResources().getString(R.string.add_location_tooltip);
        String name = jwSharePreference.getString(JWSharePreference.PREFERENCE_LOGIN_NAME,"");

        txt_tooltip.setText(addLocationTooltip.replace("_UserName",name));
        btn_location_regist.setOnClickListener(this);
        (findViewById(R.id.btn_list)).setOnClickListener(this);

        btn_cancel.setOnClickListener(this);
        btn_space.setOnClickListener(this);


        seq = getIntent().getIntExtra("seq",-1);

        if(seq!=-1){
            requestRelation = getIntent().getStringExtra("gubun");
            requestName = getIntent().getStringExtra("name");
            requestAddr = getIntent().getStringExtra("addr");
            requestFloor = getIntent().getStringExtra("floor");
            lat = getIntent().getStringExtra("lat");
            lng = getIntent().getStringExtra("lng");

            edt_location_relation.setText(requestRelation);
            edt_location_name.setText(requestName);
            edt_addr.setText(requestAddr);
            String floorValue = requestFloor.replace(CommandUtil.getInstance().getStr(R.string.add_location_floor),"");
            txt_value.setText(floorValue);

            mFloorCount = Integer.parseInt(floorValue);

            ((TextView)findViewById(R.id.activity_title)).setText(CommandUtil.getInstance().getStr(R.string.add_location_change));
            btn_location_regist.setText(CommandUtil.getInstance().getStr(R.string.add_location_change_btn));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){

            case R.id.btn_minus:
                if(mFloorCount > 0){
                    mFloorCount -= 1;
                }
                setFloorValue();
                break;

            case R.id.btn_plus:
                mFloorCount += 1;
                setFloorValue();
                break;

            case R.id.btn_location_regist:
                if(checkValid()){
                    if(lat!=null){
                        if(seq == -1){
                            requestLocationRegist();

                        }else{
                            requestLocationUpdate();
                        }
                    }else{
                        Toast.makeText(this,CommandUtil.getInstance().getStr(R.string.add_location_input_addr),Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this,CommandUtil.getInstance().getStr(R.string.add_location_input_essential),Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_list:
                Intent i = new Intent(this,LocationListActivity.class);
                startActivity(i);

                break;
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_space:
                i = new Intent(this,WebViewActivity.class);
                startActivityForResult(i,200);

                break;

        }
    }

    private boolean checkValid(){
        boolean valid = true;

        String requestRelation = edt_location_relation.getText().toString();
        String requestName = edt_location_name.getText().toString();
        String requestAddr = edt_addr.getText().toString();
        String requestFloor = txt_value.getText().toString();

        if(requestRelation.equals("") || requestName.equals("") || requestAddr.equals("") || requestFloor.equals("")){
            valid = false;
        }

        return valid;
    }

    private void setFloorValue(){
        String CountStr = "";
        if(mFloorCount <10){
            CountStr = "0";
        }

        CountStr += mFloorCount;
        txt_value.setText(CountStr);
    }

    private void requestLocationUpdate(){
        requestRelation = edt_location_relation.getText().toString();
        requestName = edt_location_name.getText().toString();
        requestAddr = edt_addr.getText().toString();
        requestFloor = txt_value.getText().toString();

        ActionRuler.getInstance().addAction(new ActionRequestUpdateLocation(this,String.valueOf(seq),user_id,requestRelation,"주거용",requestName,"12345",
                requestAddr,requestAddr,lat,lng,requestFloor, new ActionResultListener<UpdateLocationResult>() {
            @Override
            public void onResponseResult(UpdateLocationResult response) {
                try {
                    UpdateLocationResult result = response;

                    String message = "";
                    if(result.isSuccess()){
                        //성공
                        message = CommandUtil.getInstance().getStr(R.string.add_location_change_success);

                    }else{
                        message = CommandUtil.getInstance().getStr(R.string.add_location_change_fail);
                    }

                    CommandUtil.getInstance().showCommonOneButtonDialog(AddLocationActivity.this, message, CommandUtil.getInstance().getStr(R.string.str_cofirm), CommonPopupDialog.COMMON_DIALOG_OPTION_CLOSE_DIALOG, new DialogClickListener() {
                        @Override
                        public void onClick(int button) {
                            Intent i = new Intent(AddLocationActivity.this, LocationListActivity.class);
                            startActivity(i);
                            finish();
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(AddLocationActivity.this,CommandUtil.getInstance().getStr(R.string.add_location_change_fail),Toast.LENGTH_SHORT).show();
            }
        }));
        ActionRuler.getInstance().runNext();
    }
    private void requestLocationRegist(){
        requestRelation = edt_location_relation.getText().toString();
        requestName = edt_location_name.getText().toString();
        requestAddr = edt_addr.getText().toString();
        requestFloor = txt_value.getText().toString();


        ActionRuler.getInstance().addAction(new ActionRequestAddLocation(this,in_seq,user_id,requestRelation,"주거용",requestName,"12345",
                requestAddr,requestAddr,lat,lng,requestFloor, new ActionResultListener<AddLocationResult>() {
            @Override
            public void onResponseResult(AddLocationResult response) {
                try {
                    AddLocationResult result = response;

                    if(result.isSuccess()){
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.add_location_reg_success),Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(getApplicationContext(),CommandUtil.getInstance().getStr(R.string.add_location_reg_fail),Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onResponseError(String message) {
                Toast.makeText(AddLocationActivity.this,CommandUtil.getInstance().getStr(R.string.add_location_reg_fail),Toast.LENGTH_SHORT).show();
            }
        }));
        ActionRuler.getInstance().runNext();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK&&requestCode==200){
            if(data!=null){
                String arg = data.getStringExtra("arg");
                String[] tmp = arg.split(";");
                String sjuso = tmp[2];
                lat = tmp[1];
                lng = tmp[0];

                edt_addr.setText(sjuso);
            }
        }
    }
}