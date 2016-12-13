package mx.edu.itcelaya.ecommercecustomers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Address;
import mx.edu.itcelaya.ecommercecustomers.model.Category;
import mx.edu.itcelaya.ecommercecustomers.model.Customer;
import mx.edu.itcelaya.ecommercecustomers.model.Order;
import mx.edu.itcelaya.ecommercecustomers.model.Payment;
import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;
import mx.edu.itcelaya.ecommercecustomers.utils.Json;

public class ApplyCuponActivity extends AppCompatActivity implements View.OnClickListener {

    String jsonResult, jsonCustomer;
    EditText txtCupon;
    Button btnContinuar, btnAplicar;
    String amount;
    ArrayList<Product> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_cupon);

        //checar esto!!!
        Intent i = getIntent();
        jsonCustomer = i.getStringExtra("jsonCustomer");
        //Intent j = getIntent();
        cart = (ArrayList<Product>) i.getSerializableExtra("cart");

        txtCupon = (EditText) findViewById(R.id.txtCupon);
        btnContinuar = (Button) findViewById(R.id.btnContinuar);
        btnAplicar = (Button) findViewById(R.id.btnAplicar);
        btnContinuar.setOnClickListener(this);
        btnAplicar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnContinuar:
                //realiza la orden
                saveOrder();
                Toast.makeText(this, "Orden realizada", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btnAplicar:
                Toast.makeText(this, "Obtener descuendo del cupón", Toast.LENGTH_SHORT).show();
                //no necesita finish();
                loadCupons("https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/coupons");
                break;
        }

        Intent in = new Intent(ApplyCuponActivity.this, MainActivity.class);
        startActivity(in);
    }

    private void loadCupons(String p_url) {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Cupones...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                amount = applyCoupon();
                saveOrder();
            }
        });
        tarea.execute(new String[]{p_url});
    }

    private void saveOrder(){
        Order order = new Order();
        String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/orders";

        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.POST_TASK, "Guardando Orden...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                //Toast.makeText(ApplyCuponActivity.this, "Orden realizada", Toast.LENGTH_LONG).show();
                //finish();
            }
        });

        //    String optPayment[] = {"Direct Bank Transfer", "Check Payments", "Cash On Delivery"};
        String method_title = "Direct Bank Transfer";
        String method_id = "bacs";
        Payment payment_details = new Payment(method_id, method_title, false);

        cart = prepareList();
        Integer id = getID();

        order.setPayment_details(payment_details);
        order.setCustomer_id(id);
        order.setLine_items(cart);

        String tmp_json = Json.OrderToJSON(order);

        tarea.setObject(order);
        tarea.execute(new String[]{ url });
    }

    private Integer getID(){
        try {
            JSONObject jsonResponse = new JSONObject(jsonCustomer);
            JSONObject jsonMainNode = jsonResponse.optJSONObject("customer");
            String id = jsonMainNode.optString("id");
//            JSONObject jsonChildNode = jsonMainNode.getJSONObject(0);
            return Integer.parseInt(id);

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }

    private ArrayList<Product> prepareList(){
        ArrayList<Product> tmp = new ArrayList<>();

        for(int i = 0; i< cart.size(); i++){
            tmp.add(new Product(cart.get(i).getId(), cart.get(i).getQuantity()));
        }

        return tmp;
    }

    public String applyCoupon() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("coupons");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);

                String code = jsonChildNode.optString("code");

                if (code.equals(txtCupon.getText().toString())) {
                    return jsonChildNode.optString("amount");
                }
            }
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
            return null;
        }
    }
}
