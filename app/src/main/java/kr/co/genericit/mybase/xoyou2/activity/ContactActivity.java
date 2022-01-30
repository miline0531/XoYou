package kr.co.genericit.mybase.xoyou2.activity;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.RequiresApi;

import java.util.ArrayList;

import kr.co.genericit.mybase.xoyou2.R;
import kr.co.genericit.mybase.xoyou2.adapter.ContactAdapter;
import kr.co.genericit.mybase.xoyou2.common.CommonActivity;
import kr.co.genericit.mybase.xoyou2.model.Contactobj;

public class ContactActivity extends CommonActivity {
    private ListView list;
    private ContactAdapter m_Adapter;
    private String msg;
    private EditText search_edit;
    private Boolean searchFlag = false;

    ArrayList<Contactobj> contacts_arr = new ArrayList<Contactobj>();
    ArrayList<Contactobj> contacts_arr_copy = new ArrayList<Contactobj>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);

        msg = getIntent().getStringExtra("msg");

        list = (ListView) findViewById(R.id.listView1);
        search_edit = (EditText) findViewById(R.id.search_edit);

        search_edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 입력되는 텍스트에 변화가 있을 때
                Log.e("SKY" , "onTextChanged :: " + s);
                if(s.length() == 0){
                    Log.e("SKY" , "모두나오게..");
                    m_Adapter = new ContactAdapter( ContactActivity.this, contacts_arr);
                    //list.setOnItemClickListener(mItemProjectClickListener);
                    list.setAdapter(m_Adapter);
                    searchFlag = false;
                    return;
                }
                String str = "" + s;
                contacts_arr_copy.clear();
                for (int i=0; i < contacts_arr.size(); i++){
                    if(contacts_arr.get(i).getName().matches(".*" + str + ".*")){
                        contacts_arr_copy.add(contacts_arr.get(i));
                    }
                }
                searchFlag = true;
                m_Adapter = new ContactAdapter( ContactActivity.this, contacts_arr_copy);
                //list.setOnItemClickListener(mItemProjectClickListener);
                list.setAdapter(m_Adapter);

            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // 입력이 끝났을 때

            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 입력하기 전에
            }
        });

        LoadContactsAyscn lca = new LoadContactsAyscn();
        lca.execute();

        findViewById(R.id.btn_back).setOnClickListener(btnListener);
        findViewById(R.id.btn_send).setOnClickListener(btnListener);


    }
    public void sendSmsIntent(String number){
        try{
            Uri smsUri = Uri.parse("sms:"+number);
            Intent sendIntent = new Intent(Intent.ACTION_SENDTO, smsUri);
            sendIntent.putExtra("sms_body", msg);
            startActivity(sendIntent);

//        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
//        sendIntent.putExtra("address", number);
//        sendIntent.putExtra("sms_body", editBody.getText().toString());
//        sendIntent.setType("vnd.android-dir/mms-sms");
//        startActivity(sendIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    View.OnClickListener btnListener = new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    finish();
                    break;
                case R.id.btn_send:

                    break;

            }
        }
    };
    class LoadContactsAyscn extends AsyncTask<Void, Void, ArrayList<Contactobj>> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();

            pd = ProgressDialog.show(ContactActivity.this, "Loading Contacts",
                    "Please Wait");
        }

        @Override
        protected ArrayList<Contactobj> doInBackground(Void... params) {
            // TODO Auto-generated method stub
            ArrayList<Contactobj> contacts = new ArrayList<Contactobj>();

            Cursor c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            while (c.moveToNext()) {

                @SuppressLint("Range") String contactName = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phNumber = c
                        .getString(c
                                .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                boolean falg = false;
                for (int i=0; i <contacts.size(); i++){
                    if(contactName.equals(contacts.get(i).getName())  && phNumber.equals(contacts.get(i).getNumber())){
                        falg = true;
                    }
                }
                if(!falg){
                    contacts.add(new Contactobj(
                            contactName,
                            phNumber
                    ));
                }
            }
            c.close();
            return contacts;
        }

        @Override
        protected void onPostExecute(ArrayList<Contactobj> contacts) {
            // TODO Auto-generated method stub
            super.onPostExecute(contacts);

            pd.cancel();
            contacts_arr = contacts;
            //리스트 아답터 셋팅..
            m_Adapter = new ContactAdapter( ContactActivity.this, contacts);
            list.setOnItemClickListener(mItemClickListener);
            list.setAdapter(m_Adapter);
        }

    }
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.e("SKY",  "position :: " + position);
//            if(!searchFlag){
//                //default
//                sendSmsIntent(contacts_arr.get(position).getNumber());
//            }else{
//                //search
//                sendSmsIntent(contacts_arr_copy.get(position).getNumber());
//            }
            Intent resultIntent = new Intent();
            resultIntent.putExtra("number","" + contacts_arr.get(position).getNumber());
            resultIntent.putExtra("name","" + contacts_arr.get(position).getName());
            setResult(RESULT_OK,resultIntent);
            finish();
        }
    };
}