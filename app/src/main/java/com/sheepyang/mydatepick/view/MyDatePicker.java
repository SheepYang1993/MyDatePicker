package com.sheepyang.mydatepick.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.sheepyang.mydatepick.R;
import com.sheepyang.mydatepick.adapter.TubatuAdapter;
import com.sheepyang.mydatepick.util.AppUtil;
import com.sheepyang.mydatepick.util.DateUtil;
import com.sheepyang.mydatepick.util.MyToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by SheepYang on 2016/8/27 14:51.
 */
public class MyDatePicker extends RelativeLayout {
    private static final int NEXT_CHECK = 1;
    private static final int PRE_CHECK = 2;
    private static final int DATA_TYPE_YEAR = 11;
    private static final int DATA_TYPE_MONTH = 12;
    private static final int DATA_TYPE_DAY = 13;
    private Context mContext;
    private TubatuAdapter mPagerAdapter;
    private ClipViewPager mViewPager;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private RadioGroup radioGroup;
    private RadioButton rbtnYear;
    private RadioButton rbtnMonth;
    private RadioButton rbtnDay;
    private int mStartYear = 1900;// 最早可选择的年份， 默认1900
    private int mCurrentYear;
    private int mCurrentMonth;
    private int mCurrentDay;

    public MyDatePicker(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public MyDatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public MyDatePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        initView();
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.my_date_picker, this);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        rbtnYear = (RadioButton) view.findViewById(R.id.rbtnYear);
        rbtnMonth = (RadioButton) view.findViewById(R.id.rbtnMonth);
        rbtnDay = (RadioButton) view.findViewById(R.id.rbtnDay);
        mCurrentYear = Integer.parseInt(DateUtil.getNowTime("yyyy"));
        mCurrentMonth = Integer.parseInt(DateUtil.getNowTime("MM"));
        mCurrentDay = Integer.parseInt(DateUtil.getNowTime("dd"));
        rbtnYear.setText(mCurrentYear + "年");
        rbtnMonth.setText(mCurrentMonth + "月");
        rbtnDay.setText(mCurrentDay + "日");
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (radioGroup.getCheckedRadioButtonId()) {
                    case R.id.rbtnYear:
                        addData(DATA_TYPE_YEAR, mCurrentYear);
                        break;
                    case R.id.rbtnMonth:
                        addData(DATA_TYPE_MONTH, mCurrentMonth);
                        break;
                    case R.id.rbtnDay:
                        addData(DATA_TYPE_DAY, mCurrentDay);
                        break;
                    default:
                        break;
                }
            }
        });
        initFindView(view);
    }

    private void initFindView(View view) {

        mViewPager = (ClipViewPager) view.findViewById(R.id.viewpager);
        /**调节ViewPager的滑动速度**/
        mViewPager.setSpeedScroller(300);
        /*设置页卡边距*/
        mViewPager.setPageMargin(AppUtil.dip2px(mContext, 10));
        /**给ViewPager设置缩放动画，这里通过PageTransformer来实现**/
//        mViewPager.setPageTransformer(true, new ScalePageTransformer());

        /**
         * 需要将整个页面的事件分发给ViewPager，不然的话只有ViewPager中间的view能滑动，其他的都不能滑动，
         * 这是肯定的，因为ViewPager总体布局就是中间那一块大小，其他的子布局都跑到ViewPager外面来了
         */
        view.findViewById(R.id.page_container).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 当手指按下的时候
                    x1 = event.getX();
                    y1 = event.getY();
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // 当手指抬起的时候
                    x2 = event.getX();
                    y2 = event.getY();
                    if (Math.abs(y1 - y2) > Math.abs(x1 - x2)) {// 上下滑动
                        if (y1 - y2 > AppUtil.dip2px(mContext, 50)) {// 上滑
                            changeDateChecked(PRE_CHECK);
                        } else if (y2 - y1 > AppUtil.dip2px(mContext, 50)) {// 下滑
                            changeDateChecked(NEXT_CHECK);
                        }
                        return true;
                    }
                }
                return mViewPager.dispatchTouchEvent(event);
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                String text = mPagerAdapter.getStrList().get(position);
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnYear) {
                    mCurrentYear = Integer.parseInt(text);
                    rbtnYear.setText(mCurrentYear + "年");
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnMonth) {
                    mCurrentMonth = Integer.parseInt(text);
                    rbtnMonth.setText(mCurrentMonth + "月");
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnDay) {
                    mCurrentDay = Integer.parseInt(text);
                    rbtnDay.setText(mCurrentDay + "日");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        List<String> strList = new ArrayList<String>();
        mPagerAdapter = new TubatuAdapter(mContext, strList);
        mViewPager.setAdapter(mPagerAdapter);
        addData(DATA_TYPE_YEAR, mCurrentYear);
    }

    public int getStartYear() {
        return mStartYear;
    }

    public void setStartYear(int startYear) {
        int nowYear = Integer.parseInt(DateUtil.getNowTime("yyyy"));
        if (startYear < nowYear) {
            this.mStartYear = startYear;
        }
    }

    /**
     * 切换年月日
     *
     * @param type
     */
    private void changeDateChecked(int type) {
        switch (type) {
            case PRE_CHECK:
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnDay) {
                    radioGroup.check(R.id.rbtnMonth);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnMonth) {
                    radioGroup.check(R.id.rbtnYear);
                }
                break;
            case NEXT_CHECK:
                if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnYear) {
                    radioGroup.check(R.id.rbtnMonth);
                } else if (radioGroup.getCheckedRadioButtonId() == R.id.rbtnMonth) {
                    radioGroup.check(R.id.rbtnDay);
                }
                break;
            default:
                break;
        }
    }

    private void addData(int type, int num) {
        List<String> list = new ArrayList<String>();
        int year = Integer.parseInt(DateUtil.getNowTime("yyyy"));
        switch (type) {
            case DATA_TYPE_YEAR:
                for (int i = mStartYear; i <= year; i++) {
                    list.add(i + "");
                }
                break;
            case DATA_TYPE_MONTH:
                for (int i = 1; i <= 12; i++) {
                    list.add(i + "");
                }
                break;
            case DATA_TYPE_DAY:
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, mCurrentMonth - 1);
                int dayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                for (int i = 1; i <= dayOfMonth; i++) {
                    list.add(i + "");
                }
                break;
            default:
                break;
        }
        /**这里需要将setOffscreenPageLimit的值设置成数据源的总个数，如果不加这句话，会导致左右切换异常；**/
        mViewPager.setOffscreenPageLimit(8);
        mPagerAdapter.updataList(list);
        if (num > 0) {
            if (type == DATA_TYPE_YEAR) {
                int positon = num - mStartYear;
                mViewPager.setCurrentItem(positon, false);
            } else {
                mViewPager.setCurrentItem(num - 1, false);
            }
        }
    }
}
