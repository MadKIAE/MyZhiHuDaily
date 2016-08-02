package com.madhat.myzhihudaily.adapter;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.tool.util.L;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.StaticLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.CommentItemBinding;
import com.madhat.myzhihudaily.databinding.NewsItemBinding;
import com.madhat.myzhihudaily.databinding.NewsThemeImageBinding;
import com.madhat.myzhihudaily.model.entity.CommentItem;
import com.madhat.myzhihudaily.model.entity.NewsItem;

import java.util.ArrayList;

/**
 * Created by Avast on 2016/7/31.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList mData;
    private static final int LONG_ITEM = 0;
    private static final int SHORT_ITEM = 1;

    private static final int NORMAL_ITEM = 2;

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof DivideViewHolder){
            TextView textView = (TextView)holder.itemView.findViewById(R.id.comment_numTV);
            String s = (String)mData.get(position);
            if(position==0){
                textView.setText(s+"条长评");
            }
            else {
                textView.setText(s+"条短评");
            }
            return;
        }
        if(holder instanceof CommentViewHolder){
            final CommentItem commentItem=(CommentItem)mData.get(position);
            CommentViewHolder commentViewHolder = (CommentViewHolder)holder;
            commentViewHolder.getCommentItemBinding().setCommentItem(commentItem);
            commentViewHolder.getCommentItemBinding().executePendingBindings();
            if (commentItem.makeReply()!=null){
                final TextView textView = commentViewHolder.getCommentItemBinding().commentReplyTv;
                final TextView expandTV= commentViewHolder.getCommentItemBinding().commentExpand;
                final ViewTreeObserver viewTreeObserver = textView.getViewTreeObserver();
                viewTreeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        if(textView.getLineCount()>2){
                            commentItem.setExpand(true);
                            return true;
                        }
                        String a = textView.getText().toString();
                        String b = textView.getLayout().getText().toString();
                        Boolean isEllipsize=!(a.equals(b));
                        commentItem.setExpand(isEllipsize);
                        expandTV.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if(textView.getMaxLines()==2){
                                    expandTV.setText("收起");
                                    textView.setMaxLines(1000);
                                }
                                else {
                                    expandTV.setText("展开");
                                    textView.setMaxLines(2);
                                }
                            }
                        });
                        return true;
                    }
                });
            }
            commentViewHolder.getCommentItemBinding().executePendingBindings();
            return;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       if(viewType==NORMAL_ITEM){
           CommentItemBinding commentItemBinding = DataBindingUtil.inflate(
                   LayoutInflater.from(parent.getContext()),
                   R.layout.comment_item,
                   parent,
                   false);
           CommentViewHolder commentViewHolder = new CommentViewHolder(commentItemBinding.getRoot());
           commentViewHolder.setCommentItemBinding(commentItemBinding);
           return commentViewHolder;
       }
        else {
           DivideViewHolder divideViewHolder= new DivideViewHolder(LayoutInflater.from(
                   parent.getContext()).inflate(R.layout.comment_num, parent,
                   false));
           return divideViewHolder;
       }
    }

    @Override
    public int getItemViewType(int position) {
         int i = Integer.parseInt((String)mData.get(0))+1;
        switch (position){
            case 0:
                return LONG_ITEM;
            default:
                if(position==i){
                    return SHORT_ITEM;
                }
                else {
                    return NORMAL_ITEM;
                }
        }
    }

    public CommentAdapter(ArrayList mData) {
        this.mData = mData;
    }

    //评论栏
    public class DivideViewHolder extends RecyclerView.ViewHolder{
        public DivideViewHolder (View itemView) {
            super(itemView);
        }
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder{
        public CommentItemBinding getCommentItemBinding() {
            return commentItemBinding;
        }

        public void setCommentItemBinding(CommentItemBinding commentItemBinding) {
            this.commentItemBinding = commentItemBinding;
        }

        private CommentItemBinding commentItemBinding;

        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }


}
