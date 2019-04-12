package com.min.common.widget.helper;

import android.text.InputFilter;
import android.text.Spanned;

public class EditDecimalLengthFilter implements InputFilter {

    private int len;

    public EditDecimalLengthFilter(){
        this(2);
    }

    public EditDecimalLengthFilter(int len){
        len=len;
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end,
                               Spanned dest, int dstart, int dend) {
        if (dest.length() == 0 && source.equals(".")) {
            return "0.";
        }
        String dValue = dest.toString();
        String[] splitArray = dValue.split("\\.");
        if (splitArray.length > 1) {
            String dotValue = splitArray[1];
            if (dotValue.length() == len) {
                return "";
            }
        }
        return null;
    }

}
