package com.example.t2m.moneytracker.statistical;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.dataaccess.CategoriesDAOImpl;
import com.example.t2m.moneytracker.dataaccess.ITransactionsDAO;
import com.example.t2m.moneytracker.dataaccess.IWalletsDAO;
import com.example.t2m.moneytracker.dataaccess.TransactionsDAOImpl;
import com.example.t2m.moneytracker.model.Category;
import com.example.t2m.moneytracker.model.Transaction;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

public class StatisticalTabFragment extends Fragment implements OnChartValueSelectedListener {
  private PieChart mChart;
  private CombinedChart comChart;

  ITransactionsDAO iTransactionsDAO;
  static TransactionsDAOImpl transactionsDAOImpl;
  static CategoriesDAOImpl categoriesDAOImpl;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_statistical,container,false);
    transactionsDAOImpl = new TransactionsDAOImpl(getContext());
    categoriesDAOImpl = new CategoriesDAOImpl(getContext());
    methodMChart(mChart, view);
    methodComChart(comChart, view);
    return view;
  }

  @Override
  public void onValueSelected(Entry e, Highlight h) {
//    int dur = Toast.LENGTH_SHORT;
//    Toast.makeText(, "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex(),dur).show();
//    Toast.
  }
  public void methodMChart(PieChart mChartNew, View view){
    mChartNew = (PieChart) view.findViewById(R.id.piechart);
    mChartNew.setRotationEnabled(true);
    mChartNew.setDescription(new Description());
    mChartNew.setHoleRadius(35f);
    mChartNew.setTransparentCircleAlpha(0);
    mChartNew.setCenterText("PieChart");
    mChartNew.setCenterTextSize(10);
    mChartNew.setDrawEntryLabels(true);

    addDataSet(mChartNew);

    mChartNew.setOnChartValueSelectedListener(this);
  }

  public void methodComChart(CombinedChart comChart, View view) {
    comChart = (CombinedChart) view.findViewById(R.id.combinedChart);
    comChart.getDescription().setEnabled(false);
    comChart.setBackgroundColor(Color.WHITE);
    comChart.setDrawGridBackground(false);
    comChart.setDrawBarShadow(false);
    comChart.setHighlightFullBarEnabled(false);
    comChart.setOnChartValueSelectedListener(this);

    YAxis rightAxis = comChart.getAxisRight();
    rightAxis.setDrawGridLines(false);
    rightAxis.setAxisMinimum(0f);

    YAxis leftAxis = comChart.getAxisLeft();
    leftAxis.setDrawGridLines(false);
    leftAxis.setAxisMinimum(0f);

    final List<String> xLabel = new ArrayList<>();
    xLabel.add("Jan");
    xLabel.add("Feb");
    xLabel.add("Mar");
    xLabel.add("Apr");
    xLabel.add("May");
    xLabel.add("Jun");
    xLabel.add("Jul");
    xLabel.add("Aug");
    xLabel.add("Sep");
    xLabel.add("Oct");
    xLabel.add("Nov");
    xLabel.add("Dec");

    XAxis xAxis = comChart.getXAxis();
    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
    xAxis.setAxisMinimum(0f);
    xAxis.setGranularity(1f);

    CombinedData data = new CombinedData();
    LineData lineDatas = new LineData();
    lineDatas.addDataSet((ILineDataSet) dataChart());

    data.setData(lineDatas);

    xAxis.setAxisMaximum(data.getXMax() + 0.25f);

    comChart.setData(data);
    comChart.invalidate();
  }
  @Override
  public void onNothingSelected() {

  }
  private static void addDataSet(PieChart pieChart) {
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();
    List<Category> listCat = categoriesDAOImpl.getAllCategory();
    /*if(listCat != null && listCat.size() > 0) {
      for (Category cat : listCat ) {
        List<Transaction> listTran = transactionsDAOImpl.getStatisticalByCategory(cat.getId(), cat.getType().getValue());
        if(listTran != null && listTran.size() > 0) {
          int sum1 = 0;
          int sum2 = 0;
          for( Transaction tran: listTran ) {

          }
        }
      }
    }*/

    float[] yData = { 25, 40, 70 };
    String[] xData = { "January", "February", "January" };

    for (int i = 0; i < yData.length;i++){
      yEntrys.add(new PieEntry(yData[i],i));
    }
    for (int i = 0; i < xData.length;i++){
      xEntrys.add(xData[i]);
    }

    PieDataSet pieDataSet=new PieDataSet(yEntrys,"Report");
    pieDataSet.setSliceSpace(2);
    pieDataSet.setValueTextSize(12);

    ArrayList<Integer> colors=new ArrayList<>();
    colors.add(Color.GRAY);
    colors.add(Color.BLUE);
    colors.add(Color.RED);

    pieDataSet.setColors(colors);

    Legend legend=pieChart.getLegend();
    legend.setForm(Legend.LegendForm.CIRCLE);
    legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);

    PieData pieData=new PieData(pieDataSet);
    pieChart.setData(pieData);
    pieChart.invalidate();
  }
  private static DataSet dataChart() {

    LineData d = new LineData();
    int[] data = new int[] { 1, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1, 9 };

    ArrayList<Entry> entries = new ArrayList<Entry>();

    for (int index = 0; index < 12; index++) {
      entries.add(new Entry(index, data[index]));
    }

    LineDataSet set = new LineDataSet(entries, "Request Ots approved");
    set.setColor(Color.GREEN);
    set.setLineWidth(2.5f);
    set.setCircleColor(Color.GREEN);
    set.setCircleRadius(5f);
    set.setFillColor(Color.GREEN);
    set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
    set.setDrawValues(true);
    set.setValueTextSize(10f);
    set.setValueTextColor(Color.GREEN);

    set.setAxisDependency(YAxis.AxisDependency.LEFT);
    d.addDataSet(set);

    return set;
  }
}
