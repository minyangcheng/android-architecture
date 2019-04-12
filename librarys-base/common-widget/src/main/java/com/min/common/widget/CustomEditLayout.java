package com.min.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.min.common.widget.helper.EditDecimalLengthFilter;
import com.min.common.widget.helper.InputTxtFilter;

/**
 * Created by qbm on 2016/1/4.
 */
public class CustomEditLayout extends LinearLayout {

    private TextView mLeftTv;
    private ClearableEditText mInputEt;
    private TextView mRightTv;

    private String mLeftStr;
    private int mInputType;
    private String mHint;
    private boolean mInputByLeft;
    private int maxLength;
    private String mUnitStr;
    private int mDecimalLen;  //小数位数
    private int etPadding;

    public CustomEditLayout(Context context) {
        this(context, null);
    }

    public CustomEditLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomEditLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomEditLayout);
        mLeftStr = typedArray.getString(R.styleable.CustomEditLayout_left_tag);
        mInputType = typedArray.getInt(R.styleable.CustomEditLayout_input_type, 0);
        mHint = typedArray.getString(R.styleable.CustomEditLayout_hint);
        mInputByLeft = typedArray.getBoolean(R.styleable.CustomEditLayout_input_by_left, true);
        maxLength = typedArray.getInt(R.styleable.CustomEditLayout_max_length, -1);
        mUnitStr = typedArray.getString(R.styleable.CustomEditLayout_unit);
        mDecimalLen = typedArray.getInt(R.styleable.CustomEditLayout_decimal_length, 2);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.view_custom_edit, this, true);
        mLeftTv = (TextView) view.findViewById(R.id.tv_left);
        mInputEt = (ClearableEditText) view.findViewById(R.id.et_input);
        mRightTv = (TextView) view.findViewById(R.id.tv_right);

        if (maxLength > 0) {
            setMaxLength(maxLength);
        }
        setInputType(mInputType);
        mInputEt.setPadding(etPadding, 0, 0, 0);
        mInputEt.setHint(mHint);
        if (mInputByLeft) {
            mInputEt.setGravity(Gravity.LEFT);
        } else {
            mInputEt.setGravity(Gravity.RIGHT);
        }
        mLeftTv.setText(mLeftStr);

        if (mUnitStr != null) {
            mRightTv.setText(mUnitStr);
        } else {
            mRightTv.setVisibility(GONE);
        }
    }


    public void setInputType(int type) {
        switch (type) {
            case 0:  //普通
                mInputEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case 1:  //数字
                mInputEt.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case 2:  //小数
                mInputEt.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                setmDecimalLenToView(mDecimalLen);
                break;
            case 3:  //密码
                mInputEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType
                        .TYPE_TEXT_VARIATION_WEB_PASSWORD);
                break;
            case 4:  //中文
                InputTxtFilter.inputFilter(getContext(), mInputEt, InputTxtFilter.INPUT_TYPE_CH, maxLength == 0 ? 10 : maxLength);
                break;
            default:
                mInputEt.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
    }

    public void setHint(String hint) {
        mInputEt.setPadding(etPadding, 0, 0, 0);
        mInputEt.setHint(hint);
    }

    public void setTextColor(int color) {
        mInputEt.setTextColor(color);
    }

    public String getText() {
        return mInputEt.getText().toString();
    }

    public void setText(String str) {
        if (str == null) return;
        mInputEt.setPadding(etPadding, 0, 0, 0);
        mInputEt.setText(str);
    }

    public void setTransformationMethod(TransformationMethod method) {
        mInputEt.setTransformationMethod(method);
    }

    public void setSelection(int index) {
        mInputEt.setSelection(index);
    }

    public void setMaxLength(int length) {
        mInputEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
    }

    public void setmDecimalLenToView(int len) {
        mInputEt.setFilters(new InputFilter[]{new EditDecimalLengthFilter(len)});
    }

    public ClearableEditText getEditText() {
        return mInputEt;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mInputEt.setEnabled(enabled);
        mInputEt.setClearIconVisible(enabled);
    }

}
