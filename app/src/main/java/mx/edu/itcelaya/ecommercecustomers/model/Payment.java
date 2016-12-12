package mx.edu.itcelaya.ecommercecustomers.model;

/**
 * Created by Radogan on 2016-12-11.
 */

public class Payment {
    String method_id;
    String method_title;
    boolean paid;

    public Payment(String method_id, String method_title, boolean paid) {
        this.method_id = method_id;
        this.method_title = method_title;
        this.paid = paid;
    }

    public String getMethod_id() {
        return method_id;
    }

    public void setMethod_id(String method_id) {
        this.method_id = method_id;
    }

    public String getMethod_title() {
        return method_title;
    }

    public void setMethod_title(String method_title) {
        this.method_title = method_title;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }
}
