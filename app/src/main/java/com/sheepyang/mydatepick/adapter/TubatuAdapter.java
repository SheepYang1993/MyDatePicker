package com.sheepyang.mydatepick.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sheepyang.mydatepick.R;

import java.util.List;

/**
 * Created by wujian on 2016/3/23.
 * RecyclingPagerAdapter是Jake WhartonAndroid大神封装的可用于复用的PagerAdapter。
 */
public class TubatuAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private List<String> mStrList;

    public TubatuAdapter(Context context, List<String> strList) {
        mContext = context;
        mStrList = strList;

    }

    public void updataList(List<String> list) {
        mStrList = list;
        notifyDataSetChanged();
    }

    public void addAll(List<String> list) {
        mStrList.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        String text = mStrList.get(position);
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_date_picker, null);
            vh.tv = (TextView) convertView.findViewById(R.id.text);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        vh.tv.setText(text);
        return convertView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public List<String> getStrList() {
        return mStrList;
    }

    @Override
    public int getCount() {
        return mStrList.size();
    }

    private class ViewHolder {
        TextView tv;

        public ViewHolder() {
        }
    }
}