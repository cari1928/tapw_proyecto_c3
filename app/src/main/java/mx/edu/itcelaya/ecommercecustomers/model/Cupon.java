package mx.edu.itcelaya.ecommercecustomers.model;

/**
 * Created by Radogan on 2016-12-11.
 */

public class Cupon {
    String code;
    String type;
    int amount;
    String individual_use;
    String min_amount;

    public Cupon(String code, String type, String individual_use, int amount, String min_amount) {
        this.code = code;
        this.type = type;
        this.individual_use = individual_use;
        this.amount = amount;
        this.min_amount = min_amount;
    }

    public Cupon() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndividual_use() {
        return individual_use;
    }

    public void setIndividual_use(String individual_use) {
        this.individual_use = individual_use;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getMin_amount() {
        return min_amount;
    }

    public void setMin_amount(String min_amount) {
        this.min_amount = min_amount;
    }
}
