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

import mx.edu.itcelaya.ecommercecustomers.model.Order;

/**
 * Created by Radogan on 2016-12-12.
 */

public class OrdersAdapter extends BaseAdapter {

    private Context context;
    private List<Order> ordenes;

    public OrdersAdapter(Context context, List<Order> ordenes) {
        super();
        this.context = context;
        this.ordenes = ordenes;
    }

    @Override
    public int getCount() {
        return this.ordenes.size();
    }

    @Override
    public Object getItem(int position) {
        return this.ordenes.get(position);
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
            rowView = inflater.inflate(R.layout.list_orders_2, null);
        }

        TextView tvOrderNumber   = (TextView) rowView.findViewById(R.id.tvOrderNumber);
        TextView tvCreatedAt  = (TextView) rowView.findViewById(R.id.tvCreatedAt);
        TextView tvTotalProducts  = (TextView) rowView.findViewById(R.id.tvTotalProducts);
       // TextView tvEmail  = (TextView) rowView.findViewById(R.id.tvEmail);

        final Order item = this.ordenes.get(position);
        tvOrderNumber.setText(item.getOrder_number() + "");
        tvCreatedAt.setText(item.getCreated_at());
        tvTotalProducts.setText(item.getTotal_line_items_quantity() + "");
       // tvEmail.setText(item.getEmail());

        return rowView;
    }

    private InputStream OpenHttpConnection(String urlString)
            throws IOException
    {
        InputStream in = null;
        int response = -1;
        URL url = new URL(urlString);
        URLConnection conn = url.openConnection();
        if (!(conn instanceof HttpURLConnection))
            throw new IOException("Not an HTTP connection");
        try{
            HttpURLConnection httpConn = (HttpURLConnection) conn;
            httpConn.setRequestMethod("GET");

            httpConn.connect();
            response = httpConn.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                in = httpConn.getInputStream();
            }
        }
        catch (Exception ex) {
            throw new IOException("Error connecting" + response + ex.getMessage());
        }
        return in;
    }

    private Bitmap DownloadImage(String URL)     {
        Bitmap bitmap = null;
        InputStream in = null;
        try {
            in = OpenHttpConnection(URL);
            bitmap = BitmapFactory.decodeStream(in);
            in.close();
        } catch (IOException e1) {
            //Toast.makeText(context, e1.getMessage(), Toast.LENGTH_LONG).show();
            e1.printStackTrace();
        }
        return bitmap;
    }

    private class BackgroundTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... url) {
            //---download an image---
            Bitmap bitmap = DownloadImage(url[0]);
            return bitmap;
        }
    }
}
