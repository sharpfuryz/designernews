package eu.restio.designernews;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import eu.restio.designernews.adapters.CommentListAdapter;
import eu.restio.designernews.models.Comment;

import java.util.ArrayList;

import eu.restio.designernews.network.API;


public class ShowActivity extends ActionBarActivity {
    private android.support.v7.widget.ShareActionProvider mShareActionProvider;

    private String s_title;
    private String s_secondary;
    private String s_public_url;
    private int s_badge;

    private TextView tv_title;
    private TextView tv_secondary;
    private TextView tv_body;
    private ImageView iv_badge;
    private ListView lvComments;

    public CommentListAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        setToolbarUI();
        setParamsAndUI();
        setListView();
    }

    private void setListView() {
        ArrayList<Comment> commentList = new ArrayList<Comment>();
        commentAdapter = new CommentListAdapter(this, commentList);

    }

    @Override
    public void onBackPressed(){
        finish();
    }

    private void setToolbarUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_arrow_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_show, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain").putExtra(Intent.EXTRA_TEXT, s_public_url);
        startActivity(shareIntent);
        return super.onOptionsItemSelected(item);
    }

    public void setParamsAndUI() {
        Bundle b = getIntent().getExtras();
        s_title = b.getString("title","");
        s_secondary = b.getString("secondary","");
        s_public_url = "https://news.layervault.com/stories/".concat(b.getString("uid", ""));
        s_badge = b.getInt("badge");

        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s_public_url));
                startActivity(browserIntent);
            }
        });
        tv_secondary = (TextView) findViewById(R.id.secondary);
        tv_body = (TextView) findViewById(R.id.commentBody);
        iv_badge = (ImageView) findViewById(R.id.badge);

        tv_title.setText(s_title);
        tv_secondary.setText(s_secondary);
        setBadge(s_badge);
        fetchComments();
    }

    private void fetchComments() {
       new CommentsFetcher(s_public_url).execute();
    }

    public void setBadge(int badge) {
        if (badge != 0) {
            iv_badge.setVisibility(View.VISIBLE);
        }else{
            iv_badge.setVisibility(View.GONE);
        }
        switch (badge){
            case 1:
                iv_badge.setImageResource(R.drawable.ic_badge_discussion);
                break;
            case 2:
                iv_badge.setImageResource(R.drawable.ic_badge_site_design);
                break;
            case 3:
                iv_badge.setImageResource(R.drawable.ic_badge_ask_dn);
                break;
            case 4:
                iv_badge.setImageResource(R.drawable.ic_badge_typography);
                break;
            case 5:
                iv_badge.setImageResource(R.drawable.ic_badge_apple);
                break;
            case 6:
                iv_badge.setImageResource(R.drawable.ic_badge_show_dn);
                break;
            case 7:
                iv_badge.setImageResource(R.drawable.ic_badge_ask_me_anything);
                break;
            case 8:
                iv_badge.setImageResource(R.drawable.ic_badge_talk);
                break;
            case 9:
                iv_badge.setImageResource(R.drawable.ic_badge_css);
                break;
            default:
                iv_badge.setVisibility(View.GONE);
                break;
        }
    }

    class CommentsFetcher extends AsyncTask<Void, Void, String> {
        public String url;
        public CommentsFetcher(String url){
            this.url = url;
        }
        @Override
        protected String doInBackground(Void... params) {
            API a = API.getInstance();
            return a.fetch_comments(url + "/?format=json");
        }
        @Override
        protected void onPostExecute(String result){
            if (result != null) {
                try {
                    JSONObject jo = new JSONObject(result);
                    String comment_body = jo.getString("comment");
                    if(comment_body != null) {
                        tv_body.setText(comment_body);
                        tv_body.setVisibility(View.VISIBLE);
                    }else{
                        tv_body.setVisibility(View.GONE);
                    }
                    String ja_s = jo.getJSONArray("comments").toString();
                    invokeDepthParser(ja_s);
                    redrawCommentsLayout();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private void invokeDepthParser(String result) throws JSONException {
            JSONArray comments = new JSONArray(result);
            for (int i = 0; i < comments.length(); i++) {
                JSONObject comment = comments.getJSONObject(i);
                Gson gson = new Gson();
                Comment comment_model = gson.fromJson(comment.toString(), Comment.class);
                commentAdapter.add(comment_model);

                JSONArray commentsArr = comment.getJSONArray("comments");
                if (commentsArr != null) {
                    getCommentsFromJsonArray(commentsArr);
                }
            }
        }

        private void getCommentsFromJsonArray(JSONArray commentsArr) throws JSONException {
            invokeDepthParser(commentsArr.toString());
        }
    }

    private void redrawCommentsLayout() {
        LinearLayout layout = (LinearLayout)findViewById(R.id.commentsLayout);
        final int adapterCount = commentAdapter.getCount();
        for (int i = 0; i < adapterCount; i++) {
            View item = commentAdapter.getView(i, null, null);
            layout.addView(item);
        }
    }
}
