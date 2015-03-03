package eu.restio.designernews.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import eu.restio.designernews.MainActivity;
import eu.restio.designernews.R;
import eu.restio.designernews.adapters.StoriesListViewAdapter;
import eu.restio.designernews.models.Story;
import eu.restio.designernews.network.API;

public class TopStoriesFragment extends android.support.v4.app.ListFragment {
    public ArrayList<Story> stories_list;
    private SwipeRefreshLayout swipeLayout;
    private StoriesListViewAdapter adapter;

    public static TopStoriesFragment newInstance() {
        TopStoriesFragment fragment = new TopStoriesFragment();
        return fragment;
    }

    public TopStoriesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_topstories, container, false);
        Bundle bundle = this.getArguments();
        int position = bundle.getInt("position", 0);

        adapter = new StoriesListViewAdapter(getActivity(), stories_list);
        setListAdapter(adapter);

        new TopStoriesFetcher(position).execute();
        init_swipe_to_refresh(rootView, position);
        return rootView;
    }

    private void init_swipe_to_refresh(View root, final int position) {
        try {
            swipeLayout = (SwipeRefreshLayout) root.findViewById(R.id.swipe_container);
            swipeLayout.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            new TopStoriesFetcher(position, true).execute();
                        }
                    });
            swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    class TopStoriesFetcher extends AsyncTask<Void, Void, String> {

        public int items_group;
        public boolean force;

        public TopStoriesFetcher(int position) {
            super();
            this.items_group = position;
        }

        // Constructor with forced state
        public TopStoriesFetcher(int position, boolean force) {
            super();
            this.items_group = position;
            this.force = force;
        }

        @Override
        protected String doInBackground(Void... params) {
            API a = API.getInstance();

            if (this.force) a.forced_prefetch();

            switch (items_group) {
                case 1:
                    return a.prefetch_top_stories();
                case 2:
                    return a.prefetch_recent_stories();
                case 3:
                    return a.prefetch_discuss();
                default:
                    return a.prefetch_top_stories();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                MainActivity a = (MainActivity) getActivity();
                a.raise_io_error();
            }
            try {
                swipeLayout.setRefreshing(false);

                Gson gson = new Gson();
                Type listType = new TypeToken<ArrayList<Story>>() {
                }.getType();

                stories_list = gson.fromJson(result, listType);
                adapter.addAll(stories_list);
                adapter.notifyDataSetChanged();

            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}