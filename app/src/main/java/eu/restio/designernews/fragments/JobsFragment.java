package eu.restio.designernews.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import eu.restio.designernews.adapters.JobsListViewAdapter;
import eu.restio.designernews.MainActivity;
import eu.restio.designernews.R;
import eu.restio.designernews.models.Job;
import eu.restio.designernews.network.API;

public class JobsFragment extends android.support.v4.app.ListFragment {
    public ArrayList<Job> jobs_list;
    private JobsListViewAdapter adapter;

    public static JobsFragment newInstance() {
        JobsFragment fragment = new JobsFragment();
        return fragment;
    }
    public JobsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_jobs, container, false);
        adapter = new JobsListViewAdapter(getActivity(), jobs_list);
        setListAdapter(adapter);
        new JobsFetcher().execute();
        return rootView;
    }
    class JobsFetcher extends AsyncTask<Void, Void, String> {

        public int items_group;
        public boolean force;

        public JobsFetcher(){
            super();
        }


        @Override
        protected String doInBackground(Void... params) {
            API a = API.getInstance();
            return a.prefetch_jobs();
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                MainActivity a = (MainActivity) getActivity();
                a.raise_io_error();
            }
            try {
                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Job>>() {}.getType();

                jobs_list = gson.fromJson(result, listType);
                adapter.addAll(jobs_list);
                adapter.notifyDataSetChanged();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

}
