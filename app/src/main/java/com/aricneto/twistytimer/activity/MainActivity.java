package com.aricneto.twistytimer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.aricneto.twistytimer.AppRater;
import com.aricneto.twistify.R;
import com.aricneto.twistytimer.database.DatabaseHandler;
import com.aricneto.twistytimer.fragment.AlgListFragment;
import com.aricneto.twistytimer.fragment.SchemeSelectDialogMain;
import com.aricneto.twistytimer.fragment.ThemeSelectDialog;
import com.aricneto.twistytimer.fragment.TimerFragmentMain;
import com.aricneto.twistytimer.utils.ThemeUtils;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements BillingProcessor.IBillingHandler {

    private static final int DEBUG_ID = 11;
    BillingProcessor bp;

    private static final int TIMER_ID    = 1;
    private static final int THEME_ID    = 2;
    private static final int SCHEME_ID   = 9;
    private static final int OLL_ID      = 6;
    private static final int PLL_ID      = 7;
    private static final int DONATE_ID   = 8;
    private static final int EXPORT_ID   = 3;
    private static final int ABOUT_ID    = 4;
    private static final int SETTINGS_ID = 5;

    private static final int REQUEST_SETTING = 42;
    private static final int REQUEST_ABOUT   = 23;

    SmoothActionBarDrawerToggle mDrawerToggle;
    FragmentManager             fragmentManager;
    DrawerLayout                mDrawerLayout;

    private Drawer mDrawer;

    public void openDrawer() {
        mDrawer.openDrawer();
    }

    public void closeDrawer() {
        mDrawer.closeDrawer();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getCurrentTheme(getBaseContext()));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        AppRater.app_launched(this);

        bp = new BillingProcessor(this, null, this);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_activity_container, TimerFragmentMain.newInstance(), "fragment_main")
                    .commit();
        }

        handleDrawer(savedInstanceState);

    }

    private void handleDrawer(Bundle savedInstanceState) {
        final Activity activity = this;
        // Setup drawer
        mDrawer = new DrawerBuilder()
                .withActivity(activity)
                .withDelayOnDrawerClose(- 1)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_title_timer)
                                .withIcon(R.drawable.ic_timer_black_24dp).withIconTintingEnabled(true).withIdentifier(TIMER_ID),

                        new SectionDrawerItem().withName(R.string.drawer_title_reference),

                        new PrimaryDrawerItem().withName(R.string.drawer_title_oll)
                                .withIcon(R.drawable.ic_oll_black_24dp).withIconTintingEnabled(true).withIdentifier(OLL_ID),

                        new PrimaryDrawerItem().withName(R.string.drawer_title_pll)
                                .withIcon(R.drawable.ic_pll_black_24dp).withIconTintingEnabled(true).withIdentifier(PLL_ID),

                        new SectionDrawerItem().withName(R.string.drawer_title_other),

                        new PrimaryDrawerItem().withName(R.string.drawer_title_export)
                                .withIcon(R.drawable.ic_file_export_black_24dp).withIconTintingEnabled(true).withSelectable(false)
                                .withIdentifier(EXPORT_ID),

                        new PrimaryDrawerItem().withName(R.string.drawer_title_changeTheme)
                                .withIcon(R.drawable.ic_brush_black_24dp).withIconTintingEnabled(true).withSelectable(false)
                                .withIdentifier(THEME_ID),

                        new PrimaryDrawerItem().withName(R.string.drawer_title_changeColorScheme)
                                .withIcon(R.drawable.ic_scheme_black_24dp).withIconTintingEnabled(true).withSelectable(false)
                                .withIdentifier(SCHEME_ID),

                        new DividerDrawerItem(),

                        new PrimaryDrawerItem().withName(R.string.action_settings)
                                .withIcon(R.drawable.ic_action_settings_black_24).withIconTintingEnabled(true).withSelectable(false)
                                .withIdentifier(SETTINGS_ID),

                        new PrimaryDrawerItem().withName(R.string.action_donate)
                                .withIcon(R.drawable.ic_mood_black_24dp).withIconTintingEnabled(true).withSelectable(false)
                                .withIdentifier(DONATE_ID),

                        new PrimaryDrawerItem().withName(R.string.drawer_about)
                                .withIcon(R.drawable.ic_action_help_black_24).withIconTintingEnabled(true).withSelectable(false)
                                .withIdentifier(ABOUT_ID)


                        //,new PrimaryDrawerItem().withName("DEBUG OPTION - ADD 1000 SOLVES")
                        //        .withIcon(R.drawable.ic_action_help_black_24).withIconTintingEnabled(true).withSelectable(false)
                        //        .withIdentifier(DEBUG_ID)
//
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        switch (drawerItem.getIdentifier()) {
                            default:
                            case TIMER_ID:
                                mDrawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragmentManager
                                                .beginTransaction()
                                                .replace(R.id.main_activity_container, TimerFragmentMain.newInstance(), "fragment_main")
                                                .commit();

                                    }
                                });
                                break;

                            case OLL_ID:
                                mDrawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragmentManager
                                                .beginTransaction()
                                                .replace(R.id.main_activity_container, AlgListFragment.newInstance(DatabaseHandler.SUBSET_OLL), "fragment_main")
                                                .commit();

                                    }
                                });
                                break;

                            case PLL_ID:
                                mDrawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        fragmentManager
                                                .beginTransaction()
                                                .replace(R.id.main_activity_container, AlgListFragment.newInstance(DatabaseHandler.SUBSET_PLL), "fragment_main")
                                                .commit();

                                    }
                                });
                                break;

                            case EXPORT_ID:
                                if (isExternalStorageWritable()) {
                                    DatabaseHandler handler = new DatabaseHandler(getApplicationContext());
                                    handler.backupDatabaseCSV(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "SolvesTwistyTimer.csv");
                                    Toast.makeText(getApplicationContext(), getString(R.string.saved_to) + " " + Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/SolvesTwistyTimer.csv", Toast.LENGTH_LONG).show();
                                }
                                break;

                            case THEME_ID:
                                ThemeSelectDialog themeSelectDialog = ThemeSelectDialog.newInstance();
                                themeSelectDialog.show(fragmentManager, "theme_dialog");
                                break;

                            case SCHEME_ID:
                                SchemeSelectDialogMain schemeSelectDialogMain = SchemeSelectDialogMain.newInstance();
                                schemeSelectDialogMain.show(fragmentManager, "scheme_dialog");
                                break;

                            case SETTINGS_ID:
                                final Intent settingsIntent = new Intent(getApplicationContext(), SettingsActivity.class);
                                mDrawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(settingsIntent, REQUEST_SETTING);
                                    }
                                });
                                break;

                            case DONATE_ID:
                                if (BillingProcessor.isIabServiceAvailable(activity))
                                    new MaterialDialog.Builder(activity)
                                            .title(R.string.choose_donation_amount)
                                            .items(R.array.donation_tiers)
                                            .itemsCallback(new MaterialDialog.ListCallback() {
                                                @Override
                                                public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                                                    switch (which) {
                                                        case 0:
                                                            bp.purchase(activity, "donation_tier1");
                                                            break;
                                                        case 1:
                                                            bp.purchase(activity, "donation_tier2");
                                                            break;
                                                        case 2:
                                                            bp.purchase(activity, "donation_tier3");
                                                            break;
                                                        case 3:
                                                            bp.purchase(activity, "donation_tier4");
                                                            break;
                                                    }
                                                }
                                            })
                                            .show();

                                else
                                    Toast.makeText(activity, "Google Play not available", Toast.LENGTH_LONG).show();

                                break;

                            case ABOUT_ID:
                                final Intent aboutIntent = new Intent(getApplicationContext(), AboutActivity.class);
                                mDrawerToggle.runWhenIdle(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivityForResult(aboutIntent, REQUEST_ABOUT);
                                    }
                                });
                                break;


                            //case DEBUG_ID:
                            //    Random rand = new Random();
                            //    DatabaseHandler db = new DatabaseHandler(activity);
                            //    for (int i = 0; i < 1000; i++) {
                            //        db.addSolve(new Solve(rand.nextInt(40000), "333", "Normal", 165165l, "", 0, "", rand.nextBoolean()));
                            //    }
                            //    break;
                        }
                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        mDrawerLayout = mDrawer.getDrawerLayout();
        mDrawerToggle = new SmoothActionBarDrawerToggle(this, mDrawerLayout, null, R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

    }

    @Override
    public void onBillingInitialized() {
        /*
         * Called when BillingProcessor was initialized and it's ready to purchase
         */
    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        /*
         * Called when requested PRODUCT ID was successfully purchased
         */
        bp.consumePurchase(productId);
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
        /*
         * Called when some error occurred. See Constants class for more details
         */
    }

    @Override
    public void onPurchaseHistoryRestored() {
        /*
         * Called when purchase history was restored and the list of all owned PRODUCT ID's
         * was loaded from Google Play
         */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (! bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SETTING) {
            finish();
            startActivity(new Intent(this, MainActivity.class));
            //fragmentManager
            //        .beginTransaction()
            //        .replace(R.id.main_activity_container, TimerFragmentMain.newInstance(), "fragment_main")
            //        .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

/*
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;

            case R.id.debug_add:
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                db.addSolve(new Time(6057, PuzzleUtils.TYPE_333, "normal", 16578465465l, "R U D", 0));
                db.addSolve(new Time(6057, PuzzleUtils.TYPE_333, "bld", 16578465465l, "R U D", 0));
                db.addSolve(new Time(6057, PuzzleUtils.TYPE_333, "cross", 16578465465l, "R U D", 0));
                db.close();
                return true;

        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    // So the drawer doesn't lag when closing
    private class SmoothActionBarDrawerToggle extends ActionBarDrawerToggle {

        private Runnable runnable;

        public SmoothActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            super.onDrawerStateChanged(newState);
            if (runnable != null && newState == DrawerLayout.STATE_IDLE) {
                runnable.run();
                runnable = null;
            }
        }

        public void runWhenIdle(Runnable runnable) {
            this.runnable = runnable;
        }
    }

    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

}