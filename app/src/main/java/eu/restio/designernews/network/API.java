package eu.restio.designernews.network;

import android.app.Activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/*
*   Run once - fetches all content, them took it as strings from singleton
* */
 public class API {
    private static API instance;
    public String stories_json;

    public String stories_top;
    public String stories_recent;
    public String stories_discuss;
    public String jobs;

    private API (){

    }

    public static API getInstance(){
        if (null == instance) instance = new API();
        return instance;
    }
    public String forced_prefetch(){
        stories_json = null;
        return prefetch();
    }
    public String prefetch(){
        if (stories_json == null) {
            String url = "https://dn-backend.herokuapp.com/items.json";
            try {
                HttpGet httpGet = new HttpGet(url);
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                String stories_json = EntityUtils.toString(entity, HTTP.UTF_8);
                JSONObject jObject = new JSONObject(stories_json);
                stories_top = jObject.getJSONArray("top_stories").toString();
                stories_recent = jObject.getJSONArray("recent_stories").toString();
                stories_discuss = jObject.getJSONArray("discussions").toString();
                jobs = jObject.getJSONArray("jobs").toString();
            } catch (IOException | JSONException e) {
                return null;
            }
            return null;
        }else{
            return stories_json;
        }
    }

    public String prefetch_top_stories() {
        if(stories_top == null) prefetch();
        return stories_top;
    }
    public String prefetch_recent_stories(){
        if(stories_recent == null) prefetch();
        return stories_recent;
    }
    public String prefetch_discuss(){
        if(stories_discuss == null) prefetch();
        return stories_discuss;
    }
    public String prefetch_jobs(){
        if(jobs == null) prefetch();
        return jobs;
    }
    public String fetch_comments(String url) {
        try{
            HttpGet httpGet = new HttpGet(url);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String story = EntityUtils.toString(entity, HTTP.UTF_8);
            JSONObject jObject = new JSONObject(story);

            return  jObject.getJSONObject("story").toString();
        } catch (IOException | JSONException e) {
            return null;
        }
    }

}
