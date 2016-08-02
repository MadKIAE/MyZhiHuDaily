package com.madhat.myzhihudaily.utils;

import android.util.Log;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Avast on 2016/7/23.
 */
public class FormatDate {
    public static String formatDate(String s){
        SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 EEEE");
        return dateFm.format(createDate(s)).toString();
    }

    public static String formatDate(Date date){
        SimpleDateFormat dateFm = new SimpleDateFormat("MM月dd日 EEEE");
        return dateFm.format(date).toString();
    }

    public static String formatCommentDate(String s){
        SimpleDateFormat dateFm = new SimpleDateFormat("MM-dd HH:mm");
        java.sql.Timestamp timestamp = new java.sql.Timestamp(Long.parseLong(s)*1000);
        return dateFm.format(timestamp).toString();
//        String date = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new java.util.Date(Long.parseLong(s)* 1000));
//        return date;
    }

    private static Date createDate(String s){
        try {
            SimpleDateFormat dm = new SimpleDateFormat("yyyyMMdd");
            return dm.parse(s);
        }
        catch (Exception e){

        }
        return null;
    }

}
