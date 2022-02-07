package com.redrover.xoyou.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Contactobj implements Parcelable {
	public static Creator<Contactobj> getCreator() {
		return CREATOR;
	}

	String Name;
	String Number;
	Boolean relationFlag;

	public Contactobj(String PJT_COD, String PJT_NO, Boolean relationFlag) {
		this.Name = PJT_COD;
		this.Number = PJT_NO;
		this.relationFlag = relationFlag;
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

	public Boolean getRelationFlag() {
		return relationFlag;
	}

	public void setRelationFlag(Boolean relationFlag) {
		this.relationFlag = relationFlag;
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
