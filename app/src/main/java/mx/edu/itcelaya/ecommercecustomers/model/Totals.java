package mx.edu.itcelaya.ecommercecustomers.model;

/**
 * Created by Radogan on 2016-12-11.
 */

public class Totals {
    String sales;
    int orders;
    int items;
    int customers;

    public Totals(String sales, int orders, int items, int customers) {
        this.sales = sales;
        this.orders = orders;
        this.items = items;
        this.customers = customers;
    }

    public String getSales() {
        return sales;
    }

    public void setSales(String sales) {
        this.sales = sales;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public int getCustomers() {
        return customers;
    }

    public void setCustomers(int customers) {
        this.customers = customers;
    }
}
