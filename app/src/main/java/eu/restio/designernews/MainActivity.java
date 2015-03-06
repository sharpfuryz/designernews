package eu.restio.designernews;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import com.github.mrengineer13.snackbar.SnackBar;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import eu.restio.designernews.fragments.JobsFragment;
import eu.restio.designernews.fragments.TopStoriesFragment;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initDrawer(toolbar);
        setFrame(1);
    }

    private void initDrawer(Toolbar toolbar) {
        Drawer mDrawer = new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withHeaderDivider(false)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_top_stories).withIcon(getResources().getDrawable(R.drawable.ic_drawer_top_stories)).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_recent_stories).withIcon(getResources().getDrawable(R.drawable.ic_drawer_recent_stories)).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_discussions).withIcon(getResources().getDrawable(R.drawable.ic_drawer_discussion)).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_jobs).withIcon(getResources().getDrawable(R.drawable.ic_drawer_jobs)).withIdentifier(4)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        setFrame(position);
                    }
                });
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            mDrawer.withDrawerWidthDp(260);
        }
        mDrawer.build();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void setFrame(int position) {
        setActionbarTitle(position);
        if(position <= 3) {
            FragmentManager fragmentManager = getSupportFragmentManager();

            Bundle data = new Bundle();
            data.putInt("position", position);

            TopStoriesFragment ts_fragment = TopStoriesFragment.newInstance();
            ts_fragment.setArguments(data);

            fragmentManager.beginTransaction()
                    .replace(R.id.container, ts_fragment)
                    .commit();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            JobsFragment jobsFragment = JobsFragment.newInstance();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, jobsFragment)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        display_search_window();
        return super.onOptionsItemSelected(item);
    }

    public void setActionbarTitle(int Order){
        String lane;
        switch (Order) {
            case 1:
                lane = getResources().getString(R.string.drawer_item_top_stories);
                break;
            case 2:
                lane = getResources().getString(R.string.drawer_item_recent_stories);
                break;
            case 3:
                lane = getResources().getString(R.string.drawer_item_discussions);
                break;
            case 4:
                lane = getResources().getString(R.string.drawer_item_jobs);
                break;
            default:
                lane = "Designer News";
        }
        getSupportActionBar().setTitle(lane);
    }

    private void display_search_window() {
        //
    }
    public void raise_io_error(){
        new SnackBar.Builder(this)
                .withMessageId(R.string.network_error)
                .withStyle(SnackBar.Style.ALERT)
                .withDuration(SnackBar.LONG_SNACK)
                .show();
    }
}
