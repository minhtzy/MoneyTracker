package com.minhtzy.moneytracker.view;

import android.content.Context;
import android.util.AttributeSet;

import com.minhtzy.moneytracker.utilities.CurrencyUtils;

import androidx.annotation.Nullable;

public class CurrencyTextView extends androidx.appcompat.widget.AppCompatTextView {

    String rawText;

    String currrencyCode;

    public CurrencyTextView(Context context) {
        super(context);
        init();
    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CurrencyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init()
    {
        currrencyCode = "VND";
    }
    @Override
    public void setText(CharSequence text, BufferType type) {
        rawText = text.toString();
        String currency = text.toString();
        try {
            currency = CurrencyUtils.getInstance().formatCurrency(currency,currrencyCode);
        }catch (Exception e){}

        super.setText(currency, type);
    }

    @Override
    public CharSequence getText() {
        return rawText;
    }

    public void setCurrrencyCode(String currrencyCode)
    {
        this.currrencyCode = currrencyCode;
    }
}
