package com.madhat.myzhihudaily.utils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.madhat.myzhihudaily.model.entity.CommentItem;
import com.madhat.myzhihudaily.model.entity.DetailItem;
import com.madhat.myzhihudaily.model.entity.ExtraItem;
import com.madhat.myzhihudaily.model.entity.NewsInterface;
import com.madhat.myzhihudaily.model.entity.NewsItem;
import com.madhat.myzhihudaily.model.entity.StartImg;
import com.madhat.myzhihudaily.model.entity.ThemeItem;
import com.madhat.myzhihudaily.model.entity.TopNews;

import org.w3c.dom.Comment;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Avast on 2016/7/18.
 */
public class ZhiHuAPI {
    private Retrofit retrofit;
    private Retrofit mRetrofit;
    //基本URL
    public static final String BASE_URL = "http://news-at.zhihu.com/";

    public ZhiHuAPI(){
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mRetrofit = new Retrofit.Builder()
                     .baseUrl(BASE_URL)
                     .build();

    }

    //获取图片接口
    public interface StratImgApi {
        @GET("api/4/start-image/1080*1776")
        Call<StartImg> getStartImg();
    }


    //获取启动界面图片
    public void getStartImg(final Handler handler){
        StratImgApi stratImgApi = retrofit.create(StratImgApi.class);
        Call<StartImg> call= stratImgApi.getStartImg();
        call.enqueue(new Callback<StartImg>() {
            @Override
            public void onResponse(Call<StartImg> call, Response<StartImg> response) {
                StartImg startImg = response.body();
                Message message = new Message();
                message.what=1;
                message.obj=startImg;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<StartImg> call, Throwable t) {

            }
        });
    }

    //获取今日新闻
    public interface TodayNewsApi{
        @GET("api/4/news/latest")
        Call<ResponseBody> getNewsItem();
    }

    //获取今日新闻
    public void getTodayNews(final Handler handler, final int flag){
        TodayNewsApi todayNewsApi = mRetrofit.create(TodayNewsApi.class);
        Call<ResponseBody> call = todayNewsApi.getNewsItem();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    String jsonStr = response.body().string();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
                    String date = jsonObject.get("date").getAsString();
                    JsonArray stories = jsonObject.getAsJsonArray("stories");
                    JsonArray topStories = jsonObject.getAsJsonArray("top_stories");
                    //返回数据集
                    ArrayList<NewsInterface> ns = new ArrayList<NewsInterface>();
                    TopNews topNews = new TopNews();
                    for (int i = 0; i < topStories.size(); i++) {
                        NewsItem newsItem = new NewsItem();
                        JsonObject jo = topStories.get(i).getAsJsonObject();
                        newsItem.setDate(date);
                        newsItem.setId(jo.get("id").getAsString());
                        newsItem.setTitle(jo.get("title").getAsString());
                        newsItem.setType(jo.get("type").getAsString());
                        ArrayList<String> as = new ArrayList<String>();
                        as.add(jo.get("image").getAsString());
                        newsItem.setImages(as);
                        topNews.getTopNewsItems().add(newsItem);
                    }
                    ns.add(topNews);
                    for (int i = 0; i < stories.size(); i++) {
                        NewsItem newsItem = new NewsItem();
                        JsonObject jo = stories.get(i).getAsJsonObject();
                        newsItem.setDate(date);
                        newsItem.setId(jo.get("id").getAsString());
                        newsItem.setTitle(jo.get("title").getAsString());
                        newsItem.setType(jo.get("type").getAsString());
                        if(jo.get("images")==null){
                            newsItem.setImages(null);
                        }
                        else {
                            ArrayList<String> as=gson.fromJson(jo.getAsJsonArray("images"),new TypeToken<ArrayList<String>>(){}.getType());
                            newsItem.setImages(as);
                        }
                        ns.add(newsItem);
                    }
                    Message message = new Message();
                    message.what=flag;
                    message.obj=ns;
                    handler.sendMessage(message);
                }
               catch (Exception e){
                   Log.e(this.getClass().toString(),e.getMessage());
               }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //获取过往消息
    public interface BeforeNewsApi{
        @GET("api/4/news/before/{date}")
        Call<ResponseBody> getBeforeNews(@Path("date") String date);
    }

    public void getBeforeNews(final Handler handler, String date, final int flag){
        BeforeNewsApi beforeNewsApi = mRetrofit.create(BeforeNewsApi.class);
        Call<ResponseBody>call  = beforeNewsApi.getBeforeNews(date);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Gson gson = new Gson();
                    String jsonStr = response.body().string();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
                    String date = jsonObject.get("date").getAsString();
                    JsonArray stories = jsonObject.getAsJsonArray("stories");
                    ArrayList<NewsInterface> ns = new ArrayList<NewsInterface>();
                    for (int i = 0; i < stories.size(); i++) {
                        NewsItem newsItem = new NewsItem();
                        JsonObject jo = stories.get(i).getAsJsonObject();
                        newsItem.setDate(date);
                        newsItem.setId(jo.get("id").getAsString());
                        newsItem.setTitle(jo.get("title").getAsString());
                        newsItem.setType(jo.get("type").getAsString());
                        if(jo.get("images")==null){
                            newsItem.setImages(null);
                        }
                        else {
                            ArrayList<String> as=gson.fromJson(jo.getAsJsonArray("images"),new TypeToken<ArrayList<String>>(){}.getType());
                            newsItem.setImages(as);
                        }
                        ns.add(newsItem);
                    }
                    Message message = new Message();
                    message.what=flag;
                    message.obj=ns;
                    handler.sendMessage(message);
                }
                catch (Exception e){
                    Log.e(this.getClass().toString(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //获取具体新闻
    public interface DetailNewsApi{
        @GET("api/4/news/{id}")
        Call<ResponseBody> getDetailNews(@Path("id") String id);
    }

    public void getDetailNews(final Handler handler, String id, final int flag){
        DetailNewsApi detailNewsApi = mRetrofit.create(DetailNewsApi.class);
        Call<ResponseBody> call = detailNewsApi.getDetailNews(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    DetailItem detailItem = new DetailItem();
                    Gson gson = new Gson();
                    String jsonStr = response.body().string();
                    JsonParser jsonParser = new JsonParser();
                    JsonObject jsonObject = jsonParser.parse(jsonStr).getAsJsonObject();
                    String type = jsonObject.get("type").getAsString();
                    detailItem.setBody(jsonObject.get("body").getAsString());
                    detailItem.setType(type);
                    detailItem.setTitle(jsonObject.get("title").getAsString());
                    detailItem.setId(jsonObject.get("id").getAsString());
                    detailItem.setShareUrl(jsonObject.get("share_url").getAsString());
                    if(jsonObject.has("image")){
                        detailItem.setImageSource(jsonObject.get("image_source").getAsString());
                        detailItem.setImage(jsonObject.get("image").getAsString());
                    }
                    else {
                        detailItem.setImageSource(null);
                        detailItem.setImage(null);
                    }
                    String cssUrl = jsonObject.get("css").getAsString().substring(jsonObject.get("css").getAsString().lastIndexOf("="));
                    ArrayList<Object> as = new ArrayList<Object>();
                    as.add(detailItem);
                    as.add(handler);
                    Message message = new Message();
                    Bundle bundle = new Bundle();
                    message.what = 1;
                    message.obj = as;
                    message.arg1 = flag;
                    bundle.putString("cssKey", cssUrl);
                    message.setData(bundle);
                    mhandler.sendMessage(message);
                }
                catch (Exception e){
                    Log.e(this.getClass().toString(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //
    private  interface CssApi{
        @GET("css/news_qa.auto.css")
        Call<ResponseBody> getCss(@Query("v") String cssUrl);
    }

    private String  getCssUrl(final String cssUrl, final ArrayList<Object> as, final int flag){
        CssApi cssApi = mRetrofit.create(CssApi.class);
        Call<ResponseBody> call = cssApi.getCss(cssUrl);
        call.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               try {
                   Message message = new Message();
                   message.obj=as;
                   message.what=2;
                   Bundle bundle = new Bundle();
                   bundle.putString("css",response.body().string());
                   message.setData(bundle);
                   message.arg1=flag;
                   mhandler.sendMessage(message);
               }
               catch (Exception e){
               }

           }

           @Override
           public void onFailure(Call<ResponseBody> call, Throwable t) {
           }
       });
        return  null;
    }

    private interface ThemeListApi{
        @GET("api/4/themes")
        Call<ResponseBody> getThemeList();
    }

    public void getThemeList(final Handler handler,final int FLAG){
        ThemeListApi themeListApi = mRetrofit.create(ThemeListApi.class);
        Call<ResponseBody> call = themeListApi.getThemeList();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ArrayList<ThemeItem> themeItems = new ArrayList<ThemeItem>();
                    Gson gson = new Gson();
                    String jsonStr = response.body().string();
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = jsonParser.parse(jsonStr).getAsJsonObject().get("others").getAsJsonArray();
                    for (int i=0;i<jsonArray.size();i++){
                        ThemeItem themeItem = new ThemeItem();
                        themeItem.setId(jsonArray.get(i).getAsJsonObject().get("id").getAsString());
                        themeItem.setDescription(jsonArray.get(i).getAsJsonObject().get("description").getAsString());
                        themeItem.setName(jsonArray.get(i).getAsJsonObject().get("name").getAsString());
                        themeItem.setThumbnail(jsonArray.get(i).getAsJsonObject().get("thumbnail").getAsString());
                        themeItems.add(themeItem);
                    }
                    Message message =new Message();
                    message.obj=themeItems;
                    message.what=FLAG;
                    handler.sendMessage(message);
                }
                catch (Exception e){
                    Log.e(this.getClass().toString(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //获取某个列表的新闻
    private interface ThemeNewsApi{
        @GET("api/4/theme/{themeId}")
        Call<ResponseBody> getThemeList(@Path("themeId") String themeId);
    }

    public void getThemeNews(final Handler handler,String themeId,final int flag){
        ThemeNewsApi themeNewsApi = mRetrofit.create(ThemeNewsApi.class);
        Call<ResponseBody> call = themeNewsApi.getThemeList(themeId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ArrayList<NewsItem> ns= new ArrayList<NewsItem>();
                    Gson gson = new Gson();
                    String jsonStr = response.body().string();
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = jsonParser.parse(jsonStr).getAsJsonObject().get("stories").getAsJsonArray();
                    {
                        NewsItem newsItem = new NewsItem();
                        newsItem.setTitle(jsonParser.parse(jsonStr).getAsJsonObject().get("description").getAsString());
                        ArrayList<String> img = new ArrayList<String>();
                        //img.add(jsonParser.parse(jsonStr).getAsJsonObject().get("image").getAsString());
                        img.add(jsonParser.parse(jsonStr).getAsJsonObject().get("background").getAsString());
                        newsItem.setImages(img);
                        ns.add(newsItem);
                    }
                    for(int i=0;i<jsonArray.size();i++){
                        NewsItem newsItem = new NewsItem();
                        newsItem.setId(jsonArray.get(i).getAsJsonObject().get("id").getAsString());
                        newsItem.setType(jsonArray.get(i).getAsJsonObject().get("type").getAsString());
                        newsItem.setTitle(jsonArray.get(i).getAsJsonObject().get("title").getAsString());
                        ArrayList<String> img = new ArrayList<String>();
                        if(jsonArray.get(i).getAsJsonObject().get("images")==null){
                            newsItem.setImages(null);
                        }
                        else {
                            ArrayList<String> as=gson.fromJson(jsonArray.get(i).getAsJsonObject().getAsJsonArray("images"),new TypeToken<ArrayList<String>>(){}.getType());
                            newsItem.setImages(as);
                        }
                        ns.add(newsItem);
                    }
                    Message message =new Message();
                    message.obj=ns;
                    message.what=flag;
                    handler.sendMessage(message);
                }
                catch (Exception e){
                    Log.e(this.getClass().toString(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    //获取某个新闻的额外信息（评论数、点赞数）
    private interface  ExtraNewsApi{
        @GET("api/4/story-extra/{id}")
        Call<ExtraItem> getExtraNews(@Path("id") String id);
    }

    public void getExtraNews(final Handler handler,final String id,final int flag){
        ExtraNewsApi extraNewsApi = retrofit.create(ExtraNewsApi.class);
        Call<ExtraItem> call= extraNewsApi.getExtraNews(id);
        call.enqueue(new Callback<ExtraItem>() {
            @Override
            public void onResponse(Call<ExtraItem> call, Response<ExtraItem> response) {
                ExtraItem extraItem = response.body();
                Message message = new Message();
                message.what=flag;
                message.obj=extraItem;
                handler.sendMessage(message);
            }

            @Override
            public void onFailure(Call<ExtraItem> call, Throwable t) {

            }
        });
    }

    //获取评论
    private interface  CommentsApi{
        //获取长评论
        @GET("api/4/story/{id}/long-comments")
        Call<ResponseBody> getLongComments(@Path("id") String id);
        //获取短评论
        @GET("api/4/story/{id}/short-comments")
        Call<ResponseBody> getShortComments(@Path("id") String id);
    }

    public void getComments(final Handler handler,String id,Boolean isLong,final int flag){
        CommentsApi commentsApi = mRetrofit.create(CommentsApi.class);
        Call<ResponseBody> call=isLong?commentsApi.getLongComments(id):commentsApi.getShortComments(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    ArrayList<CommentItem> commentItems = new ArrayList<CommentItem>();
                    Gson gson = new Gson();
                    String jsonStr = response.body().string();
                    JsonParser jsonParser = new JsonParser();
                    JsonArray jsonArray = jsonParser.parse(jsonStr).getAsJsonObject().get("comments").getAsJsonArray();
                    for (int i=0;i<jsonArray.size();i++){
                        CommentItem commentItem =  new CommentItem();
                        JsonObject jo = jsonArray.get(i).getAsJsonObject();
                        commentItem.setId(jo.get("id").getAsString());
                        commentItem.setAuthor(jo.get("author").getAsString());
                        commentItem.setAvater(jo.get("avatar").getAsString());
                        commentItem.setContent(jo.get("content").getAsString());
                        commentItem.setTime(jo.get("time").getAsString());
                        commentItem.setLikes(jo.get("likes").getAsString());
                        if(jo.get("reply_to")!=null){
                            JsonObject jr = jo.get("reply_to").getAsJsonObject();
                            commentItem.setReply_author(jr.get("author").getAsString());
                            commentItem.setReply_content(jr.get("content").getAsString());
                            commentItem.setReply_id(jr.get("id").getAsString());
                            commentItem.setReply_status(jr.get("status").getAsString());
                        }

                        commentItems.add(commentItem);
                    }
                    Message message =new Message();
                    message.obj=commentItems;
                    message.what=flag;
                    handler.sendMessage(message);
                }
                catch (Exception e){
                    Log.e(this.getClass().toString(),e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }




    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                getCssUrl((String) msg.getData().get("cssKey"), (ArrayList<Object>) msg.obj,msg.arg1);
            }
            if (msg.what == 2) {
                ArrayList<Object> aob = (ArrayList<Object>) msg.obj;
                DetailItem detailItem = (DetailItem) aob.get(0);
                Handler handler = (Handler) aob.get(1);
                String css = (String) msg.getData().get("css");
                //原CSS中有预留的空白头部
                String holder=".headline .img-place-holder {\n" +
                        "  height: 200px;\n" +
                        "}";
                //删除CSS字符串中的空白头部
                detailItem.setCss(css.replace(holder,""));
                Message message = new Message();
                message.what = msg.arg1;
                message.obj = detailItem;
                handler.sendMessage(message);
            }
        }
    };
}
