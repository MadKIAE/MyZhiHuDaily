package com.madhat.myzhihudaily.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.madhat.myzhihudaily.BR;
import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.FragmentTopNewsBinding;
import com.madhat.myzhihudaily.databinding.NewsHeadPagerBinding;
import com.madhat.myzhihudaily.databinding.NewsItemBinding;
import com.madhat.myzhihudaily.databinding.NewsItemWithdateBinding;
import com.madhat.myzhihudaily.model.entity.NewsInterface;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.model.entity.TopNews;
import com.madhat.myzhihudaily.ui.MainActivity;
import com.madhat.myzhihudaily.ui.fragment.HeadItemFragment;
import com.madhat.myzhihudaily.ui.fragment.MainFragment;
import com.madhat.myzhihudaily.utils.FormatDate;
import com.madhat.myzhihudaily.utils.Px2Dp;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Avast on 2016/7/17.
 */
public class MyNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int Head_ITEM = 0;
    private static final int NORMAL_ITEM = 1;
    private static final int Date_ITEM = 2;
    private static final int FLAG_SET_VIEWPAGER_CURRENT=5;
    private ArrayList<NewsInterface> mData;
    private FragmentManager fm;
    public OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof NormalViewHolder) {
            NewsItem newsItem = (NewsItem) mData.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            normalViewHolder.getNewsItemBinding().setVariable(BR.NewsItem, newsItem);
            normalViewHolder.getNewsItemBinding().executePendingBindings();
            normalViewHolder.getNewsItemBinding().newsCard.setOnClickListener(normalViewHolder);
            if (holder instanceof DateViewHolder) {
                DateViewHolder dateViewHolder = (DateViewHolder) holder;
                if (position == 1) {
                    dateViewHolder.getNewsItemWithdateBinding().newsDateTv.setText("今日新闻");
                } else {
                    dateViewHolder.getNewsItemWithdateBinding().newsDateTv.setText(FormatDate.formatDate(newsItem.getDate()));
                }
                dateViewHolder.getNewsItemWithdateBinding().setVariable(BR.NewsItem, newsItem);
                dateViewHolder.getNewsItemWithdateBinding().executePendingBindings();
                dateViewHolder.getNewsItemWithdateBinding().newsItemInDate.newsCard.setOnClickListener(dateViewHolder);
            }
            return;
        }
        if (holder instanceof HeadViewHolder) {
            TopNews topNews = (TopNews) mData.get(position);
            HeadViewHolder headViewHolder = (HeadViewHolder) holder;
            final List<Fragment> viewList = new ArrayList<Fragment>();
            for (NewsItem ns : topNews.getTopNewsItems()) {
                HeadItemFragment headItemFragment = HeadItemFragment.newInstance(ns);
                viewList.add(headItemFragment);
            }
            if(headViewHolder.getNewsHeadPagerBinding().headViewPager.getAdapter()!=null){
                HeadPagerAdapter hp = (HeadPagerAdapter)headViewHolder.getNewsHeadPagerBinding().headViewPager.getAdapter();
                hp.setFragmentList(viewList);
            }
            else {
                headViewHolder.getNewsHeadPagerBinding().headViewPager.setAdapter(new HeadPagerAdapter(fm, viewList));
            }

            headViewHolder.getNewsHeadPagerBinding().executePendingBindings();
            return;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Head_ITEM) {
            NewsHeadPagerBinding newsHeadPagerBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.news_head_pager,
                    parent,
                    false);
            HeadViewHolder headViewHolder = new HeadViewHolder(newsHeadPagerBinding.getRoot());
            headViewHolder.setNewsHeadPagerBinding(newsHeadPagerBinding);
            return headViewHolder;

        } else if (viewType == NORMAL_ITEM) {
            NewsItemBinding newsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_item, parent, false);
            NormalViewHolder normalViewHolder = new NormalViewHolder(newsItemBinding.getRoot());
            normalViewHolder.setNewsItemBinding(newsItemBinding);
            return normalViewHolder;
        } else if (viewType == Date_ITEM) {
            NewsItemBinding newsItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_item, parent, false);
            NewsItemWithdateBinding newsItemWithdateBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_item_withdate, parent, false);
            DateViewHolder dateViewHolder = new DateViewHolder(newsItemWithdateBinding.getRoot());
            dateViewHolder.setNewsItemBinding(newsItemBinding);
            dateViewHolder.setNewsItemWithdateBinding(newsItemWithdateBinding);
            return dateViewHolder;
        }
        return null;
    }

    //构造函数
    public MyNewsAdapter(ArrayList<NewsInterface> data, FragmentManager fm) {
        this.mData = data;
        this.fm = fm;
    }


    //点击事件接口
    public interface OnItemClickListener {
        void onItemShortClick(View view, int position);
    }

    //普通ViewHolder
    public class NormalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NewsItemBinding getNewsItemBinding() {
            return newsItemBinding;
        }

        public void setNewsItemBinding(NewsItemBinding newsItemBinding) {
            this.newsItemBinding = newsItemBinding;
        }

        private NewsItemBinding newsItemBinding;

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemShortClick(v, getLayoutPosition());
            }
        }

        public NormalViewHolder(View itemView) {
            super(itemView);
        }
    }

    //带日期的ViewHolder
    public class DateViewHolder extends NormalViewHolder {

        public NewsItemWithdateBinding getNewsItemWithdateBinding() {
            return newsItemWithdateBinding;
        }

        public void setNewsItemWithdateBinding(NewsItemWithdateBinding newsItemWithdateBinding) {
            this.newsItemWithdateBinding = newsItemWithdateBinding;
        }

        private NewsItemWithdateBinding newsItemWithdateBinding;

        public DateViewHolder(View itemView) {
            super(itemView);
        }
    }

    //轮播图ViewHolder
    public class HeadViewHolder extends RecyclerView.ViewHolder {

        public NewsHeadPagerBinding getNewsHeadPagerBinding() {
            return newsHeadPagerBinding;
        }

        public void setNewsHeadPagerBinding(NewsHeadPagerBinding newsHeadPagerBinding) {
            this.newsHeadPagerBinding = newsHeadPagerBinding;
        }

        private NewsHeadPagerBinding newsHeadPagerBinding;

        public HeadViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return Head_ITEM;
            case 1:
                return Date_ITEM;
            default:
                NewsItem newsItem = (NewsItem) mData.get(position);
                NewsItem lastNewsItem = (NewsItem) mData.get(position - 1);
                if (newsItem.getDate().equals(lastNewsItem.getDate())) {
                    return NORMAL_ITEM;
                } else {
                    return Date_ITEM;
                }
        }
    }
}

