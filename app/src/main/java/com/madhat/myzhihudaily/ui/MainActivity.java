package com.madhat.myzhihudaily.ui;

import android.os.Handler;
import android.os.Message;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.model.entity.ThemeItem;
import com.madhat.myzhihudaily.ui.fragment.MainFragment;
import com.madhat.myzhihudaily.ui.fragment.ThemeFragment;
import com.madhat.myzhihudaily.utils.ZhiHuAPI;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    private Toolbar toolbar;
    private ActionBarDrawerToggle mActionBarDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private long time=0;
    private AppBarLayout.LayoutParams params;
    private NavigationView navigationView;
    private final static int FLAG_GET_THEMELIST=1;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==FLAG_GET_THEMELIST){
                onNavgationViewMenuItemSelected(((ArrayList<ThemeItem>)msg.obj));
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActionBarDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close);
        mActionBarDrawerToggle.syncState();
        params = (AppBarLayout.LayoutParams)toolbar.getLayoutParams();
        navigationView=(NavigationView)findViewById(R.id.nav_view);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,new MainFragment()).commit();
        ZhiHuAPI zhiHuAPI = new ZhiHuAPI();
        zhiHuAPI.getThemeList(this.handler,FLAG_GET_THEMELIST);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if(getSupportFragmentManager().getBackStackEntryCount()>0){
                return  true;
            }
            if (mDrawerLayout.isDrawerOpen(Gravity.LEFT)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(Gravity.LEFT)){
            mDrawerLayout.closeDrawer(Gravity.LEFT);
            return;
        }
        if (System.currentTimeMillis() - time > 2000 && getSupportFragmentManager().getBackStackEntryCount() == 0) {
            time = System.currentTimeMillis();
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }

    public void resetToolbar(){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        params.setScrollFlags(0);
    }

    public void setToolbar(){
        mActionBarDrawerToggle.setDrawerIndicatorEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL|AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    onBackPressed();
                    if(getSupportFragmentManager().getBackStackEntryCount()==0){
                    resetToolbar();}
                }
                else {
                    onKeyDown(KeyEvent.KEYCODE_MENU,new KeyEvent(0,KeyEvent.KEYCODE_MENU));
                }
            }
        });
    }

    public void  onNavgationViewMenuItemSelected(ArrayList<ThemeItem> themeItems){
        NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView.getChildAt(0);
        navigationMenuView.setVerticalScrollBarEnabled(false);
        for(ThemeItem ti:themeItems){
            navigationView.getMenu().add(R.id.menu_group,Integer.parseInt(ti.getId()),0,ti.getName());
        }
        navigationView.getMenu().setGroupCheckable(R.id.menu_group,true,false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.homepage:
                        MainFragment mainFragment = new MainFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,mainFragment).commit();
                        break;
                    default:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_framelayout,ThemeFragment.newInstance(String.valueOf(item.getItemId()))).commit();
                        getSupportActionBar().setTitle(item.getTitle());
                        break;
                }

                // Menu item点击后选中，并关闭Drawerlayout
                item.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);
    }
}
