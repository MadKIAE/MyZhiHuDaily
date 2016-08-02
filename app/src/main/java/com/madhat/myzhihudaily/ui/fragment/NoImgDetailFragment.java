package com.madhat.myzhihudaily.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.FragmentDetailNoimgBinding;
import com.madhat.myzhihudaily.model.entity.DetailItem;
import com.madhat.myzhihudaily.model.entity.ExtraItem;
import com.madhat.myzhihudaily.ui.MainActivity;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;

/**
 * Created by Avast on 2016/7/30.
 */
public class NoImgDetailFragment extends Fragment {
    private  FragmentDetailNoimgBinding fragmentDetailNoimgBinding;
    DetailItem detailItem;
    String oldTitle;
    Fragment mFragment;
    private MenuItem commentItem;
    private ZhiHuAPI zhiHuAPI;

    private final static int GET_EXTRA_NEWS_FLAG=1;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==GET_EXTRA_NEWS_FLAG){
                ExtraItem extraItem = (ExtraItem)msg.obj;
                commentItem.setTitle(extraItem.getComments());
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        fragmentDetailNoimgBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail_noimg,container,false);
        fragmentDetailNoimgBinding.setDetailItem(detailItem);
        setHasOptionsMenu(true);
        WebSettings settings = fragmentDetailNoimgBinding.webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getExtraNews(handler,detailItem.getId(),GET_EXTRA_NEWS_FLAG);
        return  fragmentDetailNoimgBinding.getRoot();
    }
    public static NoImgDetailFragment newInstance(DetailItem item,String oldTitle,Fragment fragment) {
        NoImgDetailFragment f = new NoImgDetailFragment();
        f.detailItem=item;
        f.oldTitle=oldTitle;
        f.mFragment = fragment;
        return f;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        getActivity().getMenuInflater().inflate(R.menu.menu_extra, menu);
        commentItem = menu.findItem(R.id.comment);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onDestroy() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.resetToolbar();
        mainActivity.getSupportActionBar().setTitle(oldTitle);
        mainActivity.getSupportFragmentManager().beginTransaction().show(this.mFragment).commit();
        super.onDestroy();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setToolbar();
        mainActivity.getSupportActionBar().setTitle("我的知乎日报");
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.comment){
            // zhiHuAPI.getComments(handler,detailItem.getId(),true,GET_LONG_COMMENT_FLAG);
            getActivity().getSupportFragmentManager().beginTransaction().hide(NoImgDetailFragment.this)
                    .add(R.id.main_framelayout,CommentFragment.newInstance(detailItem.getId())).addToBackStack(null).commit();
        }
        return  true;
    }
}
