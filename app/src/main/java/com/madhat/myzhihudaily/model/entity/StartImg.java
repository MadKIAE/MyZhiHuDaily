package com.madhat.myzhihudaily.model.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.util.Log;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.madhat.myzhihudaily.BR;
import com.madhat.myzhihudaily.utils.Px2Dp;
import com.squareup.picasso.Picasso;

/**
 * Created by Avast on 2016/7/18.
 */
public class StartImg extends BaseObservable{
    @Bindable
    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
        notifyPropertyChanged(BR.img);
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        notifyPropertyChanged(BR.text);
    }

    @BindingAdapter({"img"})
    public static void loadImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    //图片路径
    private String img;

    //图片来源
    private String text;

    public StartImg(){}
}
