package mx.edu.itcelaya.ecommercecustomers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.model.Report;

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

        final Report item = this.reports.get(position);

        tvTotalSales.setText(item.getTotal_sales());
        tvAverageSales.setText(item.getAverage_sales());
        tvTotalOrders.setText(item.getTotal_orders() + "");
        tvTotalProducts.setText(item.getTotal_items() + "");
        tvTotalTax.setText(item.getTotal_tax());

        return rowView;
    }
}
