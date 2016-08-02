package com.madhat.myzhihudaily.utils;

import android.databinding.BindingAdapter;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.madhat.myzhihudaily.R;
import com.madhat.myzhihudaily.model.entity.CommentItem;
import com.madhat.myzhihudaily.ui.weight.MyTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by Avast on 2016/7/28.
 */
public class BindTools {

    @BindingAdapter({"loadSmallImage"})
    public static void loadSmallImage(ImageView view, String url) {
        Picasso.with(view.getContext())
                .load(url)
                .resize(Px2Dp.dip2px(view.getContext(), 80), Px2Dp.dip2px(view.getContext(), 70))
                .into(view);
        //Picasso.with(view.getContext()).setDebugging(true);
    }

    @BindingAdapter({"loadTopImage"})
    public static void loadTopImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"loadDetailImage"})
    public static void loadDetailImage(ImageView view, String url) {
        Picasso.with(view.getContext()).load(url).into(view);
    }

    @BindingAdapter({"body","css"})
    public static void loadWeb(WebView webView, String body, String css) {
        String s="<html><head><style type=\"text/css\">"+css+"</style></head><body>"+body+"</body></html>";
        webView.loadData(s, "text/html; charset=UTF-8", null);
    }

    @BindingAdapter({"loadUserImage"})
    public static void loadUserImage(ImageView view, String url) {
        if(url.equals("http://pic3.zhimg.com/c513b3202_im.jpg")){
            Picasso.with(view.getContext())
                    .load(R.drawable.w)
                    .transform(new CircleTransform())
                    .into(view);
            return;
        }
        if(url.equals("http://pic4.zhimg.com/01d9e29ae2ff6c4f973a5a7c7b93a73b_im.jpg")){
            Picasso.with(view.getContext())
                    .load(R.drawable.m)
                    .transform(new CircleTransform())
                    .into(view);
            return;
        }
        Picasso.with(view.getContext())
                     .load(url)
                     .transform(new CircleTransform())
                     .into(view);
    }

    @BindingAdapter({"setCommentDate"})
    public static void setCommentDate(TextView view, String text) {
        view.setText(FormatDate.formatCommentDate(text));
    }

    @BindingAdapter({"setExpand"})
    public static void setExpand(ViewGroup viewGroup, CommentItem cm) {
         TextView expandTV = (TextView)viewGroup.findViewById(R.id.comment_expand);
         TextView textView = (TextView)viewGroup.findViewById(R.id.comment_reply_tv);

//        boolean isEllipsize = !((textView.getLayout().getText().toString()).equals(cm.makeReply()));
 //       Log.e("YYY",String.valueOf(isEllipsize));
//        expandTV.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(textView.getMaxLines()==2){
//                        expandTV.setText("收起");
//                        textView.setMaxLines(1000);
//                    }
//                    else{
//                        expandTV.setText("展开");
//                        textView.setMaxLines(2);
//                    }
//                }
//            });
    }

}
