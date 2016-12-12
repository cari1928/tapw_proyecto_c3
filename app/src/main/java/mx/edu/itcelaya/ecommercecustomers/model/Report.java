package mx.edu.itcelaya.ecommercecustomers.model;

import java.util.List;

/**
 * Created by Radogan on 2016-12-11.
 */

public class Report {
    String total_sales;
    String average_sales;
    int total_orders;
    int total_items;
    String total_tax;

    List<String> sales;
    List<Integer> orders;
    List<Integer> items;
    List<Integer> costumers;

    public Report() {
    }

    public Report(String total_sales, String average_sales, int total_orders, int total_items, String total_tax) {
        this.total_sales = total_sales;
        this.average_sales = average_sales;
        this.total_orders = total_orders;
        this.total_items = total_items;
        this.total_tax = total_tax;
    }

    public Report(String total_sales, String average_sales, int total_orders, int total_items, String total_tax, List<String> sales, List<Integer> orders, List<Integer> items, List<Integer> costumers) {
        this.total_sales = total_sales;
        this.average_sales = average_sales;
        this.total_orders = total_orders;
        this.total_items = total_items;
        this.total_tax = total_tax;
        this.sales = sales;
        this.orders = orders;
        this.items = items;
        this.costumers = costumers;
    }

    public String getTotal_sales() {
        return total_sales;
    }

    public void setTotal_sales(String total_sales) {
        this.total_sales = total_sales;
    }

    public String getAverage_sales() {
        return average_sales;
    }

    public void setAverage_sales(String average_sales) {
        this.average_sales = average_sales;
    }

    public int getTotal_orders() {
        return total_orders;
    }

    public void setTotal_orders(int total_orders) {
        this.total_orders = total_orders;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public String getTotal_tax() {
        return total_tax;
    }

    public void setTotal_tax(String total_tax) {
        this.total_tax = total_tax;
    }

    public List<String> getSales() {
        return sales;
    }

    public void setSales(List<String> sales) {
        this.sales = sales;
    }

    public List<Integer> getOrders() {
        return orders;
    }

    public void setOrders(List<Integer> orders) {
        this.orders = orders;
    }

    public List<Integer> getItems() {
        return items;
    }

    public void setItems(List<Integer> items) {
        this.items = items;
    }

    public List<Integer> getCostumers() {
        return costumers;
    }

    public void setCostumers(List<Integer> costumers) {
        this.costumers = costumers;
    }
}
