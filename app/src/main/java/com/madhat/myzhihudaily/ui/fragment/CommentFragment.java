package com.madhat.myzhihudaily.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.adapter.CommentAdapter;
import com.madhat.myzhihudaily.databinding.FragmentMainBinding;
import com.madhat.myzhihudaily.model.entity.CommentItem;
import com.madhat.myzhihudaily.model.entity.DetailItem;
import com.madhat.myzhihudaily.model.entity.ExtraItem;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.ui.weight.DividerItemDecoration;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;

import java.util.ArrayList;

/**
 * Created by Avast on 2016/7/31.
 */
public class CommentFragment extends Fragment {
    private FragmentMainBinding commentBinding;
    private CommentAdapter mAdapter;
    private ZhiHuAPI zhiHuAPI;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList mData;
    private String id;
    private ArrayList<CommentItem> longCms;
    private ArrayList<CommentItem> shortCms;
    private final static int GET_LONG_COMMENT_FLAG=1;
    private final static int GET_SHORT_COMMENT_FLAG=2;
    private static final int NORMAL_ITEM = 2;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==GET_LONG_COMMENT_FLAG){
                mData = new ArrayList();
                ArrayList<CommentItem> ac = (ArrayList<CommentItem>)msg.obj;
                mData.add(String.valueOf(ac.size()));
                mData.addAll(ac);
                mAdapter = new CommentAdapter(mData);
                commentBinding.newsRecyclerview.setAdapter(mAdapter);
                zhiHuAPI.getComments(handler,id,false,GET_SHORT_COMMENT_FLAG);
            }
            if(msg.what==GET_SHORT_COMMENT_FLAG){
                ArrayList<CommentItem> ac = (ArrayList<CommentItem>)msg.obj;
                mData.add(String.valueOf(ac.size()));
                mData.addAll(ac);
                commentBinding.newsRecyclerview.getAdapter().notifyDataSetChanged();
            }
        }
    };



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        commentBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main,container,false);
        init();
        commentBinding.newsRecyclerview.setLayoutManager(linearLayoutManager);
        commentBinding.newsRecyclerview.addItemDecoration(new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL_LIST));
        return  commentBinding.getRoot();
    }

    public void init() {
        zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getComments(handler,id,true,GET_LONG_COMMENT_FLAG);
        linearLayoutManager = new LinearLayoutManager(getContext());
    }


    public static CommentFragment newInstance(String id) {
        CommentFragment f = new CommentFragment();
        f.id=id;
        return f;
    }



}
