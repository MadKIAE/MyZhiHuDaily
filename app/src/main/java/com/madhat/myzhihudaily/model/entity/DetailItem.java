package com.madhat.myzhihudaily.model.entity;

import android.databinding.BindingAdapter;
import android.webkit.WebView;
import android.widget.ImageView;

import com.madhat.myzhihudaily.utils.Px2Dp;
import com.squareup.picasso.Picasso;

/**
 * Created by Avast on 2016/7/27.
 */
public class DetailItem {

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCss() {
        return css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getImageSource() {
        return imageSource;
    }

    public void setImageSource(String imageSource) {
        this.imageSource = imageSource;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getRecommenders() {
        return recommenders;
    }

    public void setRecommenders(String recommenders) {
        this.recommenders = recommenders;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getEditorName() {
        return editorName;
    }

    public void setEditorName(String editorName) {
        this.editorName = editorName;
    }

    public String getThemeId() {
        return themeId;
    }

    public void setThemeId(String themeId) {
        this.themeId = themeId;
    }

    private String body;

    private String css;

    private String imageSource;

    private String title;

    private String image;

    private String shareUrl;

    private  String recommenders;

    private String js;

    private String type;

    private  String id;

    private String themeName;

    private String editorName;

    private String themeId;

}
