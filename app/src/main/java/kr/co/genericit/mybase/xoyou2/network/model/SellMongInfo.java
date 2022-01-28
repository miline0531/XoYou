package kr.co.genericit.mybase.xoyou2.network.model;

public class SellMongInfo {
    int mong_srl;
    String start_time;
    String end_time;
    int transaction_type;
    int start_value;
    String description;
    String title;
    String[] category_code;


    public SellMongInfo(int mong_srl, String start_time, String end_time, int transaction_type, int start_value, String description, String title, String[] category_code) {
        this.mong_srl = mong_srl;
        this.start_time = start_time;
        this.end_time = end_time;
        this.transaction_type = transaction_type;
        this.start_value = start_value;
        this.description = description;
        this.title = title;
        this.category_code = category_code;
    }

    public int getMong_srl() {
        return mong_srl;
    }

    public void setMong_srl(int mong_srl) {
        this.mong_srl = mong_srl;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getTransaction_type() {
        return transaction_type;
    }

    public void setTransaction_type(int transaction_type) {
        this.transaction_type = transaction_type;
    }

    public int getStart_value() {
        return start_value;
    }

    public void setStart_value(int start_value) {
        this.start_value = start_value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String[] category_code) {
        this.category_code = category_code;
    }
}
