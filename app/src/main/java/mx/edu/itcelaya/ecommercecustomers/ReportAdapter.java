package mx.edu.itcelaya.ecommercecustomers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Order;
import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.model.Report;
import mx.edu.itcelaya.ecommercecustomers.model.Totals;

/**
 * Created by Radogan on 2016-12-11.
 */

public class ReportAdapter extends BaseAdapter {
    private Context context;
    private List<Report> reports;

    public ReportAdapter(Context context, List<Report> reviews) {
        this.context = context;
        this.reports = reviews;
    }

    @Override
    public int getCount() {
        return this.reports.size();
    }

    @Override
    public Object getItem(int i) {
        return this.reports.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_reports, null);
        }

        TextView tvTotalSales   = (TextView) rowView.findViewById(R.id.tvTotalSales);
        TextView tvAverageSales  = (TextView) rowView.findViewById(R.id.tvAverageSales);
        TextView tvTotalOrders  = (TextView) rowView.findViewById(R.id.tvTotalOrders);
        TextView tvTotalProducts  = (TextView) rowView.findViewById(R.id.tvTotalProducts);
        TextView tvTotalTax  = (TextView) rowView.findViewById(R.id.tvTotalTax);
        ListView lvSubtotal = (ListView) rowView.findViewById(R.id.lvSubtotal);
//        ListView lvOrdenes = (ListView) rowView.findViewById(R.id.lvOrdenes);
//        ListView lvProducts = (ListView) rowView.findViewById(R.id.lvProducts);
//        ListView lvCustomers = (ListView) rowView.findViewById(R.id.lvCustomers);

        final Report item = this.reports.get(position);

        tvTotalSales.setText(item.getTotal_sales());
        tvAverageSales.setText(item.getAverage_sales());
        tvTotalOrders.setText(item.getTotal_orders() + "");
        tvTotalProducts.setText(item.getTotal_items() + "");
        tvTotalTax.setText(item.getTotal_tax());

        List<String> lSales = item.getSales();

        //falta checar éstos
//        List<Integer> lOrders = item.getOrders();
//        List<Integer> lItems = item.getItems();
//        List<Integer> lCustumers = item.getCostumers();

        final ArrayList<String> list = new ArrayList<String>();
        for (Iterator<String> iter = lSales.iterator(); iter.hasNext(); ) {
            String element = iter.next();
            list.add(element);
        }

        final ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, list);
        lvSubtotal.setAdapter(adapter);
        rowView.setTag("Prueba");

        return rowView;
    }

}
