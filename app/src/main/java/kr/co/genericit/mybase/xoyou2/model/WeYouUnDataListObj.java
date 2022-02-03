package kr.co.genericit.mybase.xoyou2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class WeYouUnDataListObj implements Parcelable {
    public static Creator<WeYouUnDataListObj> getCreator() {
        return CREATOR;
    }

    String No;
    String Name;
    String Info;
    String UnName;
    String UnSimRi;
    String Image;
    String Doui;

    public WeYouUnDataListObj(String no, String name, String info, String unName, String unSimRi, String image, String doui) {
        No = no;
        Name = name;
        Info = info;
        UnName = unName;
        UnSimRi = unSimRi;
        Image = image;
        Doui = doui;
    }

    public String getNo() {
        return No;
    }

    public void setNo(String no) {
        No = no;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getInfo() {
        return Info;
    }

    public void setInfo(String info) {
        Info = info;
    }

    public String getUnName() {
        return UnName;
    }

    public void setUnName(String unName) {
        UnName = unName;
    }

    public String getUnSimRi() {
        return UnSimRi;
    }

    public void setUnSimRi(String unSimRi) {
        UnSimRi = unSimRi;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getDoui() {
        return Doui;
    }

    public void setDoui(String doui) {
        Doui = doui;
    }

    public WeYouUnDataListObj(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(No);
        dest.writeString(Name);
        dest.writeString(Info);
        dest.writeString(UnName);
        dest.writeString(UnSimRi);
        dest.writeString(Image);
        dest.writeString(Doui);
    }
    private void readFromParcel(Parcel in){

        No = in.readString();
        Name = in.readString();
        Info = in.readString();
        UnName = in.readString();
        UnSimRi = in.readString();
        Image = in.readString();
        Doui = in.readString();
    }
    @SuppressWarnings("rawtypes")
    public static final Creator<WeYouUnDataListObj> CREATOR = new Creator() {
        public Object createFromParcel(Parcel in) {
            return new WeYouUnDataListObj(in);
        }

        public Object[] newArray(int size) {
            return new WeYouUnDataListObj[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
