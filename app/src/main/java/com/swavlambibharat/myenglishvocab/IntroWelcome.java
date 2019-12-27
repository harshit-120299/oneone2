package com.swavlambibharat.myenglishvocab;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IntroWelcome extends AppCompatActivity {

    ViewPager viewPager;
    LinearLayout dotintro;
    private TextView[] dotstv;
    private int[] layouts;
    private Button next;
    private Button skip;
    private Mypagerclass mypagerclass ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isFirstTimeStartApp()){
            startmainactivity();
            finish();
        }
        setStatusBarTransparent();
        dotintro=findViewById(R.id.dotintro);
        viewPager=findViewById(R.id.view_pager);
        next=findViewById(R.id.btn_next);
        skip=findViewById(R.id.btn_skip);
        setContentView(R.layout.activity_intro_welcome);


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startmainactivity();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentPage=viewPager.getCurrentItem()+1;
                if(currentPage<layouts.length)
                {
                    viewPager.setCurrentItem(currentPage);
                }else {
                    startmainactivity();
                }

            }
        });
        layouts=new int[]{R.layout.slide_1,R.layout.slide_2};
        mypagerclass=new Mypagerclass(layouts,getApplicationContext());
        viewPager.setAdapter(mypagerclass);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position==layouts.length-1){
                    next.setText(R.string.start);
                    skip.setVisibility(View.GONE);
                }
                else {
                    next.setText(R.string.app_name);
                    next.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }
    private boolean isFirstTimeStartApp(){
        SharedPreferences ref=getApplication().getSharedPreferences("IntroAppslider", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag",true);
    }
    private void setFirstTimeStartStatus(boolean stt){
        SharedPreferences ref=getApplication().getSharedPreferences("IntroAppslider", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=ref.edit();
        editor.putBoolean("FirstTimeStartFlag",stt);
        editor.apply();

    }
    private void setDotStatus(int page){
        dotintro.removeAllViews();
        dotstv=new TextView[layouts.length];
        for (int i=0;i<dotstv.length;i++){
            dotstv[i]=new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("3a9b4bb;"));
            dotintro.addView(dotstv[i]);
        }
        if(dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }
    private void startmainactivity(){
        setFirstTimeStartStatus(false);
        startActivity(new Intent(IntroWelcome.this,MainActivity.class));
        finish();
    }
    public void setStatusBarTransparent(){
        if(Build.VERSION.SDK_INT>=21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
