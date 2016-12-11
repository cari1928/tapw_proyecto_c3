package mx.edu.itcelaya.ecommercecustomers;

import android.app.Activity;
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

import java.util.concurrent.ExecutionException;

import mx.edu.itcelaya.ecommercecustomers.model.Address;
import mx.edu.itcelaya.ecommercecustomers.model.Customer;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;
import mx.edu.itcelaya.ecommercecustomers.utils.Json;

public class EditCustomerActivity extends Activity implements View.OnClickListener {

    String jsonResult;
    int idCustomer;
    EditText txtNombres, txtApellidos, txtEmail, txtAddress, txtCity, txtState, txtPostcode, txtCountry;
    Button btnEnviar, btnCancelar;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);

        txtNombres   = (EditText) findViewById(R.id.txtNombres);
        txtApellidos = (EditText) findViewById(R.id.txtApellidos);
        txtEmail     = (EditText) findViewById(R.id.txtEmail);
        txtAddress     = (EditText) findViewById(R.id.txtAddress);
        txtCity     = (EditText) findViewById(R.id.txtCity);
        txtState     = (EditText) findViewById(R.id.txtState);
        txtPostcode     = (EditText) findViewById(R.id.txtPostcode);
        txtCountry     = (EditText) findViewById(R.id.txtCountry);

        btnEnviar    = (Button) findViewById(R.id.btnEnviar);
        btnCancelar  = (Button) findViewById(R.id.btnCancelar);
        btnEnviar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);

        Intent i = getIntent();
        idCustomer = i.getIntExtra("idCustomer", 0);
        loadCustomer();

    }

    private void loadCustomer() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Cliente", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                loadCustomerInForm();
            }
        });

        tarea.execute(new String[] { MainActivity.url + "/" + idCustomer });
    }

    private void loadCustomerInForm() {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);

            JSONObject jsonChildNode = jsonResponse.getJSONObject("customer");

            JSONObject jsonChildNodeBillingAddress = jsonChildNode.getJSONObject("billing_address");
            String first_name = jsonChildNodeBillingAddress.getString("first_name");
            String last_name = jsonChildNodeBillingAddress.getString("last_name");
            String address_1 = jsonChildNodeBillingAddress.getString("address_1");
            String city = jsonChildNodeBillingAddress.getString("city");
            String state = jsonChildNodeBillingAddress.getString("state");
            String postcode = jsonChildNodeBillingAddress.getString("postcode");
            String country = jsonChildNodeBillingAddress.getString("country");
            Address billingAddress = new Address(first_name, last_name, address_1, city, state, postcode, country);

            JSONObject jsonChildNodeShippingAddress = jsonChildNode.getJSONObject("shipping_address");
            Address shippingAddress = new Address(first_name, last_name, address_1, city, state, postcode, country);

            customer =
                    new Customer(
                            jsonChildNode.getInt("id"),
                            jsonChildNode.getString("email"),
                            jsonChildNode.getString("first_name"),
                            jsonChildNode.getString("last_name"),
                            jsonChildNode.getString("username"),
                            billingAddress,
                            shippingAddress
                    );
            //System.out.println("Nombres: " + jsonChildNode.getString("first_name"));
            //System.out.println("Apellidos: " + jsonChildNode.getString("last_name"));
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }

        txtNombres.setText(customer.getFirst_name());
        txtApellidos.setText(customer.getLast_name());
        txtEmail.setText(customer.getEmail());
        txtAddress.setText(customer.getBilling_address().getAddress_1());
        txtCity.setText(customer.getBilling_address().getCity());
        txtState.setText(customer.getBilling_address().getState());
        txtPostcode.setText(customer.getBilling_address().getPostcode());
        txtCountry.setText(customer.getBilling_address().getCountry());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnviar:
                saveCustomer();
                break;
            case R.id.btnCancelar:
                finish();
                break;
        }
    }

    private void saveCustomer() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.PUT_TASK, "Guardando Cliente...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                Toast.makeText(EditCustomerActivity.this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });
        customer.setFirst_name(txtNombres.getText().toString());
        customer.setLast_name(txtApellidos.getText().toString());
        customer.setEmail(txtEmail.getText().toString());

        Address infoBilling = new Address(
                txtNombres.getText().toString(),
                txtApellidos.getText().toString(),
                txtAddress.getText().toString(),
                txtCity.getText().toString(),
                txtState.getText().toString(),
                txtPostcode.getText().toString(),
                txtCountry.getText().toString());
        customer.setBilling_address(infoBilling);

        Address infoShipping = new Address(
                txtNombres.getText().toString(),
                txtApellidos.getText().toString(),
                txtAddress.getText().toString(),
                txtCity.getText().toString(),
                txtState.getText().toString(),
                txtPostcode.getText().toString(),
                txtCountry.getText().toString());
        customer.setShipping_address(infoShipping);

        tarea.setObject(customer);

        tarea.execute(new String[] { MainActivity.url + "/" + idCustomer });
    }
}
