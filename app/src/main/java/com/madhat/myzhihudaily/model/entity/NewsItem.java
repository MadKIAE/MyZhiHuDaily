package com.madhat.myzhihudaily.model.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.ObservableBoolean;
import android.widget.ImageView;

import com.google.gson.annotations.Expose;
import com.madhat.myzhihudaily.utils.DaysOfTwo;
import com.madhat.myzhihudaily.utils.Px2Dp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Avast on 2016/7/18.
 */
public class NewsItem implements NewsInterface {
    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //标题
    private String title;
    //日期
    private String date;

    //图片路径
    private ArrayList<String> images;

    //ID
    private String id;

    //
    private String type;

    //是否已读
    public ObservableBoolean readOrNot = new ObservableBoolean(false);

}
