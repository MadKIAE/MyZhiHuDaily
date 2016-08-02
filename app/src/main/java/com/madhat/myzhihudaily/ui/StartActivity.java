package com.madhat.myzhihudaily.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.ActivityStartBinding;
import com.madhat.myzhihudaily.model.entity.StartImg;
import com.madhat.myzhihudaily.utils.FormatDate;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;

/**
 * Created by Avast on 2016/7/18.
 */
public class StartActivity extends AppCompatActivity{

    ActivityStartBinding activityStartBinding;

    //Handler
    Handler  handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                activityStartBinding.setStartImg((StartImg)msg.obj);
            }
        }

    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityStartBinding = DataBindingUtil.setContentView(this,R.layout.activity_start);
        init();
        goToNext();
    }

    public void init(){
        ZhiHuAPI zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getStartImg(handler);
    }

    public void goToNext(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               startActivity(new Intent(StartActivity.this,MainActivity.class));
                finish();
            }
        },2000);
    }
}
