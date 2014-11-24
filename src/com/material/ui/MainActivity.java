package com.material.ui;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import com.material.R;
import com.material.ui.fragment.ShadowsFragment;

/**
 * Created by mojingtian on 14/11/19.
 */
public class MainActivity extends ActionBarActivity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mDrawerTitles;
    private int[] mDrawerIcons;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Toolbar mToolbar;
    private final int INVALID_POSITION = -1;
    private final int SHADOWS_POSITION = 0;
    private int mPostion = 0;
    private int mPostionTemp = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTitle = mDrawerTitle = getTitle();
        mDrawerTitles = getResources().getStringArray(R.array.drawer_arrays);
        mDrawerIcons = new int[]{R.drawable.drawer_icon, R.drawable.drawer_icon, R.drawable.drawer_icon, R.drawable.drawer_icon, R.drawable.drawer_icon};
        setupView();
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setNavigationIcon(R.drawable.ab_android);
        mToolbar.setLogo(R.drawable.ab_android);
        mToolbar.setOnMenuItemClickListener(new android.support.v7.widget.Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // Handle the menu item
                return true;
            }
        });

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            @Override
            public void onDrawerClosed(View view) {
                mPostion = mPostionTemp;
                mToolbar.setTitle(mTitle);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                mPostion = INVALID_POSITION;
                mToolbar.setTitle(mDrawerTitle);
                supportInvalidateOptionsMenu();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (savedInstanceState == null) {
            selectItem(0);
        }

    }

    private void setupView() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mDrawerList.setAdapter(new DrawerAdapter(this, mDrawerIcons, mDrawerTitles));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        mPostionTemp = position;
        Fragment fragment;
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragment = new ShadowsFragment();
                fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
                break;
            default:
                break;
        }
        mDrawerList.setItemChecked(position, true);
        setTitle(mDrawerTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        mToolbar.setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    class DrawerAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        private String[] texts;
        private int[] icons;
        private Context context;

        public DrawerAdapter(Context ctx, int[] icons, String[] texts) {
            super();
            this.texts = texts;
            this.icons = icons;
            this.context = ctx;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return texts.length;
        }

        @Override
        public Object getItem(int position) {
            return texts[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.drawer_item, parent, false);
            ImageView icon = (ImageView) convertView
                    .findViewById(R.id.icon);
            TextView text = (TextView) convertView
                    .findViewById(R.id.text);
            icon.setBackgroundResource(icons[position]);
            text.setText(texts[position]);
            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        switch (mPostion) {
            case SHADOWS_POSITION:
//                MenuItemCompat.setShowAsAction(menu.add(0, 1, 0, getString(R.string.shadows))
//                                .setIcon(R.drawable.drawer_icon),
//                        MenuItemCompat.SHOW_AS_ACTION_IF_ROOM
//                                | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);
                break;
            default:
                break;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

}
