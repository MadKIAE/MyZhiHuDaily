package com.madhat.myzhihudaily.model.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableBoolean;

import com.madhat.myzhihudaily.BR;

/**
 * Created by Avast on 2016/7/31.
 */
public class CommentItem extends BaseObservable{
    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getAvater() {
        return avater;
    }

    public void setAvater(String avater) {
        this.avater = avater;
    }

    public String getReply_content() {
        return reply_content;
    }

    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }

    public String getReply_status() {
        return reply_status;
    }

    public void setReply_status(String reply_status) {
        this.reply_status = reply_status;
    }

    public String getReply_id() {
        return reply_id;
    }

    public void setReply_id(String reply_id) {
        this.reply_id = reply_id;
    }

    public String getReply_author() {
        return reply_author;
    }

    public void setReply_author(String reply_author) {
        this.reply_author = reply_author;
    }

    private String author;
    private String id;
    private String content;
    private String likes;
    private String time;
    private String avater;
    private String reply_content=null;
    private String reply_status=null;
    private String reply_id=null;
    private String reply_author=null;

    @Bindable
    public Boolean getExpand() {
        return isExpand;
    }

    public void setExpand(Boolean expand) {
        isExpand = expand;
        notifyPropertyChanged(BR.expand);
    }

    private Boolean isExpand = false;

    public String makeReply(){
        if(reply_content==null){
            return null;
        }
        return "//"+reply_author+": "+reply_content;
    }
}
