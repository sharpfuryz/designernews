package eu.restio.designernews.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import eu.restio.designernews.BrowserActivity;
import eu.restio.designernews.R;
import eu.restio.designernews.models.Job;

public class JobsListViewAdapter extends ArrayAdapter<Job> {
    public Activity context;
    public ArrayList<Job> jobs;

    public JobsListViewAdapter(Activity context, ArrayList<Job> jobs_list) {
        super(context, R.layout.listview_jobs);
        this.context = context;
        this.jobs = jobs_list;
    }
    public class JobViewHolder {
        public TextView title;
        public TextView description;
    }

    public void addAll(ArrayList<Job> g_jobs){
        this.jobs = g_jobs;
    }

    @Override
    public int getCount(){
        if(jobs == null) {
            return 0;
        }else {
            return jobs.size();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final JobViewHolder holder;
        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = context.getLayoutInflater();
            rowView = inflater.inflate(R.layout.listview_jobs, null, true);
            holder = new JobViewHolder();
            holder.title = (TextView) rowView.findViewById(R.id.title);
            holder.description = (TextView) rowView.findViewById(R.id.description);
            rowView.setTag(holder);
        } else {
            holder = (JobViewHolder) rowView.getTag();
        }
        final Job job = jobs.get(position);

        holder.title.setText(job.getTitle());
        holder.description.setText(job.getDescription());
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code for system browser
                //Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(job.getLink()));
                //context.startActivity(intent);
                Intent browserIntent = new Intent(context, BrowserActivity.class);
                browserIntent.putExtra("url", job.getLink());
                context.startActivity(browserIntent);
            }
        });
        return rowView;
    }
}
