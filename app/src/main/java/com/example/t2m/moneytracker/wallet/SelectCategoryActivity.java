package com.example.t2m.moneytracker.wallet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.adpter.AdapterListCategory;
import com.example.t2m.moneytracker.dataaccess.MoneyTrackerDBHelper;
import com.example.t2m.moneytracker.model.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class SelectCategoryActivity extends AppCompatActivity {

    Button btnBacktoaddwallet;
    ArrayList<TransactionType> arrayListListchoose;
    ArrayAdapter arrayAdapterListchoose;
    ListView lvListchoose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listchoose);
        
        addControls();
        addEvents();

        ShowAllListChoose();
    }

    private void ShowAllListChoose() {
//        Login.database = openOrCreateDatabase(Login.DATABASE_NAME,MODE_PRIVATE,null);
//        Cursor cursor = Login.database.rawQuery("SELECT * FROM listchoose",null);
//        arrayListListchoose.clear();
//        while (cursor.moveToNext()){
//            String ten = cursor.getString(1);
//            String anh = cursor.getString(2);
//
//            ListChoose listChoose = new ListChoose();
//            listChoose.setTen(ten);
//            listChoose.setAnh(anh);
//            arrayListListchoose.add(listChoose);
//        }
//        cursor.close();
        MoneyTrackerDBHelper dbHelper = new MoneyTrackerDBHelper(this);
        List<TransactionType> types = dbHelper.getAllTransactionType();
        arrayListListchoose.addAll(types);
        arrayAdapterListchoose.notifyDataSetChanged();

    }

    private void addEvents() {
        btnBacktoaddwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addControls() {
        btnBacktoaddwallet = (Button)findViewById(R.id.btnBacktoaddwallet);
        lvListchoose = (ListView)findViewById(R.id.lvListchoose);
        arrayListListchoose = new ArrayList<>();
        arrayAdapterListchoose = new AdapterListCategory(
                SelectCategoryActivity.this,
                R.layout.custom_item_category,
                arrayListListchoose
        );
        lvListchoose.setAdapter(arrayAdapterListchoose);

        lvListchoose.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TransactionType transactionType = arrayListListchoose.get(position);
                Intent intent = new Intent();
                intent.putExtra("transaction_type",transactionType);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }
}
