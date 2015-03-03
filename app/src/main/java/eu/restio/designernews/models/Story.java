package eu.restio.designernews.models;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by sovremenius on 16.02.15.
 */
public class Story {
    public String title;
    public String icon;
    public String uid;
    public String ago;
    public String points;
    public String comments;
    public String author;

    public String getTitle() {
        return title;
    }

    public int getBadge(){
        switch (icon){
            case "":
                return 0;
            case "Badge Discussion":
                return 1;
            case "Badge SiteDesign":
                return 2;
            case "Badge Ask":
                return 3;
            case "Badge Type":
                return 4;
            case "Badge Apple":
                return 5;
            case "Badge Show":
                return 6;
            case "Badge AMA":
                return 7;
            case "Badge Video":
                return 8;
            case "Badge CSS":
                return 9;
            default:
                return 0;
        }
    }
    public String getPoints() {
        return Story.getFirstWord(points);
    }

    public String getAgo() {
        return ago;
    }

    public String getLink() {
        return "https://news.layervault.com/click/stories/" + getUID();
    }
    public String getAuthor() {
        return Story.getFirstWord(author);
    }

    public String getComments() {
        if(Story.getFirstWord(comments).length() > 2){
            return "∞";
        }else{
            return Story.getFirstWord(comments);
        }
    }

    public static String getFirstWord(String input){
        int i = input.indexOf(' ');
        String word = input.substring(0, i);
        return word;
    }

    public String getTitleFormatter() {
        return getTitle().replaceAll("\\(.*?\\)","").trim();
    }

    public String getUID() {
        return uid;
    }

    public String getSecondLaneFormatter() {
        return getPoints()+ " points • " + getAgo() + " from "+getAuthor();
    }


    public static boolean isVisited(Activity context, String uid) {
        String visited_uids = loadUIDs(context);
        return visited_uids.contains(uid);
    }

    public static void markAsSeen(Activity context, String uid){
        saveUIDs(context,uid);
    }

    public static boolean saveUIDs(Activity context, String uid){
        SharedPreferences prefs = context.getSharedPreferences("general_store", 0);
        SharedPreferences.Editor editor = prefs.edit();
        String to_store = loadUIDs(context);
        editor.putString("visited_uids", to_store+","+uid);
        return editor.commit();
    }

    private static String loadUIDs(Activity context) {
        SharedPreferences prefs = context.getSharedPreferences("general_store", 0);
        return prefs.getString("visited_uids","");
    }
}
