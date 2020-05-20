package com.kloudsync.techexcel5.ui;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kloudsync.techexcel5.R;
import com.kloudsync.techexcel5.app.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager mViewPager;
    private int[] mImageIdArray;
    private List<View> mViewList;
    private LinearLayout indicatorsLayout;
    private ImageView mImageView;
    private ImageView[] mImageViewArray;
    private TextView startText;
    private TextView skipText;
    private String[] titles1;
    private String[] titles2;

    @Override
    protected int setLayout() {
        return R.layout.activity_guide;
    }

    @Override
    protected void initView() {

        mViewPager = (ViewPager) findViewById(R.id.pager_guide);
        startText = findViewById(R.id.txt_start);
        skipText = findViewById(R.id.txt_skip);
        startText.setOnClickListener(this);
        skipText.setOnClickListener(this);
        mImageIdArray = new int[]{R.drawable.guide1, R.drawable.guide2, R.drawable.guide3, R.drawable.guide4};
        titles1 = new String[]{getResources().getString(R.string.guide_title_1_1), getResources().getString(R.string.guide_title_1_2), getResources().getString(R.string.guide_title_1_3), getResources().getString(R.string.guide_title_1_4)};
        titles2 = new String[]{getResources().getString(R.string.guide_title_2_1), getResources().getString(R.string.guide_title_2_2), getResources().getString(R.string.guide_title_2_3), getResources().getString(R.string.guide_title_2_4)};
        mViewList = new ArrayList<View>();

        for (int i = 0; i < mImageIdArray.length; i++) {
            View guideView = getLayoutInflater().inflate(R.layout.guide_item, null);
            ((ImageView) (guideView.findViewById(R.id.image))).setImageResource(mImageIdArray[i]);
            ((TextView) (guideView.findViewById(R.id.txt1))).setText(titles1[i]);
            ((TextView) (guideView.findViewById(R.id.txt2))).setText(titles2[i]);
            //
            mViewList.add(guideView);
        }
        mViewPager.setAdapter(new GuidePagerAdapter(mViewList));
        mViewPager.addOnPageChangeListener(this);
        initViewPagerTag();

    }

    private void initViewPagerTag() {
        indicatorsLayout = (LinearLayout) findViewById(R.id.layout_indicator);
        mImageViewArray = new ImageView[mViewList.size()];
        for (int i = 0; i < mViewList.size(); i++) {
            mImageView = new ImageView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(10, 0, 10, 0);
            mImageView.setLayoutParams(layoutParams);
            mImageViewArray[i] = mImageView;

            if (i == 0) {
                mImageView.setBackgroundResource(R.drawable.guide_indicator_focused);
            } else {
                mImageView.setBackgroundResource(R.drawable.guide_indicator);
            }
            //将数组中的imageview加入到viewgroup
            indicatorsLayout.addView(mImageViewArray[i]);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < mImageViewArray.length; i++) {
            mImageViewArray[position].setBackgroundResource(R.drawable.guide_indicator_focused);
            if (position != i) {
                mImageViewArray[i].setBackgroundResource(R.drawable.guide_indicator);
            }
        }
        //判断是否最后一页，是则显示button
        if (position == mImageViewArray.length - 1) {
//            mButton.setVisibility(View.VISIBLE);
            skipText.setVisibility(View.INVISIBLE);
            startText.setVisibility(View.VISIBLE);
            indicatorsLayout.setVisibility(View.INVISIBLE);
        } else {
//            mButton.setVisibility(View.GONE);
            skipText.setVisibility(View.VISIBLE);
            startText.setVisibility(View.INVISIBLE);
            indicatorsLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.txt_start || v.getId() == R.id.txt_skip) {
            finish();
        }
    }

    public class GuidePagerAdapter extends PagerAdapter {
        private List<View> viewList;

        public GuidePagerAdapter(List<View> viewList) {
            this.viewList = viewList;
        }

        /**
         * 返回页面的个数
         *
         * @return
         */
        @Override
        public int getCount() {
            if (viewList != null) {
                return viewList.size();
            }
            return 0;
        }

        /**
         * 判断是否由对象生成界面
         *
         * @param view
         * @param object
         * @return
         */
        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        /**
         * 初始化position位置的界面
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(viewList.get(position), 0);
            return viewList.get(position);
        }

        /**
         * 移除页面
         *
         * @param container
         * @param position
         * @param object
         */
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(viewList.get(position));
        }
    }


}
