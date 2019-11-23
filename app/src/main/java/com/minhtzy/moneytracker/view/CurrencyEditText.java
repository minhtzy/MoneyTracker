package com.minhtzy.moneytracker.view;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import com.minhtzy.moneytracker.entity.CurrencyFormat;
import com.minhtzy.moneytracker.utilities.CurrencyUtils;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class CurrencyEditText extends AppCompatEditText {

    private String current = "";
    private CurrencyEditText editText = CurrencyEditText.this;

    //properties
    private String currencyCode;

    public CurrencyEditText(Context context) {
        super(context);
        init();
    }

    public CurrencyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrencyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        if(currencyCode == null) currencyCode = "VND";
        this.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    double value = getCleanDoubleValue();

                    try {
                        String formatted = CurrencyUtils.formatCurrency(String.valueOf(value),currencyCode);
                        editText.setText(formatted);
                        editText.setSelection(formatted.length());
                    } catch (NumberFormatException e) {

                    }

                    editText.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        editText.setText("0");
    }

    /*
     *
     */
    public double getCleanDoubleValue() {
        double value = 0.0;
        value = CurrencyUtils.getCleanDouble(editText.getText().toString(),currencyCode);
        return value;
    }

    public int getCleanIntValue() {
        return (int) Math.floor(getCleanDoubleValue());
    }

    public void setCurrencyCode(String currencyCode)
    {
        this.currencyCode = currencyCode;
    }
}