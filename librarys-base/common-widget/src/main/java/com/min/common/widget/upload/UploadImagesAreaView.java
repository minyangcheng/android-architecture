package com.min.common.widget.upload;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.min.common.widget.R;
import com.min.common.widget.recyclerview.BaseRecyclerViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by minyangcheng on 2019/04/12.
 */
public class UploadImagesAreaView extends LinearLayout implements BaseRecyclerViewAdapter.OnItemClickLitener {

    private static final String TAG = UploadImagesAreaView.class.getSimpleName();

    private static String LAST_OPERATE_NAMESPACE = "UploadImagesAreaView";

    private Object mHostObject;

    private RecyclerView mRv;
    private UploadImagesAreaAdapter mAdapter;

    private TextView mInfoTv;
    private TextView mExplainTv;

    private String[] mImageNameArr;
    private int[] mImageResArr;
    private int[] mNoCheckArr;
    private int mColumnNum;
    private String mHeadContent;
    private String explainText;
    private int mMaxNum;
    private int mFooterImgId;
    private float mRatio;
    private boolean mShowAddFlag;
    private boolean mShowAddImageInTail;

    private OnExplainListener mExplainListener;
    private OnImageCountChangeListener mOnImageCountChangeListener;
    private OnSingleUploadSuccessListener mOnSingleUploadSuccessListener;
    private OnSingleDeleteListener mOnSingleDeleteListener;
    public OnPlayFileListener mOnPlayFileListener;

    private AbstractUploadImagesHandler mUploadImagesHandler;

    private String nameSpace;
    private String mToastHint;

    private int mOperatePos;

    public UploadImagesAreaView(Context context) {
        this(context, null);
    }

    public UploadImagesAreaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImagesAreaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.UploadImagesAreaView, defStyle, 0);

        int textArrResId = a.getResourceId(R.styleable.UploadImagesAreaView_imageNameArr, 0);
        if (textArrResId != 0) {
            mImageNameArr = getContext().getResources().getStringArray(textArrResId);
        } else {
            mImageNameArr = new String[0];
        }
        int imageResId = a.getResourceId(R.styleable.UploadImagesAreaView_imageResArr, 0);
        if (imageResId != 0) {
            TypedArray ta = getContext().getResources().obtainTypedArray(imageResId);
            int len = ta.length();
            mImageResArr = new int[len];
            for (int i = 0; i < len; i++) {
                mImageResArr[i] = ta.getResourceId(i, 0);
            }
            ta.recycle();
        } else {
            mImageResArr = new int[0];
        }

        mMaxNum = a.getInt(R.styleable.UploadImagesAreaView_maxNum, 18);
        mShowAddFlag = a.getBoolean(R.styleable.UploadImagesAreaView_showAdd, false);
        mShowAddImageInTail = a.getBoolean(R.styleable.UploadImagesAreaView_showAddInTail, true);
        explainText = a.getString(R.styleable.UploadImagesAreaView_explainText);
        mFooterImgId = a.getResourceId(R.styleable.UploadImagesAreaView_footerImg, 0);
        String noCheckStr = a.getString(R.styleable.UploadImagesAreaView_noCheck);
        setNoCheckArr(noCheckStr);
        mHeadContent = a.getString(R.styleable.UploadImagesAreaView_headContent);
        mRatio = a.getFloat(R.styleable.UploadImagesAreaView_radio, 1.0f);
        mColumnNum = a.getInt(R.styleable.UploadImagesAreaView_columnNum, 3);
        nameSpace = a.getString(R.styleable.UploadImagesAreaView_namespace);
        if(TextUtils.isEmpty(nameSpace)){
            nameSpace = "UploadImagesAreaView";
        }
        mToastHint = a.getString(R.styleable.UploadImagesAreaView_toastHint);
        if(TextUtils.isEmpty(mToastHint)){
            mToastHint = "文件";
        }
        String handler = a.getString(R.styleable.UploadImagesAreaView_handler);
        if (TextUtils.isEmpty(handler)) {
            throw new RuntimeException("UploadImagesAreaView should been set UploadImagesHandler");
        }
        try {
            mUploadImagesHandler = (AbstractUploadImagesHandler) Class.forName(handler).getConstructor(UploadImagesAreaView.class).newInstance(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        a.recycle();

        if (mImageNameArr.length == 0 && mImageResArr.length == 0) {
            mShowAddFlag = true;
        }
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.view_upload_image_area, this, true);
        findViews();
        initRv();
        mAdapter.setData(createImageDataList());
        if (mShowAddFlag && mShowAddImageInTail) {
            addFooter();
        } else if (mShowAddFlag && !mShowAddImageInTail) {
            addHeader();
        }
        setHeaderContent();
    }

    private void initRv() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), mColumnNum);
        mRv.setLayoutManager(manager);
        mAdapter = new UploadImagesAreaAdapter(getContext(), mUploadImagesHandler);
        mAdapter.setOnItemClickLitener(this);
        mAdapter.setColumn(mColumnNum, mRatio);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnDeleteIvClickListener(new UploadImagesAreaAdapter.OnDeleteIvClickListener() {
            @Override
            public void onDeleteIvClick(int position) {
                UploadImageBean bean = mAdapter.getData().get(position);
                if (mOnSingleDeleteListener != null) {
                    mOnSingleDeleteListener.delete(position);
                } else {
                    if (bean.isAdd) {
                        deleteAddItem(position);
                    } else {
                        bean.status = UploadImageBean.UPLOAD_PREPARED;
                        bean.path = null;
                        bean.url = null;
                        bean.filePath = null;
                        bean.fileUrl = null;
                        bean.relationObj = null;
                        notifyDataSetChange();
                    }
                }
            }
        });
    }

    public void deleteAddItem(int position) {
        mAdapter.getData().remove(position);
        notifyDataSetChange();
        watchDataList();
    }

    private void setNoCheckArr(String str) {
        if (TextUtils.isEmpty(str)) return;
        if (str.contains(",")) {
            String[] strArr = str.split(",");
            mNoCheckArr = new int[strArr.length];
            for (int i = 0; i < strArr.length; i++) {
                try {
                    mNoCheckArr[i] = Integer.parseInt(strArr[i]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                mNoCheckArr = new int[1];
                mNoCheckArr[0] = Integer.parseInt(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean inNoCheckArr(int a) {
        if (mNoCheckArr == null || mNoCheckArr.length == 0) {
            return false;
        }
        for (int i : mNoCheckArr) {
            if (i == a) {
                return true;
            }
        }
        return false;
    }

    private void setHeaderContent() {
        View headView = findViewById(R.id.view_header_content);
        if (TextUtils.isEmpty(mHeadContent)) {
            headView.setVisibility(GONE);
        } else {
            headView.setVisibility(VISIBLE);
            mInfoTv.setText(mHeadContent);
            if (!TextUtils.isEmpty(explainText)) {
                mExplainTv.setVisibility(VISIBLE);
                mExplainTv.setText(explainText);
                mExplainTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mExplainListener != null) {
                            mExplainListener.explain();
                        }
                    }
                });
            }
        }
    }

    public void addFooter() {
        View view = getHeaderOrFooterView();
        mAdapter.setFooterView(view);
    }

    public void removeFooter() {
        mAdapter.removeFooter();
    }

    public void addHeader() {
        View view = getHeaderOrFooterView();
        mAdapter.setHeaderView(view);
    }

    public void removeHeader() {
        mAdapter.removeHeader();
    }

    private View getHeaderOrFooterView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_upload_image, this, false);
        UploadImageView uiv = view.findViewById(R.id.uiv);
        if (mFooterImgId == 0) {
            uiv.getImageView().setImageResource(R.drawable.bg_image_add);
        } else {
            uiv.getImageView().setImageResource(mFooterImgId);
        }
        uiv.getImageView().setScaleType(ImageView.ScaleType.FIT_XY);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter.getData().size() < mMaxNum) {
                    LAST_OPERATE_NAMESPACE = nameSpace;
                    mOperatePos = -1;
                    showSelectImageDialog();
                }
            }
        });
        return view;
    }

    private List<UploadImageBean> createImageDataList() {
        List<UploadImageBean> uploadImageBeanList = new ArrayList<>();
        UploadImageBean bean = null;
        for (int i = 0; i < mImageResArr.length; i++) {
            bean = new UploadImageBean(mImageResArr[i], mImageNameArr[i]);
            uploadImageBeanList.add(bean);
        }
        return uploadImageBeanList;
    }

    private void findViews() {
        mInfoTv = findViewById(R.id.tv_info);
        mExplainTv = findViewById(R.id.tv_explain);
        mRv = findViewById(R.id.rv);
    }

    @Override
    public void onItemClick(View view, int position) {
        UploadImageBean bean = mAdapter.getData().get(position);
        if (isEnabled()) {
            if (bean.status != UploadImageBean.UPLOAD_PREPARED) {
                mUploadImagesHandler.playFile(position);
            } else {
                LAST_OPERATE_NAMESPACE = nameSpace;
                mOperatePos = position;
                showSelectImageDialog();
            }
        } else {
            if (mOnPlayFileListener == null) {
                mUploadImagesHandler.playFile(position);
            } else {
                mOnPlayFileListener.playFile(position);
            }
        }
    }

    private void showSelectImageDialog() {
        mUploadImagesHandler.selectFile(mHostObject);
    }

    public void handleResult(int requestCode, int resultCode, Intent data) {
        mUploadImagesHandler.handleFile(requestCode, resultCode, data);
    }

    public void imagePicked(File imageFile) {
        if (!LAST_OPERATE_NAMESPACE.equals(nameSpace)) {
            return;
        }
        UploadImageBean bean;
        if (mOperatePos >= 0) {
            bean = mAdapter.getData().get(mOperatePos);
            bean.path = imageFile.getAbsolutePath();

            notifyDataSetChange();
            uploadFile(imageFile, mOperatePos);
        } else {
            bean = new UploadImageBean(imageFile.getAbsolutePath(), "");
            bean.isAdd = true;
            if (mShowAddImageInTail) {
                mAdapter.getData().add(bean);
                notifyDataSetChange();
                uploadFile(imageFile, mAdapter.getData().size() - 1);
            } else {
                mAdapter.getData().add(0, bean);
                notifyDataSetChange();
                uploadFile(imageFile, 0);
            }
            watchDataList();
        }
        mUploadImagesHandler.handleUploadImageBean(bean);
    }

    private void watchDataList() {
        if (!isEnabled()) {
            return;
        }
        int size = mAdapter.getData().size();
        if (mShowAddFlag && mShowAddImageInTail) {
            if (size < mMaxNum) {
                addFooter();
            } else if (size == mMaxNum) {
                mAdapter.removeFooter();
            }
        } else if (mShowAddFlag && !mShowAddImageInTail) {
            if (size < mMaxNum) {
                addHeader();
            } else if (size == mMaxNum) {
                mAdapter.removeHeader();
            }
        }
        if (mOnImageCountChangeListener != null) {
            mOnImageCountChangeListener.onChange(mAdapter.getData().size());
        }
    }

    private void uploadFile(File imageFile, final int pos) {
        updateStatusInPos_ing(pos);
        mUploadImagesHandler.uploadFile(imageFile, pos);
    }

    public boolean checkResult() {
        List<UploadImageBean> uploadImageList = mAdapter.getData();
        int mustInput = mImageResArr.length;
        UploadImageBean bean = null;
        String mess = null;
        for (int i = 0; i < uploadImageList.size(); i++) {
            bean = uploadImageList.get(i);
            if (bean.status == UploadImageBean.UPLOAD_PREPARED) {
                if (i >= 0 && i < mustInput && !inNoCheckArr(i)) {
                    mess = bean.name + "未上传...";
                    break;
                }
            }
            if (bean.status == UploadImageBean.UPLOAD_ING) {
                if (i >= 0 && i < mustInput) {
                    mess = bean.name + "正在上传中...";
                } else {
                    mess = "还有" + mToastHint + "正在上传中...";
                }
                break;
            }
            if (bean.status == UploadImageBean.UPLOAD_FAIL) {
                if (i >= 0 && i < mustInput) {
                    mess = bean.name + "上传失败...";
                } else {
                    mess = "有" + mToastHint + "上传失败...";
                }
                break;
            }
        }
        if (!TextUtils.isEmpty(mess)) {
            Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public List<String> getResult() {
        List<String> resultList = new ArrayList<>();
        for (int i = 0; i < mAdapter.getData().size(); i++) {
            resultList.add(mAdapter.getData().get(i).url);
        }
        return resultList;
    }

    public void setData(List<UploadImageBean> dataList) {
        mAdapter.setData(dataList);
        int size = dataList.size();
        mImageNameArr = new String[size];
        mImageResArr = new int[size];
        for (int i = 0; i < size; i++) {
            mImageNameArr[i] = dataList.get(i).name;
            mImageResArr[i] = dataList.get(i).resId;
        }
        watchDataList();
    }

    public String getNameSpace() {
        return nameSpace;
    }

    public void setData(int textArrResId, int imageResId) {
        mImageNameArr = getContext().getResources().getStringArray(textArrResId);

        TypedArray ta = getContext().getResources().obtainTypedArray(imageResId);
        int len = ta.length();
        mImageResArr = new int[len];
        for (int i = 0; i < len; i++) {
            mImageResArr[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        mAdapter.setData(createImageDataList());
        watchDataList();
    }

    public void updateStatusInPos_ing(int pos) {
        updateStatusInPos(pos, UploadImageBean.UPLOAD_ING, null);
    }

    public void updateStatusInPos_fail(int pos) {
        updateStatusInPos(pos, UploadImageBean.UPLOAD_FAIL, null);
    }

    public void updateStatusInPos_success(int pos, String url) {
        if (mOnSingleUploadSuccessListener != null) {
            mOnSingleUploadSuccessListener.success(pos, getData().get(pos));
        }
        updateStatusInPos(pos, UploadImageBean.UPLOAD_SUCCESS, url);
    }

    public void updateStatusInPos(int pos, int status, String url) {
        if (pos < getData().size()) {
            UploadImageBean bean = mAdapter.getData().get(pos);
            bean.status = status;
            bean.url = url;
            notifyDataSetChange();
        }
    }

    /**
     * @param hostObject hostObject Fragment 或 Activity
     * @param nameSpace  每个控件的命名控件
     */
    public void setHostActivity(Object hostObject, String nameSpace) {
        this.mHostObject = hostObject;
        this.nameSpace = nameSpace;
    }

    @Override
    protected void onDetachedFromWindow() {
        mUploadImagesHandler.destroy();
        super.onDetachedFromWindow();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        mAdapter.setEnable(enabled);
        if (mShowAddFlag) {
            if (enabled) {
                if (mShowAddImageInTail) {
                    addFooter();
                } else {
                    addHeader();
                }
            } else {
                if (mShowAddImageInTail) {
                    removeFooter();
                } else {
                    removeHeader();
                }
            }
        }
    }

    public void notifyDataSetChange() {
        mAdapter.notifyDataSetChanged();
    }

    public List<UploadImageBean> getData() {
        return mAdapter.getData();
    }

    public UploadImagesAreaAdapter getAdapter() {
        return mAdapter;
    }

    public Object getHost() {
        return mHostObject;
    }

    public void setOnExplainListener(OnExplainListener explainListener) {
        this.mExplainListener = explainListener;
    }

    public void setOnImageCountChangeListener(OnImageCountChangeListener listener) {
        mOnImageCountChangeListener = listener;
    }

    public void setOnSingleUploadSuccessListener(OnSingleUploadSuccessListener
                                                         mOnSingleUploadSuccessListener) {
        this.mOnSingleUploadSuccessListener = mOnSingleUploadSuccessListener;
    }

    public void setOnSingleDeleteListener(OnSingleDeleteListener onSingleDeleteListener) {
        this.mOnSingleDeleteListener = onSingleDeleteListener;
    }

    public void setOnShowGalleryListener(OnPlayFileListener listener) {
        this.mOnPlayFileListener = listener;
    }

    public interface OnExplainListener {
        void explain();
    }

    public interface OnImageCountChangeListener {
        void onChange(int count);
    }

    public interface OnSingleUploadSuccessListener {
        void success(int position, UploadImageBean bean);
    }

    public interface OnSingleDeleteListener {
        void delete(int position);
    }

    public interface OnPlayFileListener {
        void playFile(int pos);
    }

}
