package moun.com.deli;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import moun.com.deli.database.UserDAO;
import moun.com.deli.fragment.MainFragment;
import moun.com.deli.fragment.MenuBurgersFragment;
import moun.com.deli.fragment.MenuDrinksFragment;
import moun.com.deli.fragment.MenuPizzaFragment;
import moun.com.deli.fragment.MenuSaladsFragment;
import moun.com.deli.fragment.MenuSandwichFragment;
import moun.com.deli.fragment.MenuSweetsFragment;
import moun.com.deli.fragment.MyCartFragment;
import moun.com.deli.util.AppUtils;
import moun.com.deli.util.SessionManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
    MainFragment.OnItemSelectedListener{

    private Toolbar mToolbar;
    private TextView mTitle;
    private DrawerLayout mDrawerLayout;
    private NavigationView mDrawer;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean mUserSawDrawer = false;
    private static final String FIRST_TIME = "first_time";
    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private int mSelectedId;
    private SessionManager session;
    private UserDAO userDAO;
    private boolean isTwoPane = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        mTitle = (TextView) mToolbar.findViewById(R.id.toolbar_title);
        mTitle.setText(getString(R.string.app_name));
        mTitle.setTypeface(AppUtils.getTypeface(this, AppUtils.FONT_BOLD));

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (NavigationView) findViewById(R.id.navigation_view);
        mDrawer.setNavigationItemSelectedListener(this);

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        navigate(mSelectedId);

        FrameLayout fragmentItemDetail = (FrameLayout) findViewById(R.id.content_detail_fragment);
        if (fragmentItemDetail != null) {
            isTwoPane = true;
            MenuSandwichFragment menuSandwichFragment = new MenuSandwichFragment();
            switchContent(menuSandwichFragment);
        }
        if (savedInstanceState == null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            MainFragment mainFragment = new MainFragment();
            transaction.replace(R.id.content_fragment, mainFragment);
            transaction.commit();
        }

        // Session manager
        session = new SessionManager(getApplicationContext());

        userDAO = new UserDAO(this);


    }

    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    private void navigate(int mSelectedId) {
        Intent intent = null;
        if (mSelectedId == R.id.breakfast) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, MenuActivityWithTabs.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }, 200);

        } else if (mSelectedId == R.id.cart) {
            hideDrawer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }, 200);

        } else if (mSelectedId == R.id.favorites) {
            hideDrawer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, ProfileActivityWithTabs.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }, 200);

        } else if (mSelectedId == R.id.hot_deals) {
            hideDrawer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, HotDealsActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }, 200);

        } else if (mSelectedId == R.id.location) {
            hideDrawer();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_in, R.anim.left_out);
                }
            }, 200);



        }


    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        //    menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        hideDrawer();
        navigate(mSelectedId);
        return true;
    }

    // Before the activity is destroyed, onSaveInstanceState() gets called.
    // The onSaveInstanceState() method saves the selected item from drawer.
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem logintItem = menu.findItem(R.id.action_login);
        MenuItem logoutItem = menu.findItem(R.id.action_logout);
        // Check if user is already logged in or not
        if (!session.isLoggedIn()) {
            // User is already logged in, hide Login button from the menu and show up the Logout button.
            logoutItem.setVisible(false);
        } else {
            logintItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent intent = null;

        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.our_menu:
                intent = new Intent(this, MenuActivityWithTabs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.hot_deals:
                intent = new Intent(this, HotDealsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.location:
                intent = new Intent(this, LocationActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.favorites:
                intent = new Intent(this, ProfileActivityWithTabs.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.my_cart:
                intent = new Intent(this, MyCartActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.action_login:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                // Closing all the Activities
                intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // Add new Flag to start new Activity
                intentLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
                startActivity(intentLogin);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                return true;
            case R.id.action_logout:
                LogoutUser();

                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Logging out the user. Will set isLoggedIn flag to false in shared
     * preferences, Clears the user data from SQLite user table.
     */
    public void LogoutUser() {
        session.setLogin(false);
        userDAO.deleteUser();
        Intent intentLogout = new Intent(this, MainActivity.class);
        // Closing all the Activities
        intentLogout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Add new Flag to start new Activity
        intentLogout.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // re-create the Main Activity
        startActivity(intentLogout);
        finish();

    }


    @Override
    public void onItemSelected(int position) {
        if (isTwoPane) {
            if(position == 1){
                MenuSandwichFragment menuSandwichFragment = new MenuSandwichFragment();
                switchContent(menuSandwichFragment);
            } else if (position == 2){
                MenuBurgersFragment menuBurgersFragment = new MenuBurgersFragment();
                switchContent(menuBurgersFragment);
            } else if (position == 3){
                MenuPizzaFragment menuPizzaFragment = new MenuPizzaFragment();
                switchContent(menuPizzaFragment);
            } else if(position == 4){
                MenuSaladsFragment menuSaladsFragment = new MenuSaladsFragment();
                switchContent(menuSaladsFragment);
            } else if (position == 5){
                MenuSweetsFragment menuSweetsFragment = new MenuSweetsFragment();
                switchContent(menuSweetsFragment);
            } else {
                MenuDrinksFragment menuDrinksFragment = new MenuDrinksFragment();
                switchContent(menuDrinksFragment);
            }

        } else {
            Intent intent = new Intent(MainActivity.this, MenuActivityWithTabs.class);
            intent.putExtra("currentItem", position);
            startActivity(intent);
        }

    }

    public void switchContent(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
                            android.R.anim.fade_in, android.R.anim.fade_out);
            transaction.replace(R.id.content_detail_fragment, fragment);
            transaction.commit();
        }
    }
}
