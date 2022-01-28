package kr.co.genericit.mybase.xoyou2.network.response;

public class CoinDataResult {
    boolean result;
    String msg;
    Resp resp;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Resp getResp() {
        return resp;
    }

    public void setResp(Resp resp) {
        this.resp = resp;
    }

    public class Resp{
        String max_price;
        String min_price;
        int number_go_store;
        int number_transaction;
        int number_transaction_success;
        int number_sale_register;
        int number_auction_register;
        int number_sale_in_store;
        int number_auction_in_store;
        String total_token_transfer;
        String my_balance;
        int transaction_register_today;

        @Override
        public String toString() {
            return "Resp{" +
                    "max_price='" + max_price + '\'' +
                    ", min_price='" + min_price + '\'' +
                    ", number_go_store=" + number_go_store +
                    ", number_transaction=" + number_transaction +
                    ", number_transaction_success=" + number_transaction_success +
                    ", number_sale_register=" + number_sale_register +
                    ", number_auction_register=" + number_auction_register +
                    ", number_sale_in_store=" + number_sale_in_store +
                    ", number_auction_in_store=" + number_auction_in_store +
                    ", total_token_transfer='" + total_token_transfer + '\'' +
                    ", my_balance='" + my_balance + '\'' +
                    ", transaction_register_today=" + transaction_register_today +
                    '}';
        }

        public String getMax_price() {
            return max_price;
        }

        public void setMax_price(String max_price) {
            this.max_price = max_price;
        }

        public String getMin_price() {
            return min_price;
        }

        public void setMin_price(String min_price) {
            this.min_price = min_price;
        }

        public int getNumber_go_store() {
            return number_go_store;
        }

        public void setNumber_go_store(int number_go_store) {
            this.number_go_store = number_go_store;
        }

        public int getNumber_transaction() {
            return number_transaction;
        }

        public void setNumber_transaction(int number_transaction) {
            this.number_transaction = number_transaction;
        }

        public int getNumber_transaction_success() {
            return number_transaction_success;
        }

        public void setNumber_transaction_success(int number_transaction_success) {
            this.number_transaction_success = number_transaction_success;
        }

        public int getNumber_sale_register() {
            return number_sale_register;
        }

        public void setNumber_sale_register(int number_sale_register) {
            this.number_sale_register = number_sale_register;
        }

        public int getNumber_auction_register() {
            return number_auction_register;
        }

        public void setNumber_auction_register(int number_auction_register) {
            this.number_auction_register = number_auction_register;
        }

        public int getNumber_sale_in_store() {
            return number_sale_in_store;
        }

        public void setNumber_sale_in_store(int number_sale_in_store) {
            this.number_sale_in_store = number_sale_in_store;
        }

        public int getNumber_auction_in_store() {
            return number_auction_in_store;
        }

        public void setNumber_auction_in_store(int number_auction_in_store) {
            this.number_auction_in_store = number_auction_in_store;
        }

        public String getTotal_token_transfer() {
            return total_token_transfer;
        }

        public void setTotal_token_transfer(String total_token_transfer) {
            this.total_token_transfer = total_token_transfer;
        }

        public String getMy_balance() {
            return my_balance;
        }

        public void setMy_balance(String my_balance) {
            this.my_balance = my_balance;
        }

        public int getTransaction_register_today() {
            return transaction_register_today;
        }

        public void setTransaction_register_today(int transaction_register_today) {
            this.transaction_register_today = transaction_register_today;
        }
    }



}
