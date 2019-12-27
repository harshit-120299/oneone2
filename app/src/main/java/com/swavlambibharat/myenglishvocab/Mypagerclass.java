package com.swavlambibharat.myenglishvocab;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

public class Mypagerclass extends PagerAdapter {
    private LayoutInflater inflater;
    private int[] layouts;
    private Context context;

    public Mypagerclass(int[] layouts, Context context) {
        this.layouts = layouts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v=inflater.inflate(layouts[position],container,false);
        container.addView(v);
        return v;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View v=(View)object;
        container.removeView(v);
    }
}
