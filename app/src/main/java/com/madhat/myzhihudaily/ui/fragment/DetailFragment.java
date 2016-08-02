package com.madhat.myzhihudaily.ui.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.databinding.FragmentDetailBinding;
import com.madhat.myzhihudaily.model.entity.CommentItem;
import com.madhat.myzhihudaily.model.entity.DetailItem;
import com.madhat.myzhihudaily.model.entity.ExtraItem;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.ui.MainActivity;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Field;
import java.util.ArrayList;

import retrofit2.http.Url;

/**
 * Created by Avast on 2016/7/27.
 */
public class DetailFragment extends Fragment {
    private FragmentDetailBinding fragmentDetailBinding;
    private DetailItem detailItem;
    private String oldTitle;
    private  Fragment mFragment;
    private MenuItem commentItem;
    private ZhiHuAPI zhiHuAPI;

    private final static int GET_EXTRA_NEWS_FLAG=1;
    private final static int GET_LONG_COMMENT_FLAG=2;
    private final static int GET_SHORT_COMMENT_FLAG=3;

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
        fragmentDetailBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false);
        fragmentDetailBinding.setDetailItem(detailItem);
        setHasOptionsMenu(true);
        WebSettings settings = fragmentDetailBinding.webview.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDefaultTextEncodingName("utf-8");
        //Picasso.with(fragmentDetailBinding.detailImg.getContext()).load(detailItem.getImage()).into(fragmentDetailBinding.detailImg);

        zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getExtraNews(handler,detailItem.getId(),GET_EXTRA_NEWS_FLAG);

        return  fragmentDetailBinding.getRoot();


//        View rootView = inflater.inflate(R.layout.fragment_detail,container,false);
//        ImageView imageView = (ImageView) rootView.findViewById(R.id.iconimg);
//        Picasso.with(getContext()).load(detailItem.getImage()).into(imageView);
//        return  rootView;


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //return super.onOptionsItemSelected(item);
        if(item.getItemId()==R.id.comment){
           // zhiHuAPI.getComments(handler,detailItem.getId(),true,GET_LONG_COMMENT_FLAG);
            getActivity().getSupportFragmentManager().beginTransaction().hide(DetailFragment.this)
                         .add(R.id.main_framelayout,CommentFragment.newInstance(detailItem.getId())).addToBackStack(null).commit();
        }
        return  true;
    }

    public static DetailFragment newInstance(DetailItem item, String oldTitle, Fragment fragment) {
        DetailFragment f = new DetailFragment();
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
        //setIconVisible(menu,true);
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

//    public void setIconVisible(Menu menu, boolean visable){
//        Field field;
//        try {
//            field = menu.getClass().getDeclaredField("mOptionalIconsVisible");
//
//            Log.d("TAG"," setIconVisible1() field="+field);
//            field.setAccessible(true);
//            field.set(menu, visable);
//        } catch (NoSuchFieldException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IllegalArgumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
}
