package kr.co.genericit.mybase.xoyou2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WeListObj implements Parcelable {
    public static Creator<WeListObj> getCreator() {
        return CREATOR;
    }


    String No;
    String UnName;
    String MaxName;
    String MinName;

    public WeListObj(String no, String unName, String maxName, String minName) {
        No = no;
        UnName = unName;
        MaxName = maxName;
        MinName = minName;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getUnName() {
        return UnName;
    }

    public void setUnName(String unName) {
        UnName = unName;
    }

    public String getMaxName() {
        return MaxName;
    }

    public void setMaxName(String maxName) {
        MaxName = maxName;
    }

    public String getMinName() {
        return MinName;
    }

    public void setMinName(String minName) {
        MinName = minName;
    }


    public WeListObj(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(No);
        dest.writeString(UnName);
        dest.writeString(MaxName);
        dest.writeString(MinName);
    }
    private void readFromParcel(Parcel in){

        No = in.readString();
        UnName = in.readString();
        MaxName = in.readString();
        MinName = in.readString();
    }
    @SuppressWarnings("rawtypes")
    public static final Creator<WeListObj> CREATOR = new Creator() {
        public Object createFromParcel(Parcel in) {
            return new WeListObj(in);
        }

        public Object[] newArray(int size) {
            return new WeListObj[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
