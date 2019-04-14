package com.min.core.view;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.DatePicker;

import com.min.core.R;

import java.lang.reflect.Field;

/**
 * Created by minych on 18-2-28.
 */

public class CustomDatePickerDialog extends AlertDialog implements DialogInterface.OnClickListener, DatePicker.OnDateChangedListener {

    private static final String START_YEAR = "start_year";
    private static final String START_MONTH = "start_month";
    private static final String START_DAY = "start_day";

    private int year;
    private int month;
    private int day;

    private boolean hideDay;

    private DatePickerDialog.OnDateSetListener listener;
    private DatePicker datePicker;

    public CustomDatePickerDialog(Context context, int year, int month, DatePickerDialog.OnDateSetListener listener) {
        this(context, year, month, 0, listener);
    }

    public CustomDatePickerDialog(Context context, int year, int month, int day, DatePickerDialog.OnDateSetListener listener) {
        super(context);
        this.year = year;
        this.month = month - 1;
        if (day <= 0) {
            day = 1;
            hideDay = true;
        }
        this.day = day;
        this.listener = listener;
        init();
    }

    private void init() {
        setTitle("选择日期");
        setButton(BUTTON_POSITIVE, "确定", this);
        setButton(BUTTON_NEGATIVE, "取消", this);

        View view = getLayoutInflater().inflate(R.layout.dialog_date_time_picker, null);
        setView(view);
        view.findViewById(R.id.timePicker).setVisibility(View.GONE);
        datePicker = view.findViewById(R.id.datePicker);
        datePicker.init(year, month, day, this);
        if (hideDay) {
            hideDay(datePicker);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == BUTTON_POSITIVE) {
            listener.onDateSet(datePicker, year, month + 1, day);
        }
    }

    @Override
    public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    private void hideDay(DatePicker mDatePicker) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                if (daySpinnerId != 0) {
                    View daySpinner = mDatePicker.findViewById(daySpinnerId);
                    if (daySpinner != null) {
                        daySpinner.setVisibility(View.GONE);
                    }
                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        Object dayPicker = new Object();
                        try {
                            dayPicker = datePickerField.get(mDatePicker);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                        ((View) dayPicker).setVisibility(View.GONE);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Bundle onSaveInstanceState() {
        Bundle state = super.onSaveInstanceState();
        state.putInt(START_YEAR, year);
        state.putInt(START_MONTH, month);
        state.putInt(START_DAY, day);
        return state;
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        year = savedInstanceState.getInt(START_YEAR);
        month = savedInstanceState.getInt(START_MONTH);
        day = savedInstanceState.getInt(START_DAY);
        datePicker.init(year, month, day, this);
    }

}
