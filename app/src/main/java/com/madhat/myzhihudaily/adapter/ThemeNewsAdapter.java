package com.madhat.myzhihudaily.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madhat.myzhihudaily.BR;
import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.NewsItemBinding;
import com.madhat.myzhihudaily.databinding.NewsItemNoimageBinding;
import com.madhat.myzhihudaily.databinding.NewsThemeImageBinding;
import com.madhat.myzhihudaily.model.entity.NewsInterface;
import com.madhat.myzhihudaily.model.entity.NewsItem;

import java.util.ArrayList;

/**
 * Created by Avast on 2016/7/30.
 */
public class ThemeNewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int Head_ITEM = 0;
    private static final int NORMAL_ITEM = 1;
    private static final int NO_IMAGE_ITEM = 2;
    private ArrayList<NewsItem> mData;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    //点击事件接口
    public interface OnItemClickListener{
        void onItemShortClick(View view, int position);
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof NormalViewHolder){
            NewsItem newsItem = (NewsItem) mData.get(position);
            NormalViewHolder normalViewHolder = (NormalViewHolder) holder;
            normalViewHolder.getNewsItemBinding().setVariable(BR.NewsItem, newsItem);
            normalViewHolder.getNewsItemBinding().executePendingBindings();
            normalViewHolder.getNewsItemBinding().newsCard.setOnClickListener(normalViewHolder);
            return;
        }
        if(holder instanceof NoImageViewHolder){
            NewsItem newsItem = (NewsItem) mData.get(position);
            NoImageViewHolder noImageViewHolder = (NoImageViewHolder)holder;
            ((NoImageViewHolder) holder).getNewsItemNoimageBinding().setNewsItem(newsItem);
            noImageViewHolder.getNewsItemNoimageBinding().executePendingBindings();
            noImageViewHolder.getNewsItemNoimageBinding().newsCard.setOnClickListener(noImageViewHolder);
            return;
        }
        if(holder instanceof ThemeImageViewHolder){
            NewsItem newsItem = (NewsItem) mData.get(position);
            ThemeImageViewHolder themeImageViewHolder = (ThemeImageViewHolder)holder;
            ((ThemeImageViewHolder) holder).getNewsThemeImageBinding().setNewsItem(newsItem);
            themeImageViewHolder.getNewsThemeImageBinding().executePendingBindings();
            return;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==Head_ITEM){
            NewsThemeImageBinding newsThemeImageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.news_theme_image,
                    parent,
                    false);
            ThemeImageViewHolder themeImageViewHolder = new ThemeImageViewHolder(newsThemeImageBinding.getRoot());
            themeImageViewHolder.setNewsThemeImageBinding(newsThemeImageBinding);
            return themeImageViewHolder;
        }
        if(viewType==NORMAL_ITEM){
            NewsItemBinding newsItemBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.news_item,
                    parent,
                    false);
            NormalViewHolder normalViewHolder = new NormalViewHolder(newsItemBinding.getRoot());
            normalViewHolder.setNewsItemBinding(newsItemBinding);
            return normalViewHolder;
        }
        if(viewType==NO_IMAGE_ITEM){
            NewsItemNoimageBinding newsItemNoimageBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()),
                    R.layout.news_item_noimage,
                    parent,
                    false);
            NoImageViewHolder noImageViewHolder = new NoImageViewHolder(newsItemNoimageBinding.getRoot());
            noImageViewHolder.setNewsItemNoimageBinding(newsItemNoimageBinding);
            return noImageViewHolder;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ThemeNewsAdapter(ArrayList<NewsItem> mData) {
        this.mData=mData;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return Head_ITEM;
            default:
                NewsItem newsItem = (NewsItem) mData.get(position);
                if(newsItem.getImages()==null){
                    return  NO_IMAGE_ITEM;
                }
                else {
                    return NORMAL_ITEM;
                }
        }
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

    //无图ViewHolder
    public class NoImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public NewsItemNoimageBinding getNewsItemNoimageBinding() {
            return newsItemNoimageBinding;
        }

        public void setNewsItemNoimageBinding(NewsItemNoimageBinding newsItemNoimageBinding) {
            this.newsItemNoimageBinding = newsItemNoimageBinding;
        }

        private NewsItemNoimageBinding newsItemNoimageBinding;

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemShortClick(v, getLayoutPosition());
            }
        }

        public NoImageViewHolder(View itemView) {
            super(itemView);
        }
    }

    //专栏图片ViewHolder
    public class ThemeImageViewHolder extends RecyclerView.ViewHolder{
        private NewsThemeImageBinding newsThemeImageBinding;

        public NewsThemeImageBinding getNewsThemeImageBinding() {
            return newsThemeImageBinding;
        }

        public void setNewsThemeImageBinding(NewsThemeImageBinding newsThemeImageBinding) {
            this.newsThemeImageBinding = newsThemeImageBinding;
        }

        public ThemeImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
