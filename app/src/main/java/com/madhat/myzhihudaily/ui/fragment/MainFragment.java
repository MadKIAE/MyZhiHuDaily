package com.madhat.myzhihudaily.ui.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.adapter.MyNewsAdapter;
import com.madhat.myzhihudaily.databinding.FragmentMainBinding;
import com.madhat.myzhihudaily.model.entity.DetailItem;
import com.madhat.myzhihudaily.model.entity.NewsInterface;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.model.entity.TopNews;
import com.madhat.myzhihudaily.ui.MainActivity;
import com.madhat.myzhihudaily.utils.FormatDate;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Avast on 2016/7/19.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ZhiHuAPI zhiHuAPI;
    private FragmentMainBinding fragmentMainBinding;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<NewsInterface> mDatas;
    private MyNewsAdapter mAdapter;
    private static final int Head_ITEM = 0;
    private static final int NORMAL_ITEM = 1;
    private static final int Date_ITEM = 2;
    private static final int FLAG_GET_TODAY_NEWS=1;
    private static final int FLAG_GET_BEFORE_NEWS=2;
    private static final int FLAG_GET_REFRESH=3;
    private static final int FLAG_GET_DETAIL_NEWS=4;
    private static final int FLAG_SET_VIEWPAGER_CURRENT=5;
    private int loadFlag;
    private boolean vpFlag=false;
    private Timer timer;
    private boolean currentViewPager=true;

    public Handler getHandler() {
        return handler;
    }

    private  Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            //获取今日新闻
            if(msg.what==FLAG_GET_TODAY_NEWS){
                mDatas= ((ArrayList<NewsInterface>) msg.obj);
                mAdapter = new MyNewsAdapter(mDatas,getChildFragmentManager());
                fragmentMainBinding.newsRecyclerview.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(new MyNewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemShortClick(View view, int position) {
                      //  Toast.makeText(getActivity(), "Short" + mDatas.get(position), Toast.LENGTH_SHORT).show();
                        NewsItem ns = (NewsItem)mDatas.get(position);
                        ns.readOrNot.set(true);
                        zhiHuAPI.getDetailNews(handler,ns.getId(),FLAG_GET_DETAIL_NEWS);
                    }
                });
            }
            //加载更多
            if(msg.what==FLAG_GET_BEFORE_NEWS){
                int position = mDatas.size()-1;
                mDatas.addAll((ArrayList<NewsInterface>)msg.obj);
                fragmentMainBinding.newsRecyclerview.getAdapter().notifyDataSetChanged();
                fragmentMainBinding.newsRecyclerview.smoothScrollToPosition(position);
            }
            //刷新
            if(msg.what==FLAG_GET_REFRESH){
                ArrayList<NewsInterface> as = (ArrayList<NewsInterface>) msg.obj;
                mDatas.clear();
                for (int i = 0; i <as.size() ; i++) {
                    mDatas.add(i,as.get(i));
                }
                fragmentMainBinding.newsRecyclerview.getAdapter().notifyDataSetChanged();
                fragmentMainBinding.MainFragSwipe.setRefreshing(false);
                loadFlag=0;
            }
            if(msg.what==FLAG_GET_DETAIL_NEWS){
                DetailItem detailItem = (DetailItem) msg.obj;
                MainActivity mainActivity = (MainActivity)getActivity();
                mainActivity.getSupportFragmentManager().beginTransaction().hide(MainFragment.this).commit();
                getActivity().getSupportFragmentManager()
                             .beginTransaction()
                             .add(R.id.main_framelayout,
                                 DetailFragment.newInstance(detailItem,
                                                            mainActivity.getSupportActionBar().getTitle().toString(),
                                                            MainFragment.this))
                              .addToBackStack(null)
                              .commit();
                //getActivity().getSupportFragmentManager().beginTransaction().add(R.id.main_framelayout, new TestFragment()).addToBackStack(null).commit();
            }
            if(msg.what==FLAG_SET_VIEWPAGER_CURRENT){
                ViewPager viewPager = (ViewPager) fragmentMainBinding.newsRecyclerview.findViewById(R.id.head_viewPager);
                if(Head_ITEM!=mAdapter.getItemViewType(fragmentMainBinding.newsRecyclerview.getChildAdapterPosition(fragmentMainBinding.newsRecyclerview.getChildAt(0)))){
                    return;
                }
                if(!vpFlag){
                    setVpLs(viewPager);
                    viewPager.setCurrentItem(1);
                    vpFlag=true;
                }
                else {
                    if(viewPager.getCurrentItem()==viewPager.getAdapter().getCount()-1){
                        viewPager.setCurrentItem(0);
                    }
                    else {
                        viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                    }
                }
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        init();
        fragmentMainBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false);
        fragmentMainBinding.newsRecyclerview.setLayoutManager(linearLayoutManager);
        setRvLs();
        fragmentMainBinding.MainFragSwipe.setOnRefreshListener(this);
        fragmentMainBinding.MainFragSwipe.setColorSchemeResources(R.color.colorPrimary);
        return  fragmentMainBinding.getRoot();
    }

    public void init(){
        zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getTodayNews(handler,FLAG_GET_TODAY_NEWS);
        linearLayoutManager = new LinearLayoutManager(getContext());
        setTimer();
    }

    //设置RecyclerView的滑动监听事件
    public void  setRvLs(){
        fragmentMainBinding.newsRecyclerview.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    updateTitle(mAdapter.getItemViewType(recyclerView.getChildAdapterPosition(recyclerView.getChildAt(0))),
                            (Toolbar) getActivity().findViewById(R.id.toolbar),
                            recyclerView);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                int a = linearLayoutManager.findLastVisibleItemPosition();
                int b = mDatas.size()-1;
                //不知为何滑动快时会多次调用，因此先判断a是否与loadFlag相等，如果是，说明已调用过，直接返回
                if(loadFlag==a){
                    return;
                }
                if(a == b){
                    loadFlag=a;
                    NewsItem newsItem = (NewsItem)mDatas.get(mDatas.size()-1);
                    zhiHuAPI.getBeforeNews(handler,newsItem.getDate(),FLAG_GET_BEFORE_NEWS);
                }
            }
        });
    }

    //设置ViewPager的自动轮播事件
    public void setVpLs(ViewPager viewPager){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                timer.cancel();
                setTimer();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setTimer(){
        timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(FLAG_SET_VIEWPAGER_CURRENT);
            }
        },5000);}
    @Override
    public void onRefresh() {
        zhiHuAPI.getTodayNews(handler,FLAG_GET_REFRESH);
    }

    public void updateTitle(int flag,Toolbar toolbar,RecyclerView recyclerView){
        switch (flag){
            case Head_ITEM:
                toolbar.setTitle("首页");
                if(!currentViewPager){
                    setTimer();
                    currentViewPager=true;
                }
                break;
            case Date_ITEM:
                toolbar.setTitle(((TextView)recyclerView.getChildAt(0).findViewById(R.id.news_date_tv)).getText());
                currentViewPager=false;
                break;
            case NORMAL_ITEM:
                currentViewPager=false;
                int x = recyclerView.getChildLayoutPosition(recyclerView.getChildAt(0));
                NewsItem ni = (NewsItem)mDatas.get(1);
                NewsItem ns = (NewsItem) mDatas.get(x);
                String text = ni.getDate().equals(ns.getDate())?"今日新闻":FormatDate.formatDate(ns.getDate());
                toolbar.setTitle(text);
                break;
            default:
                break;
        }
    }

}
