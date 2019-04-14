package com.min.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.min.common.widget.helper.EditDecimalLengthFilter;

public class CellView extends FrameLayout {

    private TextView labelTv;
    private ClearableEditText valueEt;
    private TextView valueTv;
    private TextView unitTv;
    private ImageView arrowIv;

    private boolean isEditMode;
    private String label;
    private String value;
    private String placeholder;
    private String unit;
    private int inputType;
    private int maxLength;
    private int decimalLength;

    public CellView(Context context) {
        this(context, null);
    }

    public CellView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CellView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        initData(attrs);
        LayoutInflater.from(context).inflate(R.layout.view_cell, this);
        findView();
        fillDataToView();
    }

    private void fillDataToView() {
        labelTv.setText(label);
        if (isEditMode) {
            //输入模式
            valueEt.setVisibility(VISIBLE);
            valueTv.setVisibility(GONE);
            arrowIv.setVisibility(GONE);
            setInputType(inputType);
            if (maxLength > 0) {
                setMaxLength(maxLength);
            }
            if (!TextUtils.isEmpty(value)) {
                valueEt.setText(value);
            }
            if (!TextUtils.isEmpty(placeholder)) {
                valueEt.setHint(placeholder);
            }
        } else {
            //选择模式
            valueEt.setVisibility(GONE);
            valueTv.setVisibility(VISIBLE);
            arrowIv.setVisibility(VISIBLE);
            if (!TextUtils.isEmpty(value)) {
                valueTv.setText(value);
            }
        }

        if (!TextUtils.isEmpty(unit)) {
            unitTv.setVisibility(VISIBLE);
            unitTv.setText(unit);
        } else {
            unitTv.setVisibility(GONE);
        }
    }

    private void findView() {
        labelTv = findViewById(R.id.tv_label);
        valueEt = findViewById(R.id.et_value);
        valueTv = findViewById(R.id.tv_value);
        unitTv = findViewById(R.id.tv_unit);
        arrowIv = findViewById(R.id.iv_arrow);
    }

    private void initData(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CellView);
        isEditMode = typedArray.getBoolean(R.styleable.CellView_edit_mode, false);
        label = typedArray.getString(R.styleable.CellView_label);
        value = typedArray.getString(R.styleable.CellView_value);
        placeholder = typedArray.getString(R.styleable.CellView_placeholder);
        unit = typedArray.getString(R.styleable.CellView_unit);
        label = typedArray.getString(R.styleable.CellView_label);
        inputType = typedArray.getInt(R.styleable.CellView_input_type, 0);
        maxLength = typedArray.getInt(R.styleable.CellView_max_length, 0);
        decimalLength = typedArray.getInt(R.styleable.CellView_decimal_length, 2);
        typedArray.recycle();
    }

    public void setInputType(int type) {
        switch (type) {
            case 0:
                valueEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 1:  //数字
                valueEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:  //小数
                valueEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                setDecimalLength(decimalLength);
                break;
            case 3:  //密码
                valueEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD);
                break;
            case 4:  //数字 字母 标点符号
                valueEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            default:
                valueEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    public void setDecimalLength(int len) {
        valueEt.setFilters(new InputFilter[]{new EditDecimalLengthFilter(len)});
    }

    public void setMaxLength(int length) {
        valueEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (isEditMode) {
            if (enabled) {
                valueEt.setEnabled(true);
            } else {
                valueEt.setEnabled(false);
                valueEt.setClearIconVisible(false);
            }
        } else {
            arrowIv.setVisibility(enabled ? VISIBLE : GONE);
        }
    }

    public String getValue() {
        return isEditMode ? valueEt.getText().toString() : valueTv.getText().toString();
    }

    public void setValue(String value) {
        if (isEditMode) {
            valueEt.setText(value);
        } else {
            valueTv.setText(value);
        }
    }

}
