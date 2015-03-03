package eu.restio.designernews.adapters;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import eu.restio.designernews.R;
import eu.restio.designernews.models.Comment;

public class CommentListAdapter extends ArrayAdapter<Comment>{

    private final Activity context;
    private List<Comment> comment;

    public CommentListAdapter(Activity context, List<Comment> comments_models) {
        super(context, R.layout.listview_stories);
        this.context = context;
        this.comment = comments_models;
    }

    public void add(Comment obj){
        this.comment.add(obj);
    }
    public void addAll(List<Comment> comments){
        this.comment = comments;
    }

    @Override
    public int getCount(){
        if(comment == null) {
            return 0;
        }else {
            return comment.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommentViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listview_comments, null, true);
            holder = new CommentViewHolder();
            holder.author = (TextView) rowView.findViewById(R.id.author);
            holder.body = (TextView) rowView.findViewById(R.id.body);
            holder.published = (TextView) rowView.findViewById(R.id.published);
            rowView.setTag(holder);
        } else {
            holder = (CommentViewHolder) rowView.getTag();
        }
        final Comment c = comment.get(position);
        holder.author.setText(c.getAuthor());
        holder.body.setText(Html.fromHtml(c.getBody()));
        holder.published.setText(c.getDate());
        if(position < (comment.size() - 1)) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.comments_separator, (ViewGroup) rowView, true);
        }
        return rowView;
    }

    private class CommentViewHolder {
        public TextView author;
        public TextView body;
        public TextView published;
    }
}
