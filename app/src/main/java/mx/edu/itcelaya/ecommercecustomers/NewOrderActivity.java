package mx.edu.itcelaya.ecommercecustomers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Order;
import mx.edu.itcelaya.ecommercecustomers.model.Payment;
import mx.edu.itcelaya.ecommercecustomers.model.Product;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;
import mx.edu.itcelaya.ecommercecustomers.utils.Json;

public class NewOrderActivity extends AppCompatActivity implements View.OnClickListener {

    String optPayment[] = {"Direct Bank Transfer", "Check Payments", "Cash On Delivery"};
    String jsonResult;
    String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/orders";
    int idCustomer;
    Order order = new Order();

    //-----------------------Componentes---------------------------
    EditText txtProductID, txtQuantity;
    Spinner spPayment;
    Button btnEnviar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        createSpinner();
        txtProductID = (EditText) findViewById(R.id.txtProductID);
        txtQuantity = (EditText) findViewById(R.id.txtQuantity);

        Intent i = getIntent();
        idCustomer = i.getIntExtra("idCustomer", 0);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        //listeners
        btnEnviar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    private void createSpinner() {
        spPayment = (Spinner) findViewById(R.id.spMethodTitle);
        ArrayAdapter<String> adapterPayment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optPayment);
        spPayment.setAdapter(adapterPayment);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnviar:
                saveOrder();
                break;
            case R.id.btnCancelar:
                finish();
                break;
        }
    }

    private void saveOrder() {
        List<Product> products = new ArrayList<>();

        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.POST_TASK, "Guardando Orden...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                Toast.makeText(NewOrderActivity.this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        //    String optPayment[] = {"Direct Bank Transfer", "Check Payments", "Cash On Delivery"};
        String method_title = spPayment.getSelectedItem().toString();
        String method_id = "";
        switch (method_title) {
            case "Direct Bank Transfer":
                method_id = "bacs";
                break;
            case "Check Payments":
                method_id = "cheque";
                break;
            case "Cash On Delivery":
                method_id = "cod";
                break;
        }

        int product_id = Integer.parseInt(txtProductID.getText().toString());
        int quantity = Integer.parseInt(txtQuantity.getText().toString());

        Payment payment_details = new Payment(method_id, method_title, false);
        products.add(new Product(product_id, quantity));

        order.setPayment_details(payment_details);
        order.setCustomer_id(idCustomer);
        order.setLine_items(products);

        tarea.setObject(order);
        tarea.execute(new String[]{ url });
    }

}
