package com.libs.core.web;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.libs.core.R;
import com.libs.core.R2;
import com.libs.core.common.base.BaseRxActivity;
import com.libs.core.common.utils.LogUtils;
import com.libs.core.common.view.preview.PhotoView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 网页图片预览
 *
 * @author zhang.zheng
 * @version 2018-01-23
 */
public class WebPreviewActivity extends BaseRxActivity {

    @BindView(R2.id.view_pager)
    ViewPager mViewPager;

    private String[] mImageUrls;
    private int mCurrIndex;

    private View mCurrView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_preview_image;
    }

    @Override
    protected void initPresenter() {
        // 获取图片数据
        mImageUrls = getIntent().getStringArrayExtra(JSAppBridge.ALL_IMAGE_URLS);
        String currImgUrl = getIntent().getStringExtra(JSAppBridge.CURR_IMAGE_URL);

        // 筛选图片索引
        if (mImageUrls == null || TextUtils.isEmpty(currImgUrl))
            return;
        for (int i = 0; i < mImageUrls.length; i++) {
            if (currImgUrl.equals(mImageUrls[i])) {
                mCurrIndex = i;
                break;
            }
            LogUtils.d("ImagePreview", mImageUrls.length + ":" + currImgUrl + "-" + mCurrIndex);
        }
    }

    @Override
    protected void initViewData(Bundle bundle) {
        mViewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mImageUrls != null ? mImageUrls.length : 0;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                PhotoView view = new PhotoView(mContext);
                view.enable();
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
                Glide.with(mContext).load(mImageUrls[position]).into(view);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }

            @Override
            public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                mCurrView = (View) object;
            }
        });
        mViewPager.setCurrentItem(mCurrIndex);
    }

    @OnClick({R2.id.save_btn})
    void onClick(View v) {
        if (v.getId() == R.id.save_btn) {
            savePhotoToLocal();
        }
    }

    @Override
    protected void onDestroy() {
        if (mViewPager != null) {
            mViewPager.removeAllViews();
            mViewPager = null;
        }
        super.onDestroy();
    }

    /**
     * 保存图片
     */
    private void savePhotoToLocal() {
        if (mCurrView == null)
            return;
        PhotoView photoView = (PhotoView) mCurrView;
        BitmapDrawable glideBitmap = (BitmapDrawable) photoView.getDrawable();
        if (glideBitmap == null || glideBitmap.getBitmap() == null)
            return;
        showLoading("正在保存");
        SaveHelper.savePhoto(this, glideBitmap.getBitmap(), new SaveHelper.SaveResultCallback() {
            @Override
            public void onSavedSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("保存成功");
                        hideLoading();
                    }
                });
            }

            @Override
            public void onSavedFailed() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToast("保存失败");
                        hideLoading();
                    }
                });
            }
        });
    }
}
