package kr.co.genericit.mybase.xoyou2.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contactobj implements Parcelable {
	public static Creator<Contactobj> getCreator() {
		return CREATOR;
	}

	String Name;
	String Number;

	public Contactobj(String PJT_COD, String PJT_NO) {
		this.Name = PJT_COD;
		this.Number = PJT_NO;
	}


	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getNumber() {
		return Number;
	}

	public void setNumber(String number) {
		Number = number;
	}

	public Contactobj(Parcel in) {
		readFromParcel(in);
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(Name);
		dest.writeString(Number);

	}
	private void readFromParcel(Parcel in){
		Name = in.readString();
		Number = in.readString();
	}
	public static final Creator<Contactobj> CREATOR = new Creator() {
		public Object createFromParcel(Parcel in) {
			return new Contactobj(in);
		}
		public Object[] newArray(int size) {
			return new Contactobj[size];
		}
	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
}
