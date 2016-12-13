package mx.edu.itcelaya.ecommercecustomers;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mx.edu.itcelaya.ecommercecustomers.model.Order;
import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;

public class OrderStatusActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    String statusSelected;
    String url;
    String[] a_status;
    String jsonResult;
    String type;
    List<String> status_items = new ArrayList<String>();
    List<Order> items = new ArrayList<Order>();
    List<Product> p_items = new ArrayList<Product>();
    int customer_id;

    ListView listOrders;
    Spinner sp_status;
    AlertDialog dialogFoto;
    Button btnRegresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        NukeSSLCerts.nuke(); //para ejecutar lar url

        Intent i = getIntent();
        customer_id = i.getIntExtra("customer_id", 0);

        listOrders = (ListView) findViewById(R.id.lvOrders);

        createSpinner();
        //falta traer el customer_id !!!
    }

    public void createSpinner() {
        statusSelected = "";
        sp_status = (Spinner) findViewById(R.id.spStatus);

        //para cargar los status disponibles!!
        url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/orders/statuses";
        type = "status";
        loadElements(url);
    }

    private void loadElements(String p_url) {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Status...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                //Toast.makeText(getApplicationContext(), jsonResult, Toast.LENGTH_LONG).show();
                prepareStatus();
            }
        });

        tarea.execute(new String[]{p_url});
    }

    private void prepareStatus() {
        switch (type) {
            case "status":
                ListStatus(); //se llenan algunas variables que se usarán a continuación
                a_status = new String[8];
                a_status[0] = "Select Status";
                for (int i = 1; i < a_status.length; i++) {
                    a_status[i] = status_items.get(i - 1);
                }

                ArrayAdapter<String> adapter_sp = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, a_status);
                sp_status.setAdapter(adapter_sp);
                sp_status.setOnItemSelectedListener(this);
                break;

            case "orders":
                //Toast.makeText(getApplicationContext(), "Lista de Órdenes", Toast.LENGTH_LONG).show();
                ListOrders();
                break;

            case "products":
                ListProducts();
                //Toast.makeText(getApplicationContext(), "Lista de Productos", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void ListProducts() {
        try {
            //se obtiene el apartado product
            JSONObject jsonResponse = new JSONObject(jsonResult);
            String jsonProduct = jsonResponse.optString("product");

            //ya se pueden obtener sus elementos
            JSONObject jsonResponseProduct = new JSONObject(jsonProduct);
            Integer id = jsonResponseProduct.optInt("id");
            String ImageURL = jsonResponseProduct.optString("featured_src");
            String title = jsonResponseProduct.optString("title");
            Double price = jsonResponseProduct.optDouble("price"); //podría causar error
            String description = jsonResponseProduct.optString("description");
            Boolean in_stock = jsonResponseProduct.optBoolean("in_stock");
            String stock_quantity = "0";

            if (in_stock) {
                stock_quantity = jsonResponseProduct.optString("stock_quantity");
                //Toast.makeText(getApplicationContext(), stock_quantity, Toast.LENGTH_LONG).show();
                if (stock_quantity != null && !stock_quantity.equals("")) {
                    stock_quantity = "0";
                }
            }
            p_items.add(new Product(id, ImageURL, title, price, description));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void ListOrders() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("orders");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                List<Integer> products_id = new ArrayList<Integer>();
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                String jsonStatus = jsonChildNode.optString("status");

                //Toast.makeText(getApplicationContext(), "JSONStatus: " + jsonStatus, Toast.LENGTH_LONG).show();
                if (jsonStatus.equals(statusSelected)) {
                    Integer order_number = jsonChildNode.optInt("order_number");
                    String created_at = jsonChildNode.optString("created_at");
                    Integer total_line_items_quantity = jsonChildNode.optInt("total_line_items_quantity");
                    String total = jsonChildNode.optString("total");

                    String jsonPayment = jsonChildNode.optString("payment_details");
                    JSONObject jsonResponsePayment = new JSONObject(jsonPayment);
                    String payment_details = jsonResponsePayment.optString("method_title"); //obtenido!!!

                    String jsonPersonal = jsonChildNode.optString("billing_address");
                    JSONObject jsonResponsePersonal = new JSONObject(jsonPersonal);
                    String first_name = jsonResponsePersonal.optString("first_name");
                    String last_name = jsonResponsePersonal.optString("last_name");
                    String email = jsonResponsePersonal.optString("email");
                    String nombre = first_name + " " + last_name;

//                    String jsonProduct = jsonChildNode.optString("line_items");
//                    JSONArray jsonMainNode2 = jsonChildNode.optJSONArray(jsonProduct);
//                    for (int j = 0; j < jsonMainNode2.length(); j++) {
//                        JSONObject p = jsonMainNode2.getJSONObject(j);
//                        Integer product_id = p.optInt("product_id");
//                        //Toast.makeText(getApplicationContext(), "Productos: " + p.optInt("product_id"), Toast.LENGTH_LONG).show();
//                        products_id.add(product_id);
//                    }

                    JSONArray jsonProductsNode = jsonChildNode.optJSONArray("line_items");

                    String sProducts = "";
                    List<Product> products = new ArrayList<Product>();
                    for (int x = 0; x < jsonProductsNode.length(); x++) {
                        JSONObject jsonChildNodeProduct = jsonProductsNode.getJSONObject(x);
                        //sProducts += jsonChildNodeProduct.optString("product_id") + ",";
                        products.add(new Product(
                                jsonChildNodeProduct.optInt("product_id"),
                                jsonChildNodeProduct.optString("name"),
                                jsonChildNodeProduct.optDouble("price"),
                                jsonChildNodeProduct.optInt("quantity")
                        ));
                    }

                    //items.add(new Order(order_number, created_at, total_line_items_quantity, nombre, email, products_id));
                    items.add(new Order(
                            jsonChildNode.optInt("id"),
                            jsonChildNode.optInt("order_number"),
                            jsonChildNode.optString("status"),
                            jsonChildNode.optDouble("total"),
                            products
                    ));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();

        }
        listOrders.setAdapter(new OrderAdapter(this, items));
    }

    public void ListStatus() {
        try {
            //se obtiene el apartado product
            JSONObject jsonResponse = new JSONObject(jsonResult);
            String jsonProduct = jsonResponse.optString("order_statuses");

            //ya se pueden obtener sus elementos
            JSONObject jsonResponseProduct = new JSONObject(jsonProduct);
            String pending = jsonResponseProduct.optString("pending");
            String processing = jsonResponseProduct.optString("processing");
            String on_hold = jsonResponseProduct.optString("on-hold");
            String completed = jsonResponseProduct.optString("completed");
            String cancelled = jsonResponseProduct.optString("cancelled");
            String refunded = jsonResponseProduct.optString("refunded");
            String failed = jsonResponseProduct.optString("failed");

            status_items.add(pending);
            status_items.add(processing);
            status_items.add(on_hold);
            status_items.add(completed);
            status_items.add(cancelled);
            status_items.add(refunded);
            status_items.add(failed);

            //Toast.makeText(this, status_items.size(), Toast.LENGTH_LONG).show();
            //p_items.add(new Products(id, ImageURL, title, price, in_stock, stock_quantity, description));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == btnRegresa) {
            dialogFoto.dismiss();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        TextView myText = (TextView) view;
        //Toast.makeText(this, "You selected " + myText.getText().toString().toLowerCase(), Toast.LENGTH_SHORT).show();
        if (position != 0) {
            switch (position) {
                case 2: //Processing
                case 4: //Completed
                case 5: //Cancelled
                case 6: //Refunded
                case 7: //Failed
                    statusSelected = myText.getText().toString().toLowerCase();
                    break;

                case 1: //Pending Payment
                    statusSelected = "pending";
                    break;

                case 3: //On Hold
                    statusSelected = "on-hold";
                    break;
            }
            initListOrders();
            type = "orders";
            loadElements("https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/customers/" + customer_id + "/orders");
        }
    }

    public void initListOrders() {
        listOrders.setAdapter(null);
        items = new ArrayList<Order>();
        listOrders.setOnItemClickListener(listenerOrder); //crear un listener que responda a cada elemento del list view
    }

    AdapterView.OnItemClickListener listenerOrder = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            List<Integer> products_id;
            p_items = new ArrayList<Product>();
            Order selected_order = items.get(position); //posición
            //Toast.makeText(getBaseContext(), "Orden: " + selected_order.getOrder_number(), Toast.LENGTH_LONG).show();

            products_id = selected_order.getProducts_id();
            for (int i = 0; i < products_id.size(); i++) {
                Integer product_id = products_id.get(i); //esto debe modificarse
                //Toast.makeText(getBaseContext(), "ID Producto: " + products_id.get(i), Toast.LENGTH_LONG).show();
                type = "products";
                loadElements("https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/products/" + product_id);
            }
            setAdapter();
        }
    };

    private void setAdapter() {
        //para mostrar los productos
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //recibe el contexto de la app
        LinearLayout layout1 = new LinearLayout(this); //para colocar en él los elementos
        layout1.setOrientation(LinearLayout.VERTICAL);

        //nuevo listview en conjunto con un arrayadapter
        ListView vProducts = new ListView(this);
        vProducts.setAdapter(new ProductAdapter(this, p_items));

        //boton
        btnRegresa = new Button(this);
        btnRegresa.setText("Cerrar");
        btnRegresa.setOnClickListener(this);

        //se pasan los elementos al layout
        layout1.addView(vProducts);
        layout1.addView(btnRegresa);

        builder.setView(layout1); //se le pasa el layout a builder
        dialogFoto = builder.create(); //se termina de crear el dialogo
        dialogFoto.show(); //se muestra el dialogo
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static class NukeSSLCerts {
        protected static final String TAG = "NukeSSLCerts";

        public static void nuke() {
            try {
                TrustManager[] trustAllCerts = new TrustManager[]{
                        new X509TrustManager() {
                            public X509Certificate[] getAcceptedIssuers() {
                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                                return myTrustedAnchors;
                            }

                            @Override
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            @Override
                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }
                        }
                };

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                });
            } catch (Exception e) {
            }
        }
    }
}
