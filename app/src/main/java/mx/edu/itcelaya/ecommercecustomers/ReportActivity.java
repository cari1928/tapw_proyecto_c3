package mx.edu.itcelaya.ecommercecustomers;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import mx.edu.itcelaya.ecommercecustomers.model.Report;
import mx.edu.itcelaya.ecommercecustomers.task.AsyncResponse;
import mx.edu.itcelaya.ecommercecustomers.task.WooCommerceTask;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {

    String optTime[] = {"Day", "Week", "Month", "Last Month", "Year"};
    String optInformation[] = {"Subtotals", "Orders", "Products", "Customers"};
    String optGraph[] = {"BarChart", "PieChart"};

    String jsonResult;
    Report report = new Report();
    //String url = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/coupons";

    //-----------------------Componentes---------------------------
    Menu menu;
    EditText txtCode, txtAmount, txtMinAmount;
    Spinner spTime, spInformation, spGraph;
    Button btnGraph, btnReport;
    AlertDialog dialogFoto;
    Button btnRegresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        createSpinners();

        btnGraph = (Button) findViewById(R.id.btnGraph);
        btnReport = (Button) findViewById(R.id.btnReport);

        //listeners
        btnGraph.setOnClickListener(this);
        btnReport.setOnClickListener(this);
    }

    private void createSpinners() {
        spTime = (Spinner) findViewById(R.id.spTime);
        spInformation = (Spinner) findViewById(R.id.spInformation);
        spGraph = (Spinner) findViewById(R.id.spGraph);

        ArrayAdapter<String> adapterType = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optTime);
        ArrayAdapter<String> adapterIndividualUse = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optInformation);
        ArrayAdapter<String> adapterGraph = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, optGraph);

        //set adapters
        spTime.setAdapter(adapterType);
        spInformation.setAdapter(adapterIndividualUse);
        spGraph.setAdapter(adapterGraph);
    }

    @Override
    public void onClick(View view) {

        String time = spTime.getSelectedItem().toString();
        String tmpURL;

        if (!time.equals("Day")) {

            if (time.equals("Last Month")) {
                time = "last_month";
            }
            time = time.toLowerCase();
        }

        tmpURL = "https://tapw-proyecto-c3-cari1928.c9users.io/wc-api/v3/reports/sales?filter[period]=" + time;

        switch (view.getId()) {
            case R.id.btnGraph:
                loadGraphic(tmpURL);
                break;
            case R.id.btnReport:
                loadReport(tmpURL);
                break;
        }

        if (view == btnRegresa) {
            dialogFoto.dismiss();
        }
    }

    private void loadGraphic(String p_url){
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Reporte...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;

                Intent iGraph = new Intent(ReportActivity.this, GraphActivity.class);
                iGraph.putExtra("json", jsonResult);
                iGraph.putExtra("graph", spGraph.getSelectedItem().toString());
                startActivity(iGraph);
                //Toast.makeText(getApplicationContext(), jsonResult, Toast.LENGTH_LONG).show();
            }
        });
        tarea.execute(new String[]{p_url});
    }

    private void loadReport(String p_url) {
        WooCommerceTask tarea = new WooCommerceTask(this, WooCommerceTask.GET_TASK, "Cargando Reporte...", new AsyncResponse() {
            @Override
            public void setResponse(String output) {
                jsonResult = output;
                ListReport();
                //Toast.makeText(getApplicationContext(), jsonResult, Toast.LENGTH_LONG).show();
            }
        });
        tarea.execute(new String[]{p_url});
    }

    private void showGraphic(){

    }

    public void ListReport() {
        List<Report> rItems = new ArrayList<Report>();
        List<String> lSales = new ArrayList<>();
        List<Integer> lOrders = new ArrayList<>();
        List<Integer> lItems = new ArrayList<>();
        List<Integer> lCustumers = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONObject jsonChildNode = jsonResponse.getJSONObject("sales");

            String total_sales = jsonChildNode.optString("total_sales");
            String average_sales = jsonChildNode.optString("average_sales");
            Integer total_orders = jsonChildNode.optInt("total_orders");
            Integer total_items = jsonChildNode.optInt("total_items");
            String total_tax = jsonChildNode.optString("total_tax");

            //JSONArray jsonLists = jsonChildNode.optJSONArray("totals");

//            String prueba = jsonChildNode.optString("totals");
//            JSONObject jsonPrueba = jsonChildNode.optJSONObject("totals");
//            Toast.makeText(getApplicationContext(), prueba, Toast.LENGTH_LONG).show();
//            System.out.print(prueba);

            rItems.add(new Report(total_sales, average_sales, total_orders, total_items, total_tax));

            AlertDialog.Builder builder = new AlertDialog.Builder(this); //recibe el contexto de la app
            LinearLayout layout1 = new LinearLayout(this); //para colocar en él los elementos
            layout1.setOrientation(LinearLayout.VERTICAL);

            //nuevo listview en conjunto con un arrayadapter
            ListView vReports = new ListView(this);
            vReports.setAdapter(new ReportAdapter(this, rItems));

            //boton
            btnRegresa = new Button(this);
            btnRegresa.setText("Cerrar");
            btnRegresa.setOnClickListener(this);

            //se pasan los elementos al layout
            layout1.addView(vReports);
            layout1.addView(btnRegresa);

            builder.setView(layout1); //se le pasa el layout a builder
            dialogFoto = builder.create(); //se termina de crear el dialogo
            dialogFoto.show(); //se muestra el dialogo

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();

        }
    }
}
