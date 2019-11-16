package com.minhtzy.moneytracker.statistical;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.minhtzy.moneytracker.R;
import com.minhtzy.moneytracker.model.Transaction;

import java.util.ArrayList;

public class Fragment_Statistical_Tab0 extends Fragment implements View.OnClickListener {
    private static FragmentManager fragmentManager;
    FrameLayout fragmentControlThuChi, fragmentControlThuChiRight;
    View view;
    Animation animation ,animationright_out,animationleft_in;
    int TG=1,Loai=5;
    TextView txtthu0,txtchi0,textViewLich0;
    TextView txtthu1,txtchi1,textViewLich1;
    TextView txtthu2,txtchi2,textViewLich2;
    TextView txtthutruoc,txtchitruoc,textViewLichtruoc;
    TextView txtthu3,txtchi3,textViewLich3;
    TextView txtweekdays0,txtweekdays1,txtweekdays2,txtweekdays3;
    Context ct;
    ArrayList<Transaction> chiHomNay;
    RippleView lnload;
    public Fragment_Statistical_Tab0(){
    }
    @SuppressLint("ValidFragment")
    public Fragment_Statistical_Tab0(FragmentManager fragmentManager, FrameLayout fl, FrameLayout flRight) {
        fragmentControlThuChi=fl;
        this.fragmentManager=fragmentManager;
        fragmentControlThuChiRight=flRight;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.activity_layout_thongke_tab0, container, false);
        anhxa();
        return view;
    }
    public void anhxa(){
        txtthu0=(TextView)view.findViewById(R.id.txtthu0);
        txtchi0=(TextView)view.findViewById(R.id.txtchi0);
        textViewLich0=(TextView)view.findViewById(R.id.textViewLich0);
        txtthu1=(TextView)view.findViewById(R.id.txtthu1);
        txtchi1=(TextView)view.findViewById(R.id.txtchi1);
        textViewLich1=(TextView)view.findViewById(R.id.textViewLich1);
        txtthu2=(TextView)view.findViewById(R.id.txtthu2);
        txtchi2=(TextView)view.findViewById(R.id.txtchi2);
        textViewLich2=(TextView)view.findViewById(R.id.textViewLich2);

        txtthu2=(TextView)view.findViewById(R.id.txtThutruoc);
        txtchi2=(TextView)view.findViewById(R.id.txtchi2);
        textViewLich2=(TextView)view.findViewById(R.id.textViewLichTruoc);

        txtthu3=(TextView)view.findViewById(R.id.txtthu3);
        txtchi3=(TextView)view.findViewById(R.id.txtchi3);
        textViewLich3=(TextView)view.findViewById(R.id.textViewLich3);
        txtweekdays0=(TextView)view.findViewById(R.id.txtweekdays0);
        txtweekdays1=(TextView)view.findViewById(R.id.txtweekdays1);
        txtweekdays2=(TextView)view.findViewById(R.id.txtweekdays2);
        txtweekdays3=(TextView)view.findViewById(R.id.txtweekdays3);
    }
    @TargetApi(Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

    }

    private void thongbao(String nd){
        Toast.makeText(getContext(),nd, Toast.LENGTH_LONG).show();
    }
}
