package com.example.t2m.moneytracker.statistical;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.PopupMenu;
import android.text.SpannableString;
import android.text.format.DateUtils;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.dataaccess.CategoriesDAOImpl;
import com.example.t2m.moneytracker.dataaccess.ITransactionsDAO;
import com.example.t2m.moneytracker.dataaccess.TransactionsDAOImpl;
import com.example.t2m.moneytracker.model.Category;
import com.example.t2m.moneytracker.model.MTDate;
import com.example.t2m.moneytracker.model.Transaction;
import com.example.t2m.moneytracker.model.Wallet;
import com.example.t2m.moneytracker.utilities.TransactionsManager;
import com.example.t2m.moneytracker.utilities.WalletsManager;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StatisticalTabFragment extends Fragment implements OnChartValueSelectedListener {
  private PieChart mChart, mChartChi, mChartThu, mChartVayNo;

  private CombinedChart comChart;
  private EditText mTextFromDate, mTextToDate;
  private Calendar mCalendar;
  private Spinner dropdown, dropdown2;
  private BarChart barChart;
  private RelativeLayout relativeLayoutBarChart, relativeLayoutMchart, spinnerThongKeTheoNhon, relativeLayoutMchartChi, relativeLayoutMchartThu, relativeLayoutMchartVayNo;
  ITransactionsDAO iTransactionsDAO;
  static TransactionsDAOImpl transactionsDAOImpl;
  static CategoriesDAOImpl categoriesDAOImpl;
  private LinearLayout textDateLinearLayout;
  protected Typeface mTfLight, mTfRegular;
  View view;
  protected static String[] nhom = new String[] {
          "Khoản chi", "Nợ/Cho vay", "Khoản thu"
  };
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.activity_statistical_tab,container,false);
    transactionsDAOImpl = new TransactionsDAOImpl(getContext());
    categoriesDAOImpl = new CategoriesDAOImpl(getContext());
    this.addControls(view);
    this.methodDate();
    this.methodSpinner2();
    this.getMapData();
    return view;
  }

  private void addControls(View view) {
    mTextFromDate = view.findViewById(R.id.text_from_date);
    mTextToDate = view.findViewById(R.id.text_to_date);
    dropdown = view.findViewById(R.id.spinner1);
    dropdown2 = view.findViewById(R.id.spinner2);
    mChart = view.findViewById(R.id.piechart);
    mChartChi = view.findViewById(R.id.chartChi1);
    mChartThu = view.findViewById(R.id.chartThu);
    mChartVayNo = view.findViewById(R.id.chartVayNo);
    mCalendar = Calendar.getInstance();
    comChart =  view.findViewById(R.id.combinedChart);
    barChart = view.findViewById(R.id.bar_chart);
    relativeLayoutBarChart = view.findViewById(R.id.bar_chart_relative_layout);
    relativeLayoutMchart = view.findViewById(R.id.piechart_relative_layout);
    spinnerThongKeTheoNhon = view.findViewById(R.id.spinner1_relative_layout);
    textDateLinearLayout = view.findViewById(R.id.dateId);
    relativeLayoutMchartChi = view.findViewById(R.id.piechart_relative_layout_chi);
    relativeLayoutMchartThu = view.findViewById(R.id.piechart_relative_layout_thu);
    relativeLayoutMchartVayNo = view.findViewById(R.id.piechart_relative_layout_vayNo);
  }
  /* Xử lý thời gian - start*/
  public void methodDate () {
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
  public void methodSpinner () {
    ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
      R.array.planets_array, android.R.layout.simple_spinner_item);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

    dropdown.setAdapter(adapter);
    dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        long item = parent.getItemIdAtPosition(position);

        if(item == 1) {
         methodMChartChi(item, mChartChi);
         methodSetVisiable(view.INVISIBLE , view.INVISIBLE, view.VISIBLE, view.VISIBLE,view.INVISIBLE,view.INVISIBLE );
        } else if (item == 0) {
          methodMChart();
          methodSetVisiable(view.VISIBLE , view.INVISIBLE, view.INVISIBLE, view.VISIBLE ,view.INVISIBLE,view.INVISIBLE );
        } else if (item == 2) {
          methodMChartChi(item, mChartThu);
          methodSetVisiable(view.INVISIBLE , view.INVISIBLE, view.INVISIBLE, view.VISIBLE,view.VISIBLE,view.INVISIBLE );
        } else if (item == 3) {
          methodMChartChi(item, mChartVayNo);
          methodSetVisiable(view.INVISIBLE , view.INVISIBLE, view.INVISIBLE, view.VISIBLE,view.INVISIBLE,view.VISIBLE );
        }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }
  public void methodSpinner2 () {
    ArrayAdapter<java.lang.CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(),
      R.array.planets_array2, android.R.layout.simple_spinner_item);
    adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    dropdown2.setAdapter(adapter2);


    dropdown2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
         long item = parent.getItemIdAtPosition(position);
         if(item == 0) {
           methodMChart();
           methodSetVisiable(view.VISIBLE , view.INVISIBLE, view.INVISIBLE, view.VISIBLE,view.INVISIBLE,view.INVISIBLE );
           methodSpinner();
         } else if (item > 0) {
           methodBarchart();
           methodSetVisiable(view.INVISIBLE , view.VISIBLE, view.INVISIBLE, view.INVISIBLE,view.INVISIBLE,view.INVISIBLE );
         }
      }

      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }
    });
  }

  /* Xử lý dropdown - end*/

  @Override
  public void onValueSelected(Entry e, Highlight h) {
  }

  /* Xử lý biểu đồ tròn - start*/
  public void methodMChart(){
    mChart.setRotationEnabled(true);
    mChart.setDescription(new Description());
    mChart.setHoleRadius(35f);
    mChart.setTransparentCircleAlpha(0);
    mChart.setCenterText("Tổng hợp");
    mChart.setCenterTextSize(10);
    mChart.setDrawEntryLabels(true);

    addDataSet(mChart);

    mChart.setOnChartValueSelectedListener(this);
  }

  private void addDataSet(PieChart pieChart) {
    ArrayList<PieEntry> yEntrys = new ArrayList<>();
    ArrayList<String> xEntrys = new ArrayList<>();
    List<Transaction> listChi = new ArrayList<Transaction>();
    List<Transaction> listThu = new ArrayList<Transaction>();
    List<Transaction> listVayNo = new ArrayList<Transaction>();
    Map<String, List<Transaction>> mapAll = getMapData();
    int sum = 0;
    List<Category> listCat = categoriesDAOImpl.getAllCategory();
    if(listCat != null && listCat.size() > 0) {
      actionCalucator(listCat);
    }

    if ( !mapAll.isEmpty() && mapAll != null) {
        listChi = mapAll.get("Khoản Chi");
        listThu = mapAll.get("Khoản Thu");
        listVayNo = mapAll.get("Cho Vay/Nợ");
        sum = listChi.size() + listThu.size() + listVayNo.size();
    }
//    for (int i = 0; i < 3 ; i++) {
//      yEntrys.add(new PieEntry((float) ((Math.random() * 100) + 100 / 5), nhom[i % nhom.length]));
//    }
      if (!listChi.isEmpty() && listChi != null) {
          int size = listChi.size();
          float tiLe = (float)size/sum;
          yEntrys.add(new PieEntry(tiLe*100 , "Chi"));
      }
      if (!listThu.isEmpty() && listThu != null) {
          int size = listThu.size();
          float tiLe = (float)size/sum;
          yEntrys.add(new PieEntry(tiLe*100, "Thu"));
      }
      if (!listVayNo.isEmpty() && listVayNo != null) {
          int size = listVayNo.size();
          float tiLe = (float)size/sum;
          yEntrys.add(new PieEntry(tiLe*100, "Cho vay/ nợ"));
      }

    PieDataSet pieDataSet=new PieDataSet(yEntrys,"Ghi chú");
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

  public void methodMChartChi(long item, PieChart chart) {


    chart.setUsePercentValues(true);
    chart.setExtraOffsets(5, 10, 5, 5);
    chart.setDragDecelerationFrictionCoef(0.95f);
    chart.setCenterTextTypeface(mTfLight);
    chart.setCenterText(generateCenterSpannableText());
    chart.setDrawHoleEnabled(true);
    chart.setHoleColor(Color.WHITE);
    chart.setTransparentCircleColor(Color.WHITE);
    chart.setTransparentCircleAlpha(110);
    chart.setHoleRadius(58f);
    chart.setTransparentCircleRadius(61f);
    chart.setDrawCenterText(true);
    chart.setRotationAngle(0);
    chart.setRotationEnabled(true);
    chart.setHighlightPerTapEnabled(true);
    chart.setOnChartValueSelectedListener(this);

    setData(item, chart);

    chart.animateY(1400, Easing.EaseInOutQuad);
    Legend l = chart.getLegend();
    l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
    l.setOrientation(Legend.LegendOrientation.VERTICAL);
    l.setDrawInside(false);
    l.setXEntrySpace(7f);
    l.setYEntrySpace(0f);
    l.setYOffset(0f);
    chart.setEntryLabelColor(Color.WHITE);
    chart.setEntryLabelTypeface(mTfRegular);
    chart.setEntryLabelTextSize(12f);
  }

  private SpannableString generateCenterSpannableText() {
    SpannableString s = new SpannableString("Thống kê Theo Loại");
    s.setSpan(new RelativeSizeSpan(1.7f), 0, 18, 0);
    return s;
  }
  private void setData(long item, PieChart chart) {
    List<Transaction> listChi = new ArrayList<Transaction>();
    List<Transaction> listThu = new ArrayList<Transaction>();
    List<Transaction> listVayNo = new ArrayList<Transaction>();
    Map<String, List<Transaction>> mapAll = getMapData();

    if ( !mapAll.isEmpty() && mapAll != null) {
      listChi = mapAll.get("Khoản Chi");
      listThu = mapAll.get("Khoản Thu");
      listVayNo = mapAll.get("Cho Vay/Nợ");
    }
    ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
    if (item == 1 ) {
      entries = setPieEntryThongKeKhoanChi(listChi);
    } else if (item == 2) {
      entries = setPieEntryThongKeKhoanThu(listThu);
    } else if (item == 3){
      entries = setPieEntryThongKeKhoanVayNo(listVayNo);
    }


    PieDataSet dataSet = new PieDataSet(entries, null);
    dataSet.setSliceSpace(3f);
    dataSet.setSelectionShift(5f);

    ArrayList<Integer> colors = new ArrayList<Integer>();

   /* for (int c : ColorTemplate.VORDIPLOM_COLORS)
      colors.add(c);

    for (int c : ColorTemplate.JOYFUL_COLORS)
      colors.add(c);*/

    for (int c : ColorTemplate.COLORFUL_COLORS)
      colors.add(c);

    /*for (int c : ColorTemplate.LIBERTY_COLORS)
      colors.add(c);

    for (int c : ColorTemplate.PASTEL_COLORS)
      colors.add(c);*/

    colors.add(ColorTemplate.getHoloBlue());

    dataSet.setColors(colors);

    PieData data = new PieData(dataSet);
    data.setValueFormatter(new PercentFormatter());
    data.setValueTextSize(11f);
    data.setValueTextColor(Color.BLACK);
    data.setValueTypeface(mTfLight);
    chart.setData(data);
    chart.highlightValues(null);
    chart.invalidate();
  }
  private void methodSetVisiable(int set1 , int set2, int setChi, int set4, int setThu, int setVayNo) {
    mChart.setVisibility(set1);
    relativeLayoutMchart.setVisibility(set1);

    barChart.setVisibility(set2);
    relativeLayoutBarChart.setVisibility(set2);

    spinnerThongKeTheoNhon.setVisibility(set4);
    dropdown.setVisibility(set4);

    textDateLinearLayout.setVisibility(set2);
    mTextFromDate.setVisibility(set2);
    mTextToDate.setVisibility(set2);

    relativeLayoutMchartChi.setVisibility(setChi);
    mChartChi.setVisibility(setChi);

    relativeLayoutMchartThu.setVisibility(setThu);
    mChartThu.setVisibility(setThu);

    relativeLayoutMchartVayNo.setVisibility(setVayNo);
    mChartVayNo.setVisibility(setVayNo);
  }
  /* Xử lý biểu đồ tròn - end*/

  @Override
  public void onNothingSelected() {

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


  /* Vẽ biểu đồ cột - start */
  private void methodBarchart() {
    barChart.setDrawBarShadow(false);
    barChart.setDrawValueAboveBar(true);
    Description description = new Description();
    description.setText("");
    barChart.setDescription(description);
    barChart.setMaxVisibleValueCount(50);
    barChart.setPinchZoom(false);
    barChart.setDrawGridBackground(false);

    XAxis xl = barChart.getXAxis();
    xl.setGranularity(1f);
    xl.setCenterAxisLabels(true);

    YAxis leftAxis = barChart.getAxisLeft();
    leftAxis.setDrawGridLines(false);
    leftAxis.setSpaceTop(30f);
    barChart.getAxisRight().setEnabled(false);

    //data
    float groupSpace = 0.04f;
    float barSpace = 0.02f;
    float barWidth = 0.46f;

    int startYear = 1980;
    int endYear = 1985;

    List<BarEntry> yVals1 = new ArrayList<BarEntry>();
    List<BarEntry> yVals2 = new ArrayList<BarEntry>();

    for (int i = startYear; i < endYear; i++) {
      yVals1.add(new BarEntry(i, 0.4f));
    }

    for (int i = startYear; i < endYear; i++) {
      yVals2.add(new BarEntry(i, 0.7f));
    }

    BarDataSet set1, set2;

    if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
      set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
      set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
      set1.setValues(yVals1);
      set2.setValues(yVals2);
      barChart.getData().notifyDataChanged();
      barChart.notifyDataSetChanged();
    } else {
      set1 = new BarDataSet(yVals1, "Company A");
      set1.setColor(Color.rgb(104, 241, 175));
      set2 = new BarDataSet(yVals2, "Company B");
      set2.setColor(Color.rgb(164, 228, 251));

      ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
      dataSets.add(set1);
      dataSets.add(set2);

      BarData data = new BarData(dataSets);
      barChart.setData(data);
    }

    barChart.getBarData().setBarWidth(barWidth);
    barChart.groupBars(startYear, groupSpace, barSpace);
    barChart.invalidate();

  }
  /* Vẽ biểu đồ cột - end */

  /* xứ lý logic */
  private Map<String, List<Transaction>> getMapData() {
    Wallet wallet = WalletsManager.getInstance(getContext()).getCurrentWallet();
    Map<String, List<Transaction>> mapAll = new HashMap<String, List<Transaction>>();
    List<Transaction> listAll = transactionsDAOImpl.getAllTransactionByWalletId(wallet.getWalletId());
    List<Transaction> listChi = new ArrayList<Transaction>();
    List<Transaction> listThu = new ArrayList<Transaction>();
    List<Transaction> listVayNo = new ArrayList<Transaction>();
    for (Transaction trans : listAll) {
      Category cat = trans.getCategory();
      int type = cat.getType().getValue();
      if (type == 1) {
        listChi.add(trans);
      } else if (type == 2 ) {
        listThu.add(trans);
      } else if (type == 3 || type == 4) {
        listVayNo.add(trans);
      }
    }
    mapAll.put("Khoản Chi", listChi);
    mapAll.put("Khoản Thu", listThu);
    mapAll.put("Cho Vay/Nợ", listVayNo);
    return mapAll;
  }

  private Map<String, List<Category>> thongKeKhoanChi(List<Transaction> listChi) {
    Map<String, List<Category>> mapChi = new HashMap<>();
    List<Category> anUong = new ArrayList<Category>();
    List<Category> hoaDonTienIch = new ArrayList<Category>();
    List<Category> diChuyen = new ArrayList<Category>();
    List<Category> muaSam = new ArrayList<Category>();
    List<Category> giaTri = new ArrayList<Category>();
    List<Category> sucKhoe = new ArrayList<Category>();
    List<Category> quaTang = new ArrayList<Category>();
    List<Category> giaDinh = new ArrayList<Category>();
    List<Category> khac = new ArrayList<Category>();
    for (Transaction trans: listChi) {
      Category cat = trans.getCategory();
      int parentId = cat.getId();
      if(cat.getParentCategory() != null) 
        parentId = cat.getParentCategory().getId();
      if (parentId == 1) {
        anUong.add(cat);
      } else if (parentId == 5) {
        hoaDonTienIch.add(cat);
      } else if (parentId == 13) {
        diChuyen.add(cat);
      } else if (parentId == 18) {
        muaSam.add(cat);
      } else if (parentId == 24) {
        giaTri.add(cat);
      } else if (parentId == 28) {
        sucKhoe.add(cat);
      } else if (parentId == 33) {
        quaTang.add(cat);
      } else if (parentId == 37) {
        giaDinh.add(cat);
      } else {
        khac.add(cat);
      }
    }

    mapChi.put("Ăn uống", anUong);
    mapChi.put("Hóa đơn & Tiện ích", hoaDonTienIch);
    mapChi.put("Di chuyển", diChuyen);
    mapChi.put("Mua sắm", muaSam);
    mapChi.put("Giải trí", giaTri);
    mapChi.put("Sức khỏe", sucKhoe);
    mapChi.put("Quà tặng & Quyên góp", quaTang);
    mapChi.put("Gia đình", giaDinh);
    mapChi.put("Khác", khac);
    return mapChi;
  }

  private ArrayList<PieEntry> setPieEntryThongKeKhoanChi (List<Transaction> listChi) {
    ArrayList<PieEntry> entries = new ArrayList<>();
    List<Category> anUong = new ArrayList<Category>();
    List<Category> hoaDonTienIch = new ArrayList<Category>();
    List<Category> diChuyen = new ArrayList<Category>();
    List<Category> muaSam = new ArrayList<Category>();
    List<Category> giaTri = new ArrayList<Category>();
    List<Category> sucKhoe = new ArrayList<Category>();
    List<Category> quaTang = new ArrayList<Category>();
    List<Category> giaDinh = new ArrayList<Category>();
    List<Category> khac = new ArrayList<Category>();

    for (Transaction trans: listChi) {
      Category cat = trans.getCategory();
      int parentId = cat.getId();
      if(cat.getParentCategory() != null) 
        parentId = cat.getParentCategory().getId();
      if (parentId == 1) {
        anUong.add(cat);
      } else if (parentId == 5) {
        hoaDonTienIch.add(cat);
      } else if (parentId == 13) {
        diChuyen.add(cat);
      } else if (parentId == 18) {
        muaSam.add(cat);
      } else if (parentId == 24) {
        giaTri.add(cat);
      } else if (parentId == 28) {
        sucKhoe.add(cat);
      } else if (parentId == 33) {
        quaTang.add(cat);
      } else if (parentId == 37) {
        giaDinh.add(cat);
      } else {
        khac.add(cat);
      }
    }

    long sum = anUong.size() + hoaDonTienIch.size()+ diChuyen.size()+ muaSam.size()
            + giaTri.size()+ sucKhoe.size()+ quaTang.size()+ giaDinh.size()+ khac.size();
    float anUongPT = phanTram(sum, anUong);
    if (anUongPT > 0) {
      entries.add(new PieEntry(anUongPT, "Ăn uống"));
    }
    float hoaDonTienIchPT = phanTram(sum, hoaDonTienIch);
    if (hoaDonTienIchPT > 0) {
      entries.add(new PieEntry(hoaDonTienIchPT, "Hóa đơn & Tiện ích"));
    }
    float diChuyenPT = phanTram(sum, diChuyen);
    if (diChuyenPT > 0) {
      entries.add(new PieEntry(diChuyenPT, "Di chuyển"));
    }
    float muaSamPT = phanTram(sum, muaSam);
    if (muaSamPT > 0) {
      entries.add(new PieEntry(muaSamPT, "Mua sắm"));
    }
    float giaTriPT = phanTram(sum, giaTri);
    if (giaTriPT > 0) {
      entries.add(new PieEntry(giaTriPT, "Giải trí"));
    }
    float sucKhoePT = phanTram(sum, sucKhoe);
    if (sucKhoePT > 0) {
      entries.add(new PieEntry(sucKhoePT, "Sức khỏe"));
    }
    float quaTangPT = phanTram(sum, quaTang);
    if (quaTangPT > 0) {
      entries.add(new PieEntry(quaTangPT, "Quà tặng & Quyên góp"));
    }
    float giaDinhPT = phanTram(sum, giaDinh);
    if (giaDinhPT > 0) {
      entries.add(new PieEntry(giaDinhPT, "Gia đình"));
    }
    float khacPT = phanTram(sum, khac);
    if (khacPT > 0) {
      entries.add(new PieEntry(khacPT, "Khác"));
    }
    return entries;
  }

  private float phanTram(long sum, List<Category> list) {
    float phanTram = 0;
    if (!list.isEmpty() && list != null) {
      int size = list.size();
      float tiLe = (float)size/sum;
      phanTram = tiLe*100;
    }
    return phanTram;
  }

  private ArrayList<PieEntry> setPieEntryThongKeKhoanThu (List<Transaction> listThu) {
    ArrayList<PieEntry> entries = new ArrayList<>();
    List<Category> thuong = new ArrayList<Category>();
    List<Category> tienLai = new ArrayList<Category>();
    List<Category> luong = new ArrayList<Category>();
    List<Category> duocTang = new ArrayList<Category>();
    List<Category> banDo = new ArrayList<Category>();
    List<Category> khoanThuKhac = new ArrayList<Category>();


    for (Transaction trans: listThu) {
      Category cat = trans.getCategory();
      int id = cat.getId();
      if (id == 51) {
        thuong.add(cat);
      } else if (id == 52) {
        tienLai.add(cat);
      } else if (id == 53) {
        luong.add(cat);
      } else if (id == 54) {
        duocTang.add(cat);
      } else if (id == 55) {
        banDo.add(cat);
      } else if (id == 56) {
        khoanThuKhac.add(cat);
      }
    }

    long sum = thuong.size() + tienLai.size()+ luong.size()+ duocTang.size()
            + banDo.size()+ khoanThuKhac.size();
    float thuongPT = phanTram(sum, thuong);
    if (thuongPT > 0) {
      entries.add(new PieEntry(thuongPT, "Thưởng"));
    }
    float tienLaiPT = phanTram(sum, tienLai);
    if (tienLaiPT > 0) {
      entries.add(new PieEntry(tienLaiPT, "Tiền lãi"));
    }
    float luongPT = phanTram(sum, luong);
    if (luongPT > 0) {
      entries.add(new PieEntry(luongPT, "Lương"));
    }
    float duocTangPT = phanTram(sum, duocTang);
    if (duocTangPT > 0) {
      entries.add(new PieEntry(duocTangPT, "Được tặng"));
    }
    float banDoPT = phanTram(sum, banDo);
    if (banDoPT > 0) {
      entries.add(new PieEntry(banDoPT, "Bán đồ"));
    }
    float khoanThuKhacPT = phanTram(sum, khoanThuKhac);
    if (khoanThuKhacPT > 0) {
      entries.add(new PieEntry(khoanThuKhacPT, "Khoản thu khác"));
    }

    return entries;
  }
  private ArrayList<PieEntry> setPieEntryThongKeKhoanVayNo (List<Transaction> listVayNo) {
    ArrayList<PieEntry> entries = new ArrayList<>();
    List<Category> choVay = new ArrayList<Category>();
    List<Category> traNo = new ArrayList<Category>();
    List<Category> diVay = new ArrayList<Category>();
    List<Category> thuNo = new ArrayList<Category>();

    for (Transaction trans: listVayNo) {
      Category cat = trans.getCategory();
      int id = cat.getId();
      if (id == 57) {
        choVay.add(cat);
      } else if (id == 58) {
        traNo.add(cat);
      } else if (id == 59) {
        diVay.add(cat);
      } else if (id == 60) {
        thuNo.add(cat);
      }
    }

    long sum = choVay.size() + traNo.size()+ diVay.size()+ thuNo.size();
    float choVayPT = phanTram(sum, choVay);
    if (choVayPT > 0) {
      entries.add(new PieEntry(choVayPT, "Cho vay"));
    }
    float traNoPT = phanTram(sum, traNo);
    if (traNoPT > 0) {
      entries.add(new PieEntry(traNoPT, "Trả nợ"));
    }
    float diVayPT = phanTram(sum, diVay);
    if (diVayPT > 0) {
      entries.add(new PieEntry(diVayPT, "Đi vay"));
    }
    float thuNoPT = phanTram(sum, thuNo);
    if (thuNoPT > 0) {
      entries.add(new PieEntry(thuNoPT, "Thu nợ"));
    }

    return entries;
  }
}
