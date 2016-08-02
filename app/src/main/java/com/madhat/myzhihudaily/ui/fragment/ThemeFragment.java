package com.madhat.myzhihudaily.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.adapter.ThemeNewsAdapter;
import com.madhat.myzhihudaily.databinding.FragmentMainBinding;
import com.madhat.myzhihudaily.model.entity.DetailItem;
import com.madhat.myzhihudaily.model.entity.NewsInterface;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.model.entity.ThemeItem;
import com.madhat.myzhihudaily.ui.MainActivity;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;

import java.util.ArrayList;

/**
 * Created by Avast on 2016/7/30.
 */
public class ThemeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private FragmentMainBinding themeBinding;
    private String themeId;
    private ZhiHuAPI zhiHuAPI;
    private ThemeNewsAdapter mAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NewsItem> mData;
    private final static int FLAG_GET_THEME_NEWS = 1;
    private static final int FLAG_GET_BEFORE_NEWS = 2;
    private static final int FLAG_GET_REFRESH = 3;
    private static final int FLAF_GET_DETAIL_NEWS = 4;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FLAG_GET_THEME_NEWS) {
                mData = (ArrayList<NewsItem>) msg.obj;
                mAdapter = new ThemeNewsAdapter(mData);
                themeBinding.newsRecyclerview.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new ThemeNewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemShortClick(View view, int position) {
                        NewsItem ns = (NewsItem) mData.get(position);
                        ns.readOrNot.set(true);
                        zhiHuAPI.getDetailNews(handler, ns.getId(), FLAF_GET_DETAIL_NEWS);
                    }
                });
            }
            if (msg.what == FLAF_GET_DETAIL_NEWS) {
                DetailItem detailItem = (DetailItem) msg.obj;
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.getSupportFragmentManager().beginTransaction().hide(ThemeFragment.this).commit();
                if (detailItem.getImage() == null) {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_framelayout,
                                    NoImgDetailFragment.newInstance(detailItem,
                                            mainActivity.getSupportActionBar().getTitle().toString(),
                                            ThemeFragment.this))
                            .addToBackStack(null)
                            .commit();
                } else {
                    getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .add(R.id.main_framelayout,
                                    DetailFragment.newInstance(detailItem,
                                            mainActivity.getSupportActionBar().getTitle().toString(),
                                            ThemeFragment.this))
                            .addToBackStack(null)
                            .commit();
                }

            }

            //刷新
            if (msg.what == FLAG_GET_REFRESH) {
                themeBinding.MainFragSwipe.setRefreshing(false);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        themeBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        init();
        themeBinding.newsRecyclerview.setLayoutManager(linearLayoutManager);
        themeBinding.MainFragSwipe.setOnRefreshListener(this);
        themeBinding.MainFragSwipe.setColorSchemeResources(R.color.colorPrimary);
        return themeBinding.getRoot();
    }

    public void init() {
        zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getThemeNews(handler, themeId, FLAG_GET_THEME_NEWS);
        linearLayoutManager = new LinearLayoutManager(getContext());
    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessage(FLAG_GET_REFRESH);
    }

    public static ThemeFragment newInstance(String themeId) {
        ThemeFragment f = new ThemeFragment();
        f.themeId = themeId;
        return f;
    }
}
