package com.madhat.myzhihudaily.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.FragmentTopNewsBinding;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;
import com.squareup.picasso.Picasso;

/**
 * Created by Avast on 2016/7/20.
 */
public class HeadItemFragment extends Fragment {
    FragmentTopNewsBinding fragmentTopNewsBinding;
    private static final int FLAG_GET_DETAIL_NEWS=4;
    NewsItem ns;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentTopNewsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_top_news,container,false);
        fragmentTopNewsBinding.setNewsItem(ns);
        fragmentTopNewsBinding.fragTopNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZhiHuAPI zhiHuAPI = new ZhiHuAPI();
                MainFragment mainFragment = (MainFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.main_framelayout);
                zhiHuAPI.getDetailNews(mainFragment.getHandler(),ns.getId(),FLAG_GET_DETAIL_NEWS);
            }
        });
        return fragmentTopNewsBinding.getRoot();
    }

    public static HeadItemFragment newInstance(NewsItem ns) {
        HeadItemFragment f = new HeadItemFragment();
        f.ns=ns;
        return f;
    }
}
