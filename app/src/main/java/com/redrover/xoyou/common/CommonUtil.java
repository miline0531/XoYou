package com.redrover.xoyou.common;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import co.kr.sky.Check_Preferences;
import com.redrover.xoyou.model.ContractObj;


public class CommonUtil {
    private static CommonUtil _instance;
    private CommonUtil dataSet = CommonUtil.getInstance();
    public Boolean LIFE_ROOM;
    public Handler LIFE_HANDLER;



    public int LOGIN_TYPE;
    public String PHONE;
    public String REG_ID;
    public String VERSION;
    public Handler ROOM_HM;

    public Boolean SYC_FLAG;
    public Boolean LIFE_LIST;
    public Activity ac;
    public static String DEVICE_SERIAL_NO;
    public static String DEVICE_ID;

    public static String Main_flag = "";
    public static String Main_bottombtn = "";

    public Double wi;
    public Double gy;
    public int tempDeviceYN;

    public Boolean qrCodeGoFlag;
    public static ArrayList<String> Place_arr = new ArrayList<String>();
    public static ArrayList<Activity> avArr = new ArrayList<Activity>();

    private static final String Email_PATTERN = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
    private static final String Passwrod_PATTERN = "^(?=.*[a-zA-Z]+)(?=.*[!@#$%^*+=-]|.*[0-9]+).{6,16}$";

    public static Typeface font = null;

    public static void setFont(Context context) {

        if (font != null) {
            return;
        }
        font = Typeface.createFromAsset(context.getAssets(), "coolvetica.ttf");
    }

    public static Typeface lsfont = null;

    public static void setLSFont(Context context) {

        if (lsfont != null) {
            return;
        }
        lsfont = Typeface.createFromAsset(context.getAssets(), "coolvetica.ttf");
    }

    static {
        _instance = new CommonUtil();
        try {
//            _instance.SERVER    = 	   		"http://ec2-13-115-119-239.ap-northeast-1.compute.amazonaws.com/BigWordEgs/";
//			_instance.Local_Path = 	   	    "/data/data/co.kr.bigwordenglish/databases";
            _instance.LOGIN_TYPE = 0;
            _instance.tempDeviceYN = 0;
            _instance.VERSION = "";
            _instance.PHONE = "";
            _instance.REG_ID = "";
            _instance.SYC_FLAG = false;
            _instance.LIFE_ROOM = false;
            _instance.LIFE_LIST = false;
            _instance.DEVICE_SERIAL_NO 		= 	   		"";
            _instance.Main_flag 		= 	   		"";
            _instance.Main_bottombtn 		= 	   		"";
            _instance.DEVICE_ID 		= 	   		"";
            _instance.qrCodeGoFlag 		= 	   		false;


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static CommonUtil getInstance() {
        return _instance;
    }

    public String JsonnotReplace(String value){
        return value.replace("[" , "").replace("]" , "");
    }
    public int GetYYYY() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(cal.YEAR);
        return year;
    }

    public int GetMM() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(cal.MONTH);
        return (month + 1);
    }

    public int GetDD() {
        Calendar cal = Calendar.getInstance();
        int day = cal.get(cal.DATE);
        return day;
    }

    public String FullPatternDate(String pattern) {
        SkyLog.e( "pattern :: " + pattern);

        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat(pattern);
        String str = dayTime.format(new Date(time));
        SkyLog.e( "str :: " + str);

        return str;
    }

    public String FullDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str = dayTime.format(new Date(time));

        return str;
    }

    public String MainDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy???MM???dd??? HH???mm???ss???");
        String str = dayTime.format(new Date(time));

        return str;
    }

    public Boolean TodayCheck(String date) {
        long time = System.currentTimeMillis();
        SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-MM-dd");
        String str = dayTime.format(new Date(time));

        if (date.equals(str)) {
            return true;
        }
        return false;
    }


    //????????? merge (??? : val1 : ????????????{0} , val2 , ???  ==> ???????????????
    public String mergeStringResource(String val1 , String val2){
        return  val1.replace("{0}" , val2);
    }

    //????????? ????????????
    public String stringResource(Activity av , String val1){
        return  Check_Preferences.getAppPreferences(av , "" + val1.toUpperCase());
    }


    public String getVersionInfo(Context context){
        String version = null;
        try {
            PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            version = i.versionName;
        } catch(PackageManager.NameNotFoundException e) { }
        return version;
    }



    @SuppressLint("MissingPermission")
    public String getDeviceId(Activity av){
        TelephonyManager telephony=(TelephonyManager)av.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = "" + telephony.getDeviceId();
        Log.e("SKY", "deviceId: " + deviceId);

        if(deviceId.equals("null")){
            final String tmDevice, tmSerial, androidId;
            tmDevice = "" + telephony.getDeviceId();
            tmSerial = "" + telephony.getSimSerialNumber();
            androidId = "" + android.provider.Settings.Secure.getString(av.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            UUID deviceUuid = new UUID(androidId.hashCode(), ((long)tmDevice.hashCode() << 32) | tmSerial.hashCode());
            deviceId = deviceUuid.toString();
        }
        Check_Preferences.setAppPreferences(av , "DEVICE_ID" , deviceId);
        return "" + deviceId;
    }

    public String getDeviceSerialNo(Activity av){
        try {
            TelephonyManager telephony=(TelephonyManager)av.getSystemService(Context.TELEPHONY_SERVICE);
            return (String) Build.MODEL;
        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }
    }





    /**
     * ?????? version ?????? ( ????????? ?????? ????????? ?????? )
     *
     * @param localVerName ????????? ??????
     * @param lastVerName  ?????? ?????? ??????
     * @return ?????? ????????? ????????? ?????? ???????????? ????????????. ( true - ????????????, false - ???????????? ?????? )
     */
    public static boolean isAppUpdate(String localVerName, String lastVerName) {
        // ???????????? ??? ?????? ????????? ?????? ??????
        String[] localVerArray = localVerName.split("\\.");
        String[] lastVerArray = lastVerName.split("[.]");

        if (localVerArray.length != 2 || lastVerArray.length != 2)
            return false;

        int[] localVerIntArray = {Integer.valueOf(localVerArray[0]), Integer.valueOf(localVerArray[1])};
        int[] lastVerIntArray = {Integer.valueOf(lastVerArray[0]), Integer.valueOf(lastVerArray[1])};

        int localVer = (localVerIntArray[0] * 1000) + (localVerIntArray[1] * 100);
        int lastVer = (lastVerIntArray[0] * 1000) + (lastVerIntArray[1] * 100);


        if (localVer < lastVer)
            return true;

        return false;
    }

    public static String getIPAddress(boolean useIPv4) {
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress()) {
                        String sAddr = addr.getHostAddress();
                        //boolean isIPv4 = InetAddressUtils.isIPv4Address(sAddr);
                        boolean isIPv4 = sAddr.indexOf(':') < 0;

                        if (useIPv4) {
                            if (isIPv4)
                                return sAddr;
                        } else {
                            if (!isIPv4) {
                                int delim = sAddr.indexOf('%'); // drop ip6 zone suffix
                                return delim < 0 ? sAddr.toUpperCase() : sAddr.substring(0, delim).toUpperCase();
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
        } // for now eat exceptions
        return "";
    }

    /**
     * Image SDCard Save (input Bitmap -> saved file JPEG)
     * Writer intruder(Kwangseob Kim)
     *
     * @param bitmap : input bitmap file
     * @param folder : input folder name
     * @param name   : output file name
     */
    public static void saveBitmaptoJpeg(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder + "/";
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException exception) {
        } catch (IOException exception) {
        }
    }

    /**
     * Image SDCard Save (input Bitmap -> saved file JPEG)
     * Writer intruder(Kwangseob Kim)
     *
     * @param bitmap : input bitmap file
     * @param folder : input folder name
     * @param name   : output file name
     */
    public static void saveBitmaptoJpeg2(Bitmap bitmap, String folder, String name) {
        String ex_storage = Environment.getExternalStorageDirectory().getAbsolutePath();
        // Get Absolute Path in External Sdcard
        String foler_name = "/" + folder;
        String file_name = name + ".jpg";
        String string_path = ex_storage + foler_name;

        File file_path;
        try {
            file_path = new File(string_path);
            if (!file_path.isDirectory()) {
                file_path.mkdirs();
            }
            FileOutputStream out = new FileOutputStream(string_path + file_name);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.close();

        } catch (FileNotFoundException exception) {
        } catch (IOException exception) {
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }



    /**
     * ???????????? ???????????? ??????
     *
     * @param email
     * @return boolean
     */
    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile(Email_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * ??????????????? ???????????? ??????
     *
     * @param passwd
     * @return boolean
     */
    public static boolean checkPasswd(String passwd) {
        Pattern pattern = Pattern.compile(Passwrod_PATTERN);
        Matcher matcher = pattern.matcher(passwd);
        return matcher.matches();
    }

    /**
     * ????????? ?????????
     *
     * @param context
     * @param et
     */
    public static void hideKeyboard(final Context context, final EditText et) {
        et.postDelayed(new Runnable() {                // ????????? ?????????
            public void run() {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
            }
        }, 0);
    }

    /**
     * ????????? ?????????
     *
     * @param context
     * @param et
     */
    public static void showKeyboard(final Context context, final EditText et) {
        et.postDelayed(new Runnable() {
            public void run() {
                InputMethodManager mgr = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                mgr.showSoftInput(et, 0);
            }
        }, 0);
    }

    /**
     * ????????? -> ?????? ???????????? ??????
     *
     * @param comma ?????????
     * @return
     */
    public static String setComma(String comma) {
        int result = Integer.parseInt(comma);
        return new DecimalFormat("#,###").format(result);
    }

//    public File getSaveFolder() {
//        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + DEFINE.PROJECTTYPE);
//        if(!dir.exists()){
//            dir.mkdirs();
//        }
//        return dir;
//    }

    /**
     * ????????? 2?????? ?????? ??????
     *
     * @param num ??????
     * @return
     */
    public static String getTwoDateFormat(int num) {
        DecimalFormat decimalFormat = new DecimalFormat("00");
        return decimalFormat.format(num);
    }

    ;


    public static String dateSubString(String date) {
        if (date.length() == 19) {
            date = date.substring(0, 10);
            return date;
        } else {
            return date;
        }
    }

    public static String getHashCodeFromString(String str) throws NoSuchAlgorithmException {
        String raw = str;
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(raw.getBytes());
        byte[] theDigest = md.digest();
        return Base64.encodeToString(theDigest, Base64.DEFAULT).replace(" " , "").replace("\n" , "");
    }

    /**
     * ?????? ????????? ????????? ????????? ??????(??? ~ ???)
     *
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String getDateDay(String date, String dateType) throws Exception {

        String day = "";

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType);
        Date nDate = dateFormat.parse(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK);

        switch (dayNum) {
            case 1:
                day = "???";
                break;
            case 2:
                day = "???";
                break;
            case 3:
                day = "???";
                break;
            case 4:
                day = "???";
                break;
            case 5:
                day = "???";
                break;
            case 6:
                day = "???";
                break;
            case 7:
                day = "???";
                break;
        }
        return day;
    }

    public String DateSecondRemove(String val) {

        String[] obj = val.split(" ");
        String[] obj2 = obj[1].split(":");

        return obj[0] + " " + obj2[0] + ":" + obj2[1];


    }

    public String TodayDateSecondRemove(String val) {

        String[] obj = val.split(" ");
        String[] obj2 = obj[1].split(":");

        return obj2[0] + ":" + obj2[1];


    }

    public static Bitmap getBitmapFromView(View view) {
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public void copyAssets(Context cv) {
        AssetManager assetManager = cv.getAssets();
        String[] files = null;
        try {
            files = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        if (files != null) for (String filename : files) {
            InputStream in = null;
            OutputStream out = null;
            Log.e("tag", "filename :: "+ filename);
            Log.e("tag", "filename :: "+ Environment.getExternalStorageDirectory().getPath());

            try {

                in = assetManager.open(filename);
                File outFile = new File(Environment.getExternalStorageDirectory().getPath(), filename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
            } catch(IOException e) {
                Log.e("tag", "Failed to copy asset file: " + filename, e);
            }
            finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        // NOOP
                    }
                }
            }
        }
    }
    private void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    /*
    * ?????? ??????????????????
    * */
    public String getLanguage(){
        String language = Locale.getDefault().getLanguage();
        return language;
    }

    /*
     * Version code ????????????
     * */
    public String GetVersion(Activity av){
        String version="";
        try {
            PackageInfo i = av.getPackageManager().getPackageInfo(av.getPackageName(), 0);
            version = i.versionName;
        } catch(Exception e){}
        return version;
    }


    public void AlertList(Context ct , final CharSequence info[] , String title, final Handler mAfterAccum , final int returnCode){
        final AlertDialog.Builder builder = new AlertDialog.Builder(ct , AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(title);
        builder.setItems(info, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Message msg2 = mAfterAccum.obtainMessage();
                msg2.obj = info[which];
                msg2.arg1 = returnCode;
                msg2.arg2 = which;
                mAfterAccum.sendMessage(msg2);
                dialog.dismiss();
            }
        });
        builder.show();

    }



    public void sqlContractInsert(Context cv , ContractObj val){
        //Log.e("SKY" , "sqlContractInsert");
        try {
            SQLiteDatabase db = cv.openOrCreateDatabase("xoyou.db", Context.MODE_PRIVATE, null);
            db.beginTransaction(); //sql?????? ???????????? ??????????????? ?????????????????? ????????????????????? ??????
            // ????????? db??? ?????? ??????
            try{
                String sql ="";
                sql = "SELECT *  FROM sms_history WHERE number = '" + val.getAddress()+ "' AND date = '" + val.getTimestamp() + "'";
                Log.e("SKY" , "sql :: "  + sql);
                // ????????? db??? ?????? ??????
                Cursor cur = db.rawQuery(sql, null);

                Log.e("SKY" , "COUNT :: "  + cur.getCount());
                //????????? ????????? INSERT
                if (cur.getCount() == 0){
                    sql ="";

                    sql += "INSERT INTO sms_history (" +
                            "key_index," +
                            "number," +
                            "date," +
                            "body," +
                            "img," +
                            "sned_recieve," +
                            "name" +
                            ") VALUES(";
                    sql += "" + val.getMessageId()+ ",";
                    sql += "'" + val.getAddress()+ "',";
                    sql += "'" + val.getTimestamp()+ "',";
                    sql += "'" + val.getBody()+ "',";
                    sql += "'" + ""+ "',";
                    sql += "'" + val.getSend_Flag()+ "',";
                    sql += "'" + val.getName()+ "')";
                    Log.e("SKY" , "sql :: "  + sql);
                    db.execSQL(sql);
                }
                cur.close();
            }catch (Exception e) {
                Log.e("SKY","onPostExecute error : "+ e.toString());
            }

            db.setTransactionSuccessful(); //?????????????????? ????????? ??????????????? SQL????????? ?????? ??????????????? ????????? ??????
            db.endTransaction(); //??????????????? ????????? ?????????.
            db.close();
        }catch (Exception e){
            Log.e("SKY","SQL_WORKER_FIRST_INSERT error : "+ e.toString());
        }

    }

    public ArrayList<ContractObj> sqlSelectContract(Context cv , String val ){
        Log.e("SKY" , "sqlSelectContract");
        ArrayList<ContractObj> arr = new ArrayList<ContractObj>();
        try{
            SQLiteDatabase db = cv.openOrCreateDatabase("xoyou.db", Context.MODE_PRIVATE, null);
            //  db?????? ????????????
            String sql;
            sql = "SELECT * FROM sms_history WHERE number = '" + val + "'";
            // ????????? db??? ?????? ??????
            Log.e("SKY" , "sql :: " + sql);

            Cursor cur = db.rawQuery(sql, null);
            while(cur.moveToNext()){

                // ????????? ??????
                @SuppressLint("Range") String key_index 			    = cur.getString( cur.getColumnIndex("key_index"));
                @SuppressLint("Range") String number 			    = cur.getString( cur.getColumnIndex("number"));
                @SuppressLint("Range") String date 			    = cur.getString( cur.getColumnIndex("date"));
                @SuppressLint("Range") String body 			    = cur.getString( cur.getColumnIndex("body"));
                @SuppressLint("Range") String img 			    = cur.getString( cur.getColumnIndex("img"));
                @SuppressLint("Range") String sned_recieve 			    = cur.getString( cur.getColumnIndex("sned_recieve"));
                @SuppressLint("Range") String name 			    = cur.getString( cur.getColumnIndex("name"));
                @SuppressLint("Range") String suggestion 			    = cur.getString( cur.getColumnIndex("suggestion"));

                @SuppressLint("Range") String po0Val 			    = cur.getString( cur.getColumnIndex("po0Val"));
                @SuppressLint("Range") String po1Val 			    = cur.getString( cur.getColumnIndex("po1Val"));
                @SuppressLint("Range") String po2Val 			    = cur.getString( cur.getColumnIndex("po2Val"));
                @SuppressLint("Range") String po3Val 			    = cur.getString( cur.getColumnIndex("po3Val"));
                @SuppressLint("Range") String message 			    = cur.getString( cur.getColumnIndex("message"));

//                Log.e("SKY" , "sql :: ==================Strat");
//                Log.e("SKY" , "sql :: =========key_index :: " + key_index);
//                Log.e("SKY" , "sql :: =========number :: " + number);
//                Log.e("SKY" , "sql :: =========date :: " + date);
//                Log.e("SKY" , "sql :: =========body :: " + body);
//                Log.e("SKY" , "sql :: =========img :: " + img);
//                Log.e("SKY" , "sql :: =========sned_recieve :: " + sned_recieve);
//                Log.e("SKY" , "sql :: =========name :: " + name);
//                Log.e("SKY" , "sql :: =========suggestion :: " + suggestion);
//                Log.e("SKY" , "sql :: =========po0Val :: " + po0Val);
//                Log.e("SKY" , "sql :: =========po1Val :: " + po1Val);
//                Log.e("SKY" , "sql :: =========po2Val :: " + po2Val);
//                Log.e("SKY" , "sql :: =========po3Val :: " + po3Val);
//                Log.e("SKY" , "sql :: =========message :: " + message);
//
//                Log.e("SKY" , "sql :: ==================End\n\n");



                arr.add(new ContractObj(key_index ,
                        "" ,
                        number ,
                        "" ,
                        date ,
                        body  ,
                        sned_recieve ,
                        name ,
                        suggestion,
                        po0Val,
                        po1Val,
                        po2Val,
                        po3Val,
                        message));

            }
            cur.close();
            db.close();
        }catch (Exception e) {
            Log.e("SKY","onPostExecute error : "+ e.toString());
        }

        return arr;
    }

    public void sqlContractUpdate(Context cv , ContractObj val , String suggestion
            , String po0Val
            , String po1Val
            , String po2Val
            , String po3Val
            , String message){
        Log.e("SKY" , "sqlContractUpdate");
        try {
            SQLiteDatabase db = cv.openOrCreateDatabase("xoyou.db", Context.MODE_PRIVATE, null);
            db.beginTransaction(); //sql?????? ???????????? ??????????????? ?????????????????? ????????????????????? ??????
            // ????????? db??? ?????? ??????
            try{
                String sql ="";
                Log.e("SKY" , "sql :: "  + sql);
                // ????????? db??? ?????? ??????

                sql = "UPDATE sms_history SET ";
                sql += "suggestion=";
                sql += "'" + suggestion  + "'," ;
                sql += "po0Val=";
                sql += "'" + po0Val  + "'," ;
                sql += "po1Val=";
                sql += "'" + po1Val  + "'," ;
                sql += "po2Val=";
                sql += "'" + po2Val  + "'," ;
                sql += "po3Val=";
                sql += "'" + po3Val  + "'," ;
                sql += "message=";
                sql += "'" + message  + "'" ;

                sql += " WHERE number=";
                sql += "'" + val.getAddress()  + "' AND date = " ;
                sql += "'" + val.getTimestamp()  + "'" ;

                Log.e("SKY","sql  : "+ sql);
                db.execSQL(sql);
            }catch (Exception e) {
                Log.e("SKY","onPostExecute error : "+ e.toString());
            }

            db.setTransactionSuccessful(); //?????????????????? ????????? ??????????????? SQL????????? ?????? ??????????????? ????????? ??????
            db.endTransaction(); //??????????????? ????????? ?????????.
            db.close();
        }catch (Exception e){
            Log.e("SKY","SQL_WORKER_FIRST_INSERT error : "+ e.toString());
        }

    }

    //????????? flag = 0 , ????????? flag = 1
    public int readSMSMessage(Context mContext, String[] arrVal , String name) {
        //?????? ?????????!
        Uri allMessage = Uri.parse("content://sms/inbox/");


        ContentResolver cr = mContext.getContentResolver();
        Cursor c = cr.query(allMessage,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                "address=?", arrVal,
                "date DESC");

        while (c.moveToNext()) {
            com.redrover.xoyou.model.Message msg = new com.redrover.xoyou.model.Message(); // ?????? ?????? ???????????? ???????????? ??????????????? ????????????.

            long messageId = c.getLong(0);
            msg.setMessageId(String.valueOf(messageId));

            long threadId = c.getLong(1);
            msg.setThreadId(String.valueOf(threadId));

            String address = c.getString(2);
            msg.setAddress(address);

            long contactId = c.getLong(3);
            msg.setContactId(String.valueOf(contactId));

            String contactId_string = String.valueOf(contactId);
            msg.setContactId_string(contactId_string);

            long timestamp = c.getLong(4);
            msg.setTimestamp(String.valueOf(timestamp));

            String body = c.getString(5);
            msg.setBody(body);

//            SkyLog.d("==============SMS==============");
//            SkyLog.d("messageId :: " + messageId);
//            SkyLog.d("threadId :: " + threadId);
//            SkyLog.d("address :: " + address);
//            SkyLog.d("contactId_string :: " + contactId_string);
//            SkyLog.d("timestamp :: " + timestamp);
//            SkyLog.d("body :: " + body);

            ContractObj obj = new ContractObj("" + messageId ,
                    "" +threadId ,
                    address ,
                    contactId_string ,
                    "" +timestamp ,
                    body ,
                    "0" ,
                    name ,
                    "" ,
                    "" ,
                    "" ,
                    "" ,
                    "" ,
                    "");
            sqlContractInsert(mContext , obj);

            //SkyLog.d("==============SMS==============");


            //arrayList.add(msg); //???????????? ?????? arraylist??? ???????????? ??????????????? ????????????????????? ?????????????????????.

        }

        //?????? ?????????!
        Uri allMessage2 = Uri.parse("content://sms/sent/");
        Cursor c2 = cr.query(allMessage2,
                new String[]{"_id", "thread_id", "address", "person", "date", "body"},
                "address=?", arrVal,
                "date DESC");

        while (c2.moveToNext()) {
            com.redrover.xoyou.model.Message msg = new com.redrover.xoyou.model.Message(); // ?????? ?????? ???????????? ???????????? ??????????????? ????????????.

            long messageId = c2.getLong(0);
            msg.setMessageId(String.valueOf(messageId));

            long threadId = c2.getLong(1);
            msg.setThreadId(String.valueOf(threadId));

            String address = c2.getString(2);
            msg.setAddress(address);

            long contactId = c2.getLong(3);
            msg.setContactId(String.valueOf(contactId));

            String contactId_string = String.valueOf(contactId);
            msg.setContactId_string(contactId_string);

            long timestamp = c2.getLong(4);
            msg.setTimestamp(String.valueOf(timestamp));

            String body = c2.getString(5);
            msg.setBody(body);

//            SkyLog.d("==============SMS==============");
//            SkyLog.d("messageId :: " + messageId);
//            SkyLog.d("threadId :: " + threadId);
//            SkyLog.d("address :: " + address);
//            SkyLog.d("contactId_string :: " + contactId_string);
//            SkyLog.d("timestamp :: " + timestamp);
//            SkyLog.d("body :: " + body);

            ContractObj obj = new ContractObj("" + messageId ,
                    "",
                    address ,
                    contactId_string ,
                    "" +timestamp ,
                    body ,
                    "1" ,
                    name ,
                    "" ,
                    "" ,
                    "" ,
                    "" ,
                    "" ,
                    "");
            sqlContractInsert(mContext , obj);

            //SkyLog.d("==============SMS==============");


            //arrayList.add(msg); //???????????? ?????? arraylist??? ???????????? ??????????????? ????????????????????? ?????????????????????.

        }
        return 0;
    }

    public void sqlPhoneInsert(Context cv , String val){
        Log.e("SKY" , "sqlPhoneInsert");
        try {
            SQLiteDatabase db = cv.openOrCreateDatabase("xoyou.db", Context.MODE_PRIVATE, null);
            db.beginTransaction(); //sql?????? ???????????? ??????????????? ?????????????????? ????????????????????? ??????
            // ????????? db??? ?????? ??????
            try{
                String sql ="";
                sql = "SELECT *  FROM save_phone WHERE phone = '" + val + "'";
                Log.e("SKY" , "sql :: "  + sql);
                // ????????? db??? ?????? ??????
                Cursor cur = db.rawQuery(sql, null);

                Log.e("SKY" , "COUNT :: "  + cur.getCount());
                //????????? ????????? INSERT
                if (cur.getCount() == 0){
                    sql ="";

                    sql += "INSERT INTO save_phone (" +
                            "phone" +
                            ") VALUES(";
                    sql += "'" + val+ "')";
                    Log.e("SKY" , "sql :: "  + sql);
                    db.execSQL(sql);
                }
                cur.close();
            }catch (Exception e) {
                Log.e("SKY","onPostExecute error : "+ e.toString());
            }

            db.setTransactionSuccessful(); //?????????????????? ????????? ??????????????? SQL????????? ?????? ??????????????? ????????? ??????
            db.endTransaction(); //??????????????? ????????? ?????????.
            db.close();
        }catch (Exception e){
            Log.e("SKY","SQL_WORKER_FIRST_INSERT error : "+ e.toString());
        }

    }

    public Boolean sqlSelectAllPhone(Context cv , String phone){
        Log.e("SKY" , "sqlSelectAllPhone");
        phone = phone.replace("-" , "");
        try{
            SQLiteDatabase db = cv.openOrCreateDatabase("xoyou.db", Context.MODE_PRIVATE, null);
            //  db?????? ????????????
            String sql;
            sql = "SELECT phone FROM save_phone where phone = '" + phone + "'";
            // ????????? db??? ?????? ??????
            Log.e("SKY" , "sql :: " + sql);

            Cursor cur = db.rawQuery(sql, null);
            while(cur.moveToNext()){

                // ????????? ??????
                @SuppressLint("Range") String key_index 			    = cur.getString( cur.getColumnIndex("key_index"));
                @SuppressLint("Range") String number 			    = cur.getString( cur.getColumnIndex("number"));
                @SuppressLint("Range") String date 			    = cur.getString( cur.getColumnIndex("date"));
                @SuppressLint("Range") String body 			    = cur.getString( cur.getColumnIndex("body"));
                @SuppressLint("Range") String img 			    = cur.getString( cur.getColumnIndex("img"));
                @SuppressLint("Range") String sned_recieve 			    = cur.getString( cur.getColumnIndex("sned_recieve"));
                @SuppressLint("Range") String name 			    = cur.getString( cur.getColumnIndex("name"));
                @SuppressLint("Range") String suggestion 			    = cur.getString( cur.getColumnIndex("suggestion"));

                @SuppressLint("Range") String po0Val 			    = cur.getString( cur.getColumnIndex("po0Val"));
                @SuppressLint("Range") String po1Val 			    = cur.getString( cur.getColumnIndex("po1Val"));
                @SuppressLint("Range") String po2Val 			    = cur.getString( cur.getColumnIndex("po2Val"));
                @SuppressLint("Range") String po3Val 			    = cur.getString( cur.getColumnIndex("po3Val"));
                @SuppressLint("Range") String message 			    = cur.getString( cur.getColumnIndex("message"));
                SkyLog.d("?????? ??????!!!!=============");
                return true;

            }
            cur.close();
            db.close();
        }catch (Exception e) {
            Log.e("SKY","onPostExecute error : "+ e.toString());
        }
        SkyLog.d("?????? XXXXXXXXX!!!!=============");
        return false;
    }
}
