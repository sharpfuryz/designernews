package eu.restio.designernews.models;

import android.text.format.DateUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment {
    public String body;
    public String created_at;
    public int depth;
    public String user_display_name;
    public int upvotes_count;

    public String getAuthor() {
        return getFirstWord(user_display_name);
    }

    public static String getFirstWord(String input){
        int i = input.indexOf(' ');
        String word = input.substring(0, i);
        return word;
    }

    public String getBody() {
        return body;
    }

    public String getDate() {
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
            Date date = formatter.parse(created_at);
            long now = System.currentTimeMillis();
            return (String) DateUtils.getRelativeTimeSpanString(date.getTime(), now, DateUtils.DAY_IN_MILLIS);
        } catch (ParseException e) {
            return null;
        }

    }
}
