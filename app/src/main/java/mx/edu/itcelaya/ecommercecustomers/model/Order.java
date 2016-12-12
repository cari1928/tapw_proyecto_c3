package mx.edu.itcelaya.ecommercecustomers.model;

import java.util.List;

/**
 * Created by niluxer on 5/20/16.
 */
public class Order {

    int id;
    int order_number;
    String status;
    Double total;
    List<Product> line_items;

    Payment payment_details;
    int customer_id;

    public Order(int id, int order_number, String status, Double total, List<Product> line_items) {
        this.id = id;
        this.order_number = order_number;
        this.status = status;
        this.total = total;
        this.line_items = line_items;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Product> getLine_items() {
        return line_items;
    }

    public void setLine_items(List<Product> line_items) {
        this.line_items = line_items;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Payment getPayment_details() {
        return payment_details;
    }

    public void setPayment_details(Payment payment_details) {
        this.payment_details = payment_details;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }
}
