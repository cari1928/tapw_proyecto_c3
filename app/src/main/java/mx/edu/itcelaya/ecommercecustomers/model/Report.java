package mx.edu.itcelaya.ecommercecustomers.model;

/**
 * Created by Radogan on 2016-12-11.
 */

public class Report {
    String total_sales;
    String average_sales;
    int total_orders;
    int total_items;
    String total_tax;

    int total_customers;
    Totals totals;

    public Report() {
    }

    public Report(String total_sales, String average_sales, int total_orders, int total_items, String total_tax) {
        this.total_sales = total_sales;
        this.average_sales = average_sales;
        this.total_orders = total_orders;
        this.total_items = total_items;
        this.total_tax = total_tax;
    }

    public Report(String total_sales, String average_sales, int total_orders, int total_items, String total_tax, int total_customers, Totals totals) {
        this.total_sales = total_sales;
        this.average_sales = average_sales;
        this.total_orders = total_orders;
        this.total_items = total_items;
        this.total_tax = total_tax;
        this.total_customers = total_customers;
        this.totals = totals;
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

    public int getTotal_customers() {
        return total_customers;
    }

    public void setTotal_customers(int total_customers) {
        this.total_customers = total_customers;
    }

    public Totals getTotals() {
        return totals;
    }

    public void setTotals(Totals totals) {
        this.totals = totals;
    }
}
