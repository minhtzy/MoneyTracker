package com.example.t2m.moneytracker.wallet;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.t2m.moneytracker.R;
import com.example.t2m.moneytracker.account.Login;
import com.example.t2m.moneytracker.model.ListChoose;

import java.util.ArrayList;

public class listchoose extends AppCompatActivity {

    Button btnBacktoaddwallet;
    ArrayList<ListChoose> arrayListListchoose;
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
        Login.database = openOrCreateDatabase(Login.DATABASE_NAME,MODE_PRIVATE,null);
        Cursor cursor = Login.database.rawQuery("SELECT * FROM listchoose",null);
        arrayListListchoose.clear();
        while (cursor.moveToNext()){
            String ten = cursor.getString(1);
            String anh = cursor.getString(2);

            ListChoose listChoose = new ListChoose();
            listChoose.setTen(ten);
            listChoose.setAnh(anh);
            arrayListListchoose.add(listChoose);
        }
        cursor.close();
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
        arrayAdapterListchoose = new ArrayAdapter(
                listchoose.this,
                R.layout.customlistchoose,
                arrayListListchoose
        );
        lvListchoose.setAdapter(arrayAdapterListchoose);
    }
}
