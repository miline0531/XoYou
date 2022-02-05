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
import kr.co.genericit.mybase.xoyou2.common.SkyLog;
import kr.co.genericit.mybase.xoyou2.model.Contactobj;
import kr.co.genericit.mybase.xoyou2.popup.ContractInsertPopUp;
import kr.co.genericit.mybase.xoyou2.popup.Fragment2_PopUp1;

public class ContactActivity extends CommonActivity {
    private ListView list;
    private ContactAdapter m_Adapter;
    private EditText search_edit;
    private Boolean searchFlag = false;

    ArrayList<Contactobj> contacts_arr = new ArrayList<Contactobj>();
    ArrayList<Contactobj> contacts_arr_copy = new ArrayList<Contactobj>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_activity);


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
                    }else if(contacts_arr.get(i).getNumber().matches(".*" + str + ".*")){
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

            Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
            while (c.moveToNext()) {
                @SuppressLint("Range") String contactName = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                @SuppressLint("Range") String phNumber = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                boolean falg = false;
                for (int i=0; i <contacts.size(); i++){
                    if(contactName.equals(contacts.get(i).getName())  && phNumber.equals(contacts.get(i).getNumber())){
                        falg = true;
                    }
                }
                if(!falg){
//                    phNumber = phNumbe r.replace("+82" , "0");
//                    phNumber = phNumber.replace(" " , "");
                    phNumber = phNumber.replace("-" , "");
//                    phNumber = phNumber.replace("010010" , "010");
                    contacts.add(new Contactobj(contactName, phNumber , false));
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

            //관계인으로 등록 되어있는지 체크
            for (int i=0; i <contacts_arr.size(); i++){
                boolean relationFlag = false;

                String phone = contacts_arr.get(i).getNumber().replace("+82" , "0");
                phone = phone.replace(" " , "");
                phone = phone.replace("-" , "");
                phone = phone.replace("010010" , "010");

                //SkyLog.d("phone :: " + phone);

                for (int j=0; j <RelationListActivity.relationDataList.size(); j++){
                    //SkyLog.d("getCALL_NUMBER :: " + RelationListActivity.relationDataList.get(j).getCALL_NUMBER());
                    if(RelationListActivity.relationDataList.get(j).getCALL_NUMBER().equals(phone)){
                        relationFlag = true;
                    }
                }
                if(relationFlag){
                    //SkyLog.d("phone :: " + phone);
                    //SkyLog.d("getName :: " + contacts_arr.get(i).getName());
                }
                contacts_arr.get(i).setRelationFlag(relationFlag);
            }



            //리스트 아답터 셋팅..
            m_Adapter = new ContactAdapter( ContactActivity.this, contacts);
            list.setOnItemClickListener(mItemClickListener);
            list.setAdapter(m_Adapter);
        }

    }
    AdapterView.OnItemClickListener mItemClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            Log.e("SKY",  "position :: " + position);

            Intent it = new Intent(ContactActivity.this , ContractInsertPopUp.class);
            it.putExtra("obj",contacts_arr.get(position));
            startActivity(it);


//            Intent resultIntent = new Intent();
//            resultIntent.putExtra("number","" + contacts_arr.get(position).getNumber());
//            resultIntent.putExtra("name","" + contacts_arr.get(position).getName());
//            setResult(RESULT_OK,resultIntent);
//            finish();
        }
    };
}