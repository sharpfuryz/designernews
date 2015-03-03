package eu.restio.designernews.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import eu.restio.designernews.BrowserActivity;
import eu.restio.designernews.R;
import eu.restio.designernews.ShowActivity;
import eu.restio.designernews.models.Story;

/**
 * Created by sovremenius on 16.02.15.
 */
public class StoriesListViewAdapter extends ArrayAdapter<Story>{

    private final Activity context;
    private List<Story> stories;

    public StoriesListViewAdapter(Activity context, List<Story> stories_models) {
        super(context, R.layout.listview_stories);
        this.context = context;
        this.stories = stories_models;
    }

    public class ViewHolder {
        public ImageView badge;
        public TextView title;
        public TextView secondlane;
        public TextView comments;
    }

    public void addAll(List<Story> stories_models){
        this.stories = stories_models;
    }

    @Override
    public int getCount(){
        if(stories == null) {
            return 0;
        }else {
            return stories.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listview_stories, null, true);
            holder = new ViewHolder();
            holder.badge = (ImageView) rowView.findViewById(R.id.badge);
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.secondlane = (TextView) rowView.findViewById(R.id.second_lane);
            holder.comments = (TextView) rowView.findViewById(R.id.comments);
            rowView.setTag(holder);
        } else {
            holder = (ViewHolder) rowView.getTag();
        }
        final Story s = stories.get(position);

        if(Story.isVisited(context, s.getUID())){
            holder.title.setTextColor(Color.parseColor("#727272"));
        }
        setBadgeImage(holder.badge,s.getBadge());
        holder.title.setText(s.getTitleFormatter());
        holder.secondlane.setText(s.getSecondLaneFormatter());
        holder.comments.setText(s.getComments());
        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent commentsIntent = new Intent(context, ShowActivity.class);
                commentsIntent.putExtra("uid",s.getUID());
                commentsIntent.putExtra("title", s.getTitleFormatter());
                commentsIntent.putExtra("secondary",s.getSecondLaneFormatter());
                commentsIntent.putExtra("badge",s.getBadge());
                context.startActivity(commentsIntent);
            }
        });
        TouchAndClick(holder, rowView, s);
        return rowView;
    }

    private void TouchAndClick(final ViewHolder holder, final View rowView, final Story s) {
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for running in system browser
                    //                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s.getLink()));
                    //                Story.markAsSeen(context, s.getUID());
                    //                holder.title.setTextColor(Color.parseColor("#727272"));
                    //                context.startActivity(browserIntent);

                Story.markAsSeen(context, s.getUID());
                holder.title.setTextColor(Color.parseColor("#727272"));

                Intent browserIntent = new Intent(context, BrowserActivity.class);
                browserIntent.putExtra("url", s.getLink());
                context.startActivity(browserIntent);
            }
        });
        rowView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent commentsIntent = new Intent(context, ShowActivity.class);
                commentsIntent.putExtra("uid",s.getUID());
                commentsIntent.putExtra("title", s.getTitleFormatter());
                commentsIntent.putExtra("secondary",s.getSecondLaneFormatter());
                commentsIntent.putExtra("badge",s.getBadge());
                context.startActivity(commentsIntent);
                return false;
            }
        });
    }

    private void setBadgeImage(ImageView imageView, int badge) {
        switch (badge){
            case 1:
                imageView.setImageResource(R.drawable.ic_badge_discussion);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_badge_site_design);
                break;
            case 3:
                imageView.setImageResource(R.drawable.ic_badge_ask_dn);
                break;
            case 4:
                imageView.setImageResource(R.drawable.ic_badge_typography);
                break;
            case 5:
                imageView.setImageResource(R.drawable.ic_badge_apple);
                break;
            case 6:
                imageView.setImageResource(R.drawable.ic_badge_show_dn);
                break;
            case 7:
                imageView.setImageResource(R.drawable.ic_badge_ask_me_anything);
                break;
            case 8:
                imageView.setImageResource(R.drawable.ic_badge_talk);
                break;
            case 9:
                imageView.setImageResource(R.drawable.ic_badge_css);
                break;
        }
    }

}
