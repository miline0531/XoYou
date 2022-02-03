package kr.co.genericit.mybase.xoyou2.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import kr.co.genericit.mybase.xoyou2.common.SkyLog;

public class ContractObj implements Parcelable {
    public static Creator<ContractObj> getCreator() {
        return CREATOR;
    }
    String messageId;
    String threadId;
    String address;
    String contactId_string;
    String timestamp;
    String body;
    String send_Flag;
    String name;

    public ContractObj(String messageId, String threadId, String address, String contactId_string, String timestamp, String body, String send_Flag, String name) {
        this.messageId = messageId;
        this.threadId = threadId;
        this.address = address;
        this.contactId_string = contactId_string;
        this.timestamp = timestamp;
        this.body = body;
        this.send_Flag = send_Flag;
        this.name = name;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactId_string() {
        return contactId_string;
    }

    public void setContactId_string(String contactId_string) {
        this.contactId_string = contactId_string;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSend_Flag() {
        return send_Flag;
    }

    public void setSend_Flag(String send_Flag) {
        this.send_Flag = send_Flag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ContractObj(Parcel in) {
        readFromParcel(in);
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(messageId);
        dest.writeString(threadId);
        dest.writeString(address);
        dest.writeString(contactId_string);
        dest.writeString(timestamp);
        dest.writeString(body);
        dest.writeString(send_Flag);
        dest.writeString(name);
    }
    private void readFromParcel(Parcel in){

        messageId = in.readString();
        threadId = in.readString();
        address = in.readString();
        timestamp = in.readString();
        body = in.readString();
        send_Flag = in.readString();
        name = in.readString();
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
