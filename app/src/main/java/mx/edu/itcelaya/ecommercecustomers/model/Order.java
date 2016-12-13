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
    String total_discount;

    private List<Integer> products_id;
    private String created_at;
    private int total_line_items_quantity;
    private String full_name;
    private String email;

    public Order(int id, int order_number, String status, Double total, List<Product> line_items) {
        this.id = id;
        this.order_number = order_number;
        this.status = status;
        this.total = total;
        this.line_items = line_items;
    }

    public Order(int order_number, String created_at, int total_line_items_quantity, String full_name, String email, List<Integer> products_id) {
        this.order_number = order_number;
        this.created_at = created_at;
        this.total_line_items_quantity = total_line_items_quantity;
        this.total = total;
        this.payment_details = payment_details;
        this.full_name = full_name;
        this.email = email;
        this.products_id = products_id;
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

    public List<Integer> getProducts_id() {
        return products_id;
    }

    public void setProducts_id(List<Integer> products_id) {
        this.products_id = products_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getTotal_line_items_quantity() {
        return total_line_items_quantity;
    }

    public void setTotal_line_items_quantity(int total_line_items_quantity) {
        this.total_line_items_quantity = total_line_items_quantity;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTotal_discount() {
        return total_discount;
    }

    public void setTotal_discount(String total_discount) {
        this.total_discount = total_discount;
    }
}
