package com.example.t2m.moneytracker.statistical;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.dataaccess.CategoriesDAOImpl;
import com.example.t2m.moneytracker.dataaccess.ITransactionsDAO;
import com.example.t2m.moneytracker.dataaccess.IWalletsDAO;
import com.example.t2m.moneytracker.dataaccess.TransactionsDAOImpl;
import com.example.t2m.moneytracker.model.Category;
import com.example.t2m.moneytracker.model.MTDate;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StatisticalTabFragment extends Fragment implements OnChartValueSelectedListener {
  private PieChart mChart;
  private CombinedChart comChart;
  private EditText mTextFromDate;
  private EditText mTextToDate;
  private Calendar mCalendar;
  private Spinner dropdown;
  private Spinner dropdown2;
  ITransactionsDAO iTransactionsDAO;
  static TransactionsDAOImpl transactionsDAOImpl;
  static CategoriesDAOImpl categoriesDAOImpl;

  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.activity_statistical_tab,container,false);
    transactionsDAOImpl = new TransactionsDAOImpl(getContext());
    categoriesDAOImpl = new CategoriesDAOImpl(getContext());
    // methodMChart(mChart, view);
//    methodComChart(comChart, view);
    methodSpinner2(view);
    methodDate(view);
    ArrayAdapter<CharSequence> adapter1  = methodSpinner(view);
    ArrayAdapter<CharSequence> adapter2  = methodSpinner2(view);
    return view;
  }

  /* Xử lý thời gian - start*/
  public void methodDate (View view) {
    mTextFromDate = view.findViewById(R.id.text_from_date);
    mTextToDate = view.findViewById(R.id.text_to_date);
    mCalendar = Calendar.getInstance();

    mTextFromDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onClickedDate(v);
      }
    });
    mTextToDate.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        onClickedDate(v);
      }
    });
  }
  private void onClickedDate(View v) {
    DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
      @Override
      public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        mCalendar.set(year,month,dayOfMonth);
        updateLabelDate();
      }
    };
    int year = mCalendar.get(Calendar.YEAR);
    int month = mCalendar.get(Calendar.MONTH);
    int date = mCalendar.get(Calendar.DATE);
    new DatePickerDialog(getContext(),dateSetListener,year,month,date).show();
  }
  private void updateLabelDate() {
    Date date = mCalendar.getTime();
    if(DateUtils.isToday(date.getTime())) {
      mTextFromDate.setText("Từ ngày");
      mTextToDate.setText("Đến ngày");
    }
    else {
      String strDate = new MTDate(mCalendar).toIsoDateShortTimeString();
      mTextFromDate.setText(strDate);
      mTextToDate.setText(strDate);
    }
  }
  /* Xử lý thời gian - end*/

  /* Xử lý dropdown - start*/
  public ArrayAdapter<CharSequence> methodSpinner (View view) {
    dropdown = view.findViewById(R.id.spinner1);

    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
      R.array.planets_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    dropdown.setAdapter(adapter);

    return adapter;
  }
  public ArrayAdapter<CharSequence> methodSpinner2 (View view) {
    dropdown2 = view.findViewById(R.id.spinner2);

    ArrayAdapter<java.lang.CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
      R.array.planets_array2, android.R.layout.simple_spinner_item);
    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    dropdown2.setAdapter(adapter2);

    return adapter2;
  }

  /* Xử lý dropdown - end*/

  @Override
  public void onValueSelected(Entry e, Highlight h) {
//    int dur = Toast.LENGTH_SHORT;
//    Toast.makeText(, "Value: " + e.getY() + ", index: " + h.getX() + ", DataSet index: " + h.getDataSetIndex(),dur).show();
//    Toast.
  }

  /* Xử lý biểu đồ tròn - start*/
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

  private static void addDataSet(PieChart pieChart) {
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();
    List<Category> listCat = categoriesDAOImpl.getAllCategory();
    if(listCat != null && listCat.size() > 0) {
      actionCalucator(listCat);
    }

    float[] yData = { 25, 40, 35};
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
    colors.add(Color.GREEN);
    colors.add(Color.RED);

    pieDataSet.setColors(colors);

    Legend legend=pieChart.getLegend();
    legend.setForm(Legend.LegendForm.CIRCLE);
    legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);

    PieData pieData=new PieData(pieDataSet);
    pieChart.setData(pieData);
    pieChart.invalidate();
  }
  /* Xử lý biểu đồ tròn - end*/


//
//  public void methodComChart(CombinedChart comChart, View view) {
//    comChart = (CombinedChart) view.findViewById(R.id.combinedChart);
//    comChart.getDescription().setEnabled(false);
//    comChart.setBackgroundColor(Color.WHITE);
//    comChart.setDrawGridBackground(false);
//    comChart.setDrawBarShadow(false);
//    comChart.setHighlightFullBarEnabled(false);
//    comChart.setOnChartValueSelectedListener(this);
//
//    YAxis rightAxis = comChart.getAxisRight();
//    rightAxis.setDrawGridLines(false);
//    rightAxis.setAxisMinimum(0f);
//
//    YAxis leftAxis = comChart.getAxisLeft();
//    leftAxis.setDrawGridLines(false);
//    leftAxis.setAxisMinimum(0f);
//
//    final List<String> xLabel = new ArrayList<>();
//    xLabel.add("Jan");
//    xLabel.add("Feb");
//    xLabel.add("Mar");
//    xLabel.add("Apr");
//    xLabel.add("May");
//    xLabel.add("Jun");
//    xLabel.add("Jul");
//    xLabel.add("Aug");
//    xLabel.add("Sep");
//    xLabel.add("Oct");
//    xLabel.add("Nov");
//    xLabel.add("Dec");
//
//    XAxis xAxis = comChart.getXAxis();
//    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//    xAxis.setAxisMinimum(0f);
//    xAxis.setGranularity(1f);
//
//    CombinedData data = new CombinedData();
//    LineData lineDatas = new LineData();
//    lineDatas.addDataSet((ILineDataSet) dataChart());
//
//    data.setData(lineDatas);
//
//    xAxis.setAxisMaximum(data.getXMax() + 0.25f);
//
//    comChart.setData(data);
//    comChart.invalidate();
//  }
  @Override
  public void onNothingSelected() {

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

  public static void initSum() {
    float SUM_EAT = 0; // tiền dành cho ăn uống
    float SUM_BILL_UTILITIES = 0; // tiền dành cho hóa đơn và tiện ích
    float SUM_GO = 0; // tiền dành cho việc di chuyển
    float SUM_SHOPPING = 0; //tiền dành cho việc mua sắm
    float SUM_FRIEND_LOVELY = 0; // baạn bè và người yêu
    float SUM_RELAX = 0; // giải trí
    float SUM_TRAVEL = 0; //Du lịch
    float SUM_HEALTHY = 0; // Sức khỏe
    float SUM_GIFTS = 0; //QUà tặng
    float SUM_FAMILY = 0; // gia đình
    float SUM_EDUCATION  = 0; //Giáo dục
  }
  public static  void actionCalucator(List<Category> listCat) {
    initSum();
    for (Category cat : listCat ) {
      // Lấy ra danh sách giao dịch theo loại
      List<Transaction> listTran = transactionsDAOImpl.getStatisticalByCategory(cat.getId(), cat.getType().getValue());
      if(listTran != null && listTran.size() > 0) {

        for( Transaction tran: listTran ) {
          if(tran.getCategory().getType().getValue()== 1) {

          } else if (tran.getCategory().getType().getValue() == 2) {

          } else if (tran.getCategory().getType().getValue() == 3) {

          } else if (tran.getCategory().getType().getValue() == 4) {

          }
        }
      }
    }
  }
}
