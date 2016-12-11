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
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;

public class CategoryActivity extends AppCompatActivity {

    String jsonResult;
    public static String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/products/categories";
    public static String consumer_key    = "ck_a645f61ead6c17186e280ae58d547031078b345b";
    public static String consumer_secret = "cs_8097a58db4fed33c44437a1296963663398b711d";
    List<Category> items   = new ArrayList<Category>();
    CategoryAdapter cAdapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        list = (ListView) findViewById(R.id.listCategories);
        registerForContextMenu(list);
        loadCategories();
    }

    public void loadCategories() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Categorías...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                ListCategories();
            }
        });
        tarea.execute(new String[] { url });
    }

    public void ListCategories(){
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("product_categories");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                Integer id = jsonChildNode.optInt("id");
                String name = jsonChildNode.optString("name");
                String description = jsonChildNode.optString("description");
                String image = jsonChildNode.optString("image");

                items.add(new Category(id, name, description, image ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }

        cAdapter = new CategoryAdapter(this, items);
        list.setAdapter(cAdapter);
    }
}
