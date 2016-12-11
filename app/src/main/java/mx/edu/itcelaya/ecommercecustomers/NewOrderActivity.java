package mx.edu.itcelaya.ecommercecustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import mx.edu.itcelaya.ecommercecustomers.model.Customer;

public class NewOrderActivity extends AppCompatActivity {

    String jsonResult;
    int idCustomer;
    EditText txtNombres, txtApellidos, txtEmail, txtUsuario;
    Button btnEnviar, btnCancelar;
    Customer customer = new Customer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);
    }
}
