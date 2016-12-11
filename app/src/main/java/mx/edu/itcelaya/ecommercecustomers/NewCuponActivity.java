package mx.edu.itcelaya.ecommercecustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import mx.edu.itcelaya.ecommercecustomers.model.Cupon;
import mx.edu.itcelaya.ecommercecustomers.model.Customer;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;

public class NewCuponActivity extends AppCompatActivity implements View.OnClickListener {

    String optType[] = {"Fixed Cart", "Percent", "Fixed Product", "Percent Product"};
    String optIndividualUse[] = {"Yes", "No"};
    String jsonResult;
    int idCustomer;
    Cupon cupon = new Cupon();
    String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/coupons";

    //-----------------------Componentes---------------------------
    EditText txtCode, txtAmount, txtMinAmount;
    Spinner spType, spIndividualUse;
    Button btnEnviar, btnCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cupon);

        createSpinners();
        txtCode = (EditText) findViewById(R.id.txtCode);
        txtAmount = (EditText) findViewById(R.id.txtAmount);
        txtMinAmount = (EditText) findViewById(R.id.txtMinAmount);

        btnEnviar = (Button) findViewById(R.id.btnEnviar);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        //listeners
        btnEnviar.setOnClickListener(this);
        btnCancelar.setOnClickListener(this);
    }

    private void createSpinners() {
        spType = (Spinner) findViewById(R.id.spType);
        spIndividualUse = (Spinner) findViewById(R.id.spIndividualUse);

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optType);
        ArrayAdapter<String> adapterIndividualUse = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optIndividualUse);

        //set adapters
        spType.setAdapter(adapterType);
        spIndividualUse.setAdapter(adapterIndividualUse);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEnviar:
                saveCupon();
                break;
            case R.id.btnCancelar:
                finish();
                break;
        }
    }

    private void saveCupon() {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.POST_TASK, "Guardando Cupón...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                Toast.makeText(NewCuponActivity.this, "Datos guardados correctamente.", Toast.LENGTH_LONG).show();
                finish();
            }
        });

        String tmpItemType = spType.getSelectedItem().toString();
        String tmpType = "";
        switch (tmpItemType){
            case "Fixed Cart":
                tmpType = "fixed_cart";
                break;
            case "Percent":
                tmpType = "percent";
                break;
            case "Fixed Product":
                tmpType = "fixed_product";
                break;
            case "Percent Product":
                tmpType = "percent_product";
                break;
        }

        String tmpIndividualItem = spType.getSelectedItem().toString();
        String tmpIndividual = "";
        switch (tmpIndividualItem){
            case "Yes":
                tmpIndividual = "true";
                break;
            case "No":
                tmpIndividual = "false";
                break;
        }

        cupon.setCode(txtCode.getText().toString());
        cupon.setType(tmpType);
        cupon.setAmount(Integer.parseInt(txtAmount.getText().toString()));
        cupon.setIndividual_use(tmpIndividual);
        cupon.setMin_amount(txtMinAmount.getText().toString());

        tarea.setObject(cupon);

        tarea.execute(new String[]{ url });
    }
}
