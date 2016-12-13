package mx.edu.itcelaya.ecommercecustomers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    BarChart barChart;
    PieChart pieChart;
    String jsonResult, graph;
    ArrayList<BarEntry> lBarData = new ArrayList<>();
    ArrayList<PieEntry> lPieData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        barChart = (BarChart) findViewById(R.id.grBar);
        pieChart = (PieChart) findViewById(R.id.grPie);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            jsonResult = extras.getString("json");
            graph = extras.getString("graph");
            //Toast.makeText(this, jsonResult, Toast.LENGTH_SHORT).show();
            procesaJson();
        }
    }

    private void procesaJson() {
        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONObject jsonChildNode = jsonResponse.getJSONObject("sales");

            String total_sales = jsonChildNode.optString("total_sales");
            String average_sales = jsonChildNode.optString("average_sales");
            Integer total_orders = jsonChildNode.optInt("total_orders");
            Integer total_items = jsonChildNode.optInt("total_items");
            String total_tax = jsonChildNode.optString("total_tax");

            if(graph.equals("BarChart")){
                lBarData.add(new BarEntry(0, Float.valueOf(total_sales)));
                lBarData.add(new BarEntry(1, Float.valueOf(average_sales)));
                lBarData.add(new BarEntry(2, Float.valueOf(total_orders)));
                lBarData.add(new BarEntry(3, Float.valueOf(total_items)));
                lBarData.add(new BarEntry(4, Float.valueOf(total_tax)));

            } else {
                lPieData.add(new PieEntry(0, Float.valueOf(total_sales)));
                lPieData.add(new PieEntry(1, Float.valueOf(average_sales)));
                lPieData.add(new PieEntry(2, Float.valueOf(total_orders)));
                lPieData.add(new PieEntry(3, Float.valueOf(total_items)));
                lPieData.add(new PieEntry(4, Float.valueOf(total_tax)));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_LONG).show();
        }
        generaGrafica();
    }

    private void generaGrafica() {

        if(graph.equals("BarChart")){
            BarEntry v1 = lBarData.get(0);
            System.out.println("Valor 1" + v1.toString() + v1.getY());
            BarDataSet bDataSet = new BarDataSet(lBarData, "Reporte de Ventas");
            bDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            BarData bData = new BarData(bDataSet);
            barChart.setData(bData);

        } else {
            PieEntry p1 = lPieData.get(0);
            PieDataSet pDataSet = new PieDataSet(lPieData, "Reporte de Ventas");
            pDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
            PieData pData = new PieData(pDataSet);
            pieChart.setData(pData);
        }
        System.out.println("Ends...");
    }
}
