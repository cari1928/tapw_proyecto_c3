package mx.edu.itcelaya.ecommercecustomers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Product;

public class CartActivity extends AppCompatActivity {

    List<Product> items;
    ProductAdapter pAdapter;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        list = (ListView) findViewById(R.id.listProducts);
        registerForContextMenu(list);

        Intent i = getIntent();
        ArrayList<Product> items = (ArrayList<Product>) i.getSerializableExtra("cart");

        pAdapter = new ProductAdapter(this, items, 1);
        list.setAdapter(pAdapter);
    }
}
