package com.mjict.signboardsurvey.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.WaitingDialog;


/**
 * Created by Junseo on 2016-10-11.
 */
public abstract class SABaseActivity extends AppCompatSActivity {

    private View toolbar;
    private DrawerLayout drawer;
    private AppBarLayout appBarLayout;
//    private NavigationView navigationView;
    private TextView titleTextView;
    private ImageView menuButton;
    private ImageView optionButton;
    private PopupMenu optionPopup;
    private Button addressSearchButton;
    private Button demolishedSignButton;
    private Button reviewSignButton;

    private WaitingDialog waitingDialog;
    private AlertDialog.Builder alertDialog;
//    private FloatingActionButton floatingButton;

    protected abstract int getContentLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private DrawerArrowDrawable drawerArrowDrawable;

    @Override
    protected void init() {
        setContentView(R.layout.activity_base);
        toolbar = this.findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // junseo
        appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        int resid = getContentLayout();
        View mainContentView = getLayoutInflater().inflate(resid, appBarLayout);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerArrowDrawable.setProgress(slideOffset);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerArrowDrawable.setProgress(1.0f);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                drawerArrowDrawable.setProgress(0f);
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

//        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        menuButton = (ImageView)this.findViewById(R.id.menu_button);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        menuButton.setImageDrawable(drawerArrowDrawable);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    return;

                if(drawer.isDrawerOpen(GravityCompat.START) == false)
                    drawer.openDrawer(GravityCompat.START);
            }
        });

        titleTextView = (TextView)this.findViewById(R.id.title_text_view);
        optionButton = (ImageView)this.findViewById(R.id.option_button);
        optionPopup = new PopupMenu(SABaseActivity.this, optionButton, Gravity.NO_GRAVITY, android.support.v7.appcompat.R.attr.popupMenuStyle, 0);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionPopup.show();
            }
        });
        optionButton.setVisibility(View.INVISIBLE);

        waitingDialog = new WaitingDialog(this);
        waitingDialog.create();

        addressSearchButton = (Button)this.findViewById(R.id.address_search_button);
        demolishedSignButton = (Button)this.findViewById(R.id.demolished_sign_button);
        reviewSignButton = (Button)this.findViewById(R.id.review_sign_button);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // junseo
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        // junseo
//        int id = item.getItemId();
//
//        if (id == R.id.nav_camera) {
//            // Handle the camera action
//        } else if (id == R.id.nav_gallery) {
//
//        } else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        waitingDialog.dismiss();
        
        super.onPause();
    }

    public void inflateOptionMenu(int resId) {
        optionPopup.inflate(resId);
    }

    public void setOnOptionMenuItemClickListener(PopupMenu.OnMenuItemClickListener listener) {
        optionPopup.setOnMenuItemClickListener(listener);
    }

    public void setAddressSearchButtonOnClickListener(View.OnClickListener listener) {
        addressSearchButton.setOnClickListener(listener);
    }

    public void setDemolishedSignButtonOnClickListener(View.OnClickListener listener) {
        demolishedSignButton.setOnClickListener(listener);
    }

    public void setReviewSignButtonOnClickListener(View.OnClickListener listener) {
        reviewSignButton.setOnClickListener(listener);
    }

    public void showOptionButton() {
        optionButton.setVisibility(View.VISIBLE);
    }

    public void showWaitingDialog(int resId) {
        waitingDialog.setStatusText(resId);
        waitingDialog.show();
    }

    public void hideToolBar() {
        toolbar.setVisibility(View.GONE);
    }
    public void showToolBar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    public void hideWaitingDialog() {
        waitingDialog.hide();
    }

    public void setTitle(String title) {
//        toolbar.setTitle(title);
//        getSupportActionBar().setTitle(title);
//        toolbar.setTitleTextColor(Color.BLACK);
        titleTextView.setText(title);
    }

    public void setTitle(int resId) {
        String title = getString(resId);
        setTitle(title);
    }

    public void disableNavigation() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    /**
     * 메세지와 함께 알림 다이얼로그를 보여준다.
     *
     * @param message 알림 메세지
     */
    public void showAlertDialog(String message) {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.create();
        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setMessage(message);
        alertDialog.show();
    }

    /**
     * 메세지와 함께 알림 다이얼로그를 보여준다.
     *
     * @param resid 알림 메세지의 리소스 id
     */
    public void showAlertDialog(int resid) {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.create();
        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setMessage(resid);
        alertDialog.show();
    }

    public void showAlertDialog(int resid, final DialogInterface.OnClickListener listener) {
        alertDialog = new AlertDialog.Builder(this);
        alertDialog.create();
        alertDialog.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                if (listener != null)
                    listener.onClick(dialog, which);
            }
        });
        alertDialog.setMessage(resid);
        alertDialog.show();
    }

}
