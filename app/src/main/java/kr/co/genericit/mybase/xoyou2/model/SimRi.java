package kr.co.genericit.mybase.xoyou2.model;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;

public class SimRi implements Parcelable {
    public static Creator<ContractObj> getCreator() {
        return CREATOR;
    }
    String Phone;
    int Id;
    int No;
    String NickName;
    String Name;
    String UserInfo;
    String GwanInfo;
    String SimRiInfo;
    String Value;
    double iDou;
    boolean XO;

    public SimRi(String phone, int id, int no, String nickName, String name, String userInfo, String gwanInfo, String simRiInfo, String value, double iDou, boolean XO) {
        Phone = phone;
        Id = id;
        No = no;
        NickName = nickName;
        Name = name;
        UserInfo = userInfo;
        GwanInfo = gwanInfo;
        SimRiInfo = simRiInfo;
        Value = value;
        this.iDou = iDou;
        this.XO = XO;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getNo() {
        return No;
    }

    public void setNo(int no) {
        No = no;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUserInfo() {
        return UserInfo;
    }

    public void setUserInfo(String userInfo) {
        UserInfo = userInfo;
    }

    public String getGwanInfo() {
        return GwanInfo;
    }

    public void setGwanInfo(String gwanInfo) {
        GwanInfo = gwanInfo;
    }

    public String getSimRiInfo() {
        return SimRiInfo;
    }

    public void setSimRiInfo(String simRiInfo) {
        SimRiInfo = simRiInfo;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }

    public double getiDou() {
        return iDou;
    }

    public void setiDou(double iDou) {
        this.iDou = iDou;
    }

    public boolean isXO() {
        return XO;
    }

    public void setXO(boolean XO) {
        this.XO = XO;
    }


    @SuppressLint("NewApi")
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(Phone);
        dest.writeInt(Id);
        dest.writeInt(No);
        dest.writeString(NickName);
        dest.writeString(Name);
        dest.writeString(UserInfo);
        dest.writeString(GwanInfo);
        dest.writeString(SimRiInfo);
        dest.writeString(Value);
        dest.writeDouble(iDou);
        dest.writeBoolean(XO);
    }
    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void readFromParcel(Parcel in){

        Phone = in.readString();
        Id = in.readInt();
        No = in.readInt();
        NickName = in.readString();
        Name = in.readString();
        UserInfo = in.readString();
        GwanInfo = in.readString();
        SimRiInfo = in.readString();
        Value = in.readString();
        iDou = in.readDouble();
        XO = in.readBoolean();
    }
    @SuppressWarnings("rawtypes")
    public static final Creator<ContractObj> CREATOR = new Creator() {
        public Object createFromParcel(Parcel in) {
            return new ContractObj(in);
        }

        public Object[] newArray(int size) {
            return new ContractObj[size];
        }
    };

    @Override
    public int describeContents() {
        // TODO Auto-generated method stub
        return 0;
    }

}
