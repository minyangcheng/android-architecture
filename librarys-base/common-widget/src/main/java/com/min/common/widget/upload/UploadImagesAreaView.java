package com.min.common.widget.upload;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
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

    public String PREFIX = "UploadImagesAreaView";
    public String ADD_PREFIX = "UploadImagesAreaViewAdd";

    private Object mHostObject;
    private Context mContext;

    private RecyclerView mRv;
    private UploadImagesAreaAdapter mAdapter;

    private TextView mInfoTv;
    private TextView mExplainTv;

    private String[] mImageNameArr;
    private int[] mImageResArr;
    private int[] mNoCheckArr;
    private int mColumnNum;
    private String mHeadContent;
    private int mMaxNum;
    private int mFooterImgId;
    private boolean mShowExplainFlag;
    private String explainText;
    private int mItemHeight;
    private boolean mShowAddFlag;
    private boolean mShowAddImageInTail;

    private OnAddImageListener mOnAddImageListener;
    private OnExplainListener mExplainListener;
    private OnImageCountChangeListener mOnImageCountChangeListener;
    private OnSingleUploadSuccessListener mOnSingleUploadSuccessListener;
    private OnSingleDeleteListener onSingleDeleteListener;
    public OnShowGalleryListener mOnShowGalleryListener;

    private IUploadImagesHandler mUploadImagesHandler;

    public UploadImagesAreaView(Context context) {
        this(context, null);
    }

    public UploadImagesAreaView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadImagesAreaView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.UploadImagesAreaView, defStyle, 0);

        int textArrResId = a.getResourceId(R.styleable.UploadImagesAreaView_imageNameArr, 0);
        if (textArrResId != 0) {
            mImageNameArr = mContext.getResources().getStringArray(textArrResId);
        } else {
            mImageNameArr = new String[0];
        }
        int imageResId = a.getResourceId(R.styleable.UploadImagesAreaView_imageResArr, 0);
        if (imageResId != 0) {
            TypedArray ta = mContext.getResources().obtainTypedArray(imageResId);
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
        mShowExplainFlag = a.getBoolean(R.styleable.UploadImagesAreaView_showExplain, false);
        explainText = a.getString(R.styleable.UploadImagesAreaView_explainText);
        mFooterImgId = a.getResourceId(R.styleable.UploadImagesAreaView_footerImg, 0);
        String noCheckStr = a.getString(R.styleable.UploadImagesAreaView_noCheck);
        setNoCheckArr(noCheckStr);
        mHeadContent = a.getString(R.styleable.UploadImagesAreaView_headContent);
        mItemHeight = a.getDimensionPixelOffset(R.styleable.UploadImagesAreaView_itemHeight, 0);
        mColumnNum = a.getInt(R.styleable.UploadImagesAreaView_columnNum, 3);
        a.recycle();

        if (mImageNameArr.length == 0 && mImageResArr.length == 0) {
            mShowAddFlag = true;
        }
        initView();
    }

    private void initView() {
        LayoutInflater.from(mContext).inflate(R.layout.view_upload_image_area, this, true);
        findViews();
        mAdapter.setData(createImageDataList());
        if (mShowAddFlag && mShowAddImageInTail) {
            addFooter();
        } else if (mShowAddFlag && !mShowAddImageInTail) {
            addHeader();
        }
        setHeaderContent();
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
            mInfoTv = findViewById(R.id.tv_info);
            mExplainTv = findViewById(R.id.tv_explain);
            if (mShowExplainFlag) {
                mExplainTv.setVisibility(VISIBLE);
                mExplainTv.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mExplainListener != null) {
                            mExplainListener.explain();
                        }
                    }
                });
                if (!TextUtils.isEmpty(explainText)) {
                    mExplainTv.setText(explainText);
                }
            } else {
                mExplainTv.setVisibility(GONE);
            }
            mInfoTv.setText(mHeadContent);
        }
    }

    public void setHeaderContent(String headerContent) {
        if (mInfoTv != null) {
            mInfoTv.setText(headerContent);
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_upload_image, this, false);
        UploadImageView uiv = (UploadImageView) view.findViewById(R.id.uiv);
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
                    addImage();
                }
            }
        });
        return view;
    }

    private void addImage() {
        showSelectImageDialog(ADD_PREFIX + "_");
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
        mRv = findViewById(R.id.rv);
        GridLayoutManager manager = new GridLayoutManager(mContext, mColumnNum);
        mRv.setLayoutManager(manager);
        mAdapter = new UploadImagesAreaAdapter(mContext, mUploadImagesHandler);
        mAdapter.setOnItemClickLitener(this);
        mAdapter.setClomNum(mColumnNum);
        mRv.setAdapter(mAdapter);
        mAdapter.setOnDeleteIvClickListener(new UploadImagesAreaAdapter.OnDeleteIvClickListener() {
            @Override
            public void onDeleteIvClick(int position) {
                UploadImageBean bean = mAdapter.getData().get(position);
                if (onSingleDeleteListener != null) {
                    onSingleDeleteListener.delete(position);
                } else {
                    if (bean.isAdd) {
                        deleteLocalImage(position);
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

    public void deleteLocalImage(int position) {
        mAdapter.getData().remove(position);
        notifyDataSetChange();
        watchDataList();
    }

    @Override
    public void onItemClick(View view, int position) {
        UploadImageBean bean = mAdapter.getData().get(position);
        if (isEnabled()) {
            if (bean.status != UploadImageBean.UPLOAD_PREPARED) {
                showGallery(position);
            } else {
                String prefix = PREFIX + "_" + position + "_";
                showSelectImageDialog(prefix);
            }
        } else {
            if (mOnShowGalleryListener == null) {
                showGallery(position);
            } else {
                mOnShowGalleryListener.showGallery(position);
            }
        }

    }

    private void showGallery(int position) {

    }

    private void showSelectImageDialog(final String prefix) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
//                .setItems(new String[]{"拍照", "相册"}, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        if (which == 0) {
//                            mUploadImagesHandler.openCamera(prefix, mHostObject);
//                        } else if (which == 1) {
//                            mUploadImagesHandler.openGalleryPicker(prefix, mHostObject);
//                        }
//                    }
//                });
//        builder.show();
        mUploadImagesHandler.selectFile(prefix,mHostObject);
    }

    public void handleResult(UploadImagesAreaView uploadImagesAreaView, int requestCode, int resultCode, Intent data) {
        mUploadImagesHandler.handleResult(this, requestCode, resultCode, data);
    }

    private void imagePicked(File imageFile) {
        String prefix = getPrefixFromFileName(imageFile.getName());
        if (!TextUtils.isEmpty(prefix)) {
            if (prefix.equals(PREFIX)) {
                int pos = getPositionFromFileName(imageFile.getName());
                if (pos >= 0) {
                    UploadImageBean uploadImageBean = mAdapter.getData().get(pos);
                    uploadImageBean.path = imageFile.getAbsolutePath();

                    notifyDataSetChange();
                    uploadImage(imageFile, pos);
                }
            } else if (prefix.equals(ADD_PREFIX)) {
                UploadImageBean bean = new UploadImageBean(imageFile.getAbsolutePath(), "");

                bean.isAdd = true;
                if (mShowAddImageInTail) {
                    mAdapter.getData().add(bean);
                    notifyDataSetChange();
                    uploadImage(imageFile, mAdapter.getData().size() - 1);
                } else {
                    mAdapter.getData().add(0, bean);
                    notifyDataSetChange();
                    uploadImage(imageFile, 0);
                }
                watchDataList();
            }
        }
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

    private void uploadImage(final File imageFile, final int pos) {
        mUploadImagesHandler.uploadFile(this,imageFile,pos);
    }

    public boolean checkResult() {
        String hintStr = "";
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
                    mess = "还有" + hintStr + "正在上传中...";
                }
                break;
            }
            if (bean.status == UploadImageBean.UPLOAD_FAIL) {
                if (i >= 0 && i < mustInput) {
                    mess = bean.name + "上传失败...";
                } else {
                    mess = "还有" + hintStr + "上传失败...";
                }
                break;
            }
        }
        if (!TextUtils.isEmpty(mess)) {
            Toast.makeText(mContext, mess, Toast.LENGTH_SHORT).show();
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

    public List<UploadImageBean> getUploadImageBeanList() {
        return mAdapter.getData();
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

    public void setData(int textArrResId, int imageResId) {
        mImageNameArr = mContext.getResources().getStringArray(textArrResId);

        TypedArray ta = mContext.getResources().obtainTypedArray(imageResId);
        int len = ta.length();
        mImageResArr = new int[len];
        for (int i = 0; i < len; i++) {
            mImageResArr[i] = ta.getResourceId(i, 0);
        }
        ta.recycle();
        mAdapter.setData(createImageDataList());
        watchDataList();
    }

    private void updateStatusInPos(int pos, int status) {
        if (pos < getData().size()) {
            updateStatusInPosAndUrl(pos, status, null);
        }
    }

    private void updateStatusInPosAndUrl(int pos, int status, String url) {
        if (pos < getData().size()) {
            UploadImageBean bean = mAdapter.getData().get(pos);
            bean.status = status;
            bean.url = url;
            notifyDataSetChange();
        }
    }

    private int getPositionFromFileName(String fileName) {
        String[] arr = fileName.split("_");
        int pos = -1;
        try {
            pos = Integer.parseInt(arr[1]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pos;
    }

    private String getPrefixFromFileName(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return null;
        }
        String[] arr = fileName.split("_");
        return arr[0];
    }

    public void setHostActivity(Object hostObject, String prefix) {
        Fragment fragment = hostObject instanceof Fragment ? (Fragment) hostObject : null;
        Activity activity = hostObject instanceof Activity ? (Activity) hostObject : null;
        if (fragment == null && activity == null) return;
        Context context = fragment != null ? fragment.getContext() : activity;

        this.mHostObject = hostObject;
        this.mContext = context;
        if (!TextUtils.isEmpty(prefix)) {
            this.PREFIX = prefix;
            this.ADD_PREFIX = prefix + "Add";
        }
    }

    @Override
    protected void onDetachedFromWindow() {
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

    public String[] getImageNameArr() {
        return mImageNameArr;
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

    public void setOnExplainListener(OnExplainListener explainListener) {
        this.mExplainListener = explainListener;
    }

    public void setOnImageCountChangeListener(OnImageCountChangeListener listener) {
        mOnImageCountChangeListener = listener;
    }

    public void setOnSingleUploadSuccessListener(OnSingleUploadSuccessListener mOnSingleUploadSuccessListener) {
        this.mOnSingleUploadSuccessListener = mOnSingleUploadSuccessListener;
    }

    public void setOnSingleDeleteListener(OnSingleDeleteListener onSingleDeleteListener) {
        this.onSingleDeleteListener = onSingleDeleteListener;
    }

    public void setOnShowGalleryListener(OnShowGalleryListener listener) {
        this.mOnShowGalleryListener = listener;
    }

    public interface OnAddImageListener {
        void addImage(int position);
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

    public interface OnShowGalleryListener {
        void showGallery(int pos);
    }

    public void setHideCameraIndicator() {
        mAdapter.setHideCameraIndicator();
    }

}
