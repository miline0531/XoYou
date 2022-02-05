package kr.co.genericit.mybase.xoyou2.common;


import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;

import java.util.HashMap;
import java.util.Map;

import co.kr.sky.AccumThread;
import kr.co.genericit.mybase.xoyou2.activity.MainActivity;
import kr.co.genericit.mybase.xoyou2.model.ContractObj;
import kr.co.genericit.mybase.xoyou2.storage.JWSharePreference;

public class ChatAsyncTask extends AsyncTask<String,Void,String> {

    private Handler hm;
    private ContractObj obj;
    private int position;
    private AccumThread mThread;
    private Map<String, String> map = new HashMap<String, String>();

    public ChatAsyncTask(Handler hm, ContractObj obj , int position){
        this.hm = hm;
        this.obj = obj;
        this.position = position;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        if(obj.getSuggestion() != null){
            SkyLog.d("doInBackground getSuggestion:: " + obj.getSuggestion());
            Message msg2 = hm.obtainMessage();
            //msg2.obj = this.HttpPostConnection(this.map);
            msg2.arg1 = position;
            hm.sendMessage(msg2);
        }else{
            //NMR 수정해야할 부분!!!
            map.clear();
            map.put("url", NetInfo.SERVER_BASE_URL + NetInfo.API_SELECT_SIMRI_MESSAGE);
            map.put("userId", new JWSharePreference().getString(JWSharePreference.PREFERENCE_LOGIN_ID,""));
//            map.put("userId", "ifeelbluu12");
//            map.put("callNumber", "01012345678");
        map.put("callNumber", obj.getAddress());
            //map.put("message", "좋은 아침");
            map.put("message", obj.getBody());

            //스레드 생성
            mThread = new AccumThread(MainActivity.mainAc, hm, map, 5, position, null);
            mThread.start();        //스레드 시작!!
        }

        return "" + position;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        SkyLog.d("onPostExecute" + s);
    }
}

