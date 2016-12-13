package mx.edu.itcelaya.ecommercecustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Category;
import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;

public class ProductActivity extends AppCompatActivity {
    String jsonResult;
    public static String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/products";
    List<Product> items = new ArrayList<Product>();
    ProductAdapter pAdapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        list = (ListView) findViewById(R.id.listProducts);
        registerForContextMenu(list);
        loadProducts();
    }

    public void loadProducts() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Productos...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                ListProducts();
            }
        });
        tarea.execute(new String[]{ url });
    }

    public void ListProducts() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("products");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                Integer id_product = jsonChildNode.optInt("id");
                String name = jsonChildNode.optString("title");
                Double price = jsonChildNode.optDouble("price");
                Integer stock_quantity = jsonChildNode.optInt("stock_quantity");
                String ImageURL = jsonChildNode.optString("featured_src");

                if(stock_quantity < 0) {
                    stock_quantity = 0;
                }

                items.add(new Product(id_product, name, price, stock_quantity, ImageURL));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }

        pAdapter = new ProductAdapter(this, items);
        list.setAdapter(pAdapter);
    }
}
