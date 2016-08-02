package com.madhat.myzhihudaily.model.entity;

import android.databinding.Bindable;
import android.databinding.Observable;

import java.util.ArrayList;

/**
 * Created by Avast on 2016/7/18.
 */
public class TopNews implements NewsInterface{

    public ArrayList<NewsItem> getTopNewsItems() {
        return topNewsItems;
    }

    public void setTopNewsItems(ArrayList<NewsItem> topNewsItems) {
        this.topNewsItems = topNewsItems;
    }

    //顶部新闻
    private ArrayList<NewsItem> topNewsItems;

    public TopNews() {
       topNewsItems = new ArrayList<NewsItem>();
    }
}
