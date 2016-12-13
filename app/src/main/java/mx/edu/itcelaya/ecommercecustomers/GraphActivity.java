package mx.edu.itcelaya.ecommercecustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    BarChart b1;
    BarDataSet dataset;
    BarData data;
    String jsonResult;
    ArrayList<BarEntry> datos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        b1 = (BarChart) findViewById(R.id.bar1);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonResult = extras.getString("json");
            //Toast.makeText(this, jsonResult, Toast.LENGTH_SHORT).show();
            procesaJson();
        }
    }

    private void procesaJson(){
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONObject jsonChildNode = jsonResponse.getJSONObject("sales");

            String total_sales = jsonChildNode.optString("total_sales");
            String average_sales = jsonChildNode.optString("average_sales");
            Integer total_orders = jsonChildNode.optInt("total_orders");
            Integer total_items = jsonChildNode.optInt("total_items");
            String total_tax = jsonChildNode.optString("total_tax");

            datos.add(new BarEntry(0, Float.valueOf(total_sales)));
            datos.add(new BarEntry(1, Float.valueOf(average_sales)));
            datos.add(new BarEntry(2, Float.valueOf(total_orders)));
            datos.add(new BarEntry(3, Float.valueOf(total_items)));
            datos.add(new BarEntry(4, Float.valueOf(total_tax)));

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }
        generaGrafica();
    }

    private void generaGrafica() {

        BarEntry v1 = datos.get(0);
        System.out.println("Valor 1" + v1.toString() + v1.getY());

        BarDataSet dataset = new BarDataSet(datos, "Reporte de Ventas");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(dataset);
        //b1 = (BarChart) findViewById(R.id.bar1);
        b1.setData(data);
        System.out.println("Ends...");

    }
}
