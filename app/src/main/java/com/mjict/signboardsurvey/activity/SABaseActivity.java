package com.mjict.signboardsurvey.activity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.widget.CircularImageView;
import com.mjict.signboardsurvey.widget.WaitingDialog;


/**
 * Created by Junseo on 2016-10-11.
 */
public abstract class SABaseActivity extends AppCompatSActivity {

    private View toolbar;
    protected DrawerLayout drawer;
    private AppBarLayout appBarLayout;
//    private NavigationView navigationView;
    private CircularImageView userProfileImageView;
    private TextView userIdTextView;
    private TextView userNameTextView;
    private TextView titleTextView;
    private ImageView menuButton;
    private ImageView optionButton;
    private PopupMenu optionPopup;
    private Button userStatisticsButton;
    private Button addressSearchButton;
    private Button demolishedSignButton;
    private Button reviewSignButton;
    private Button mapSearchButton;
    private Button addAddressButton;
    private ImageButton homeButton;
    private ImageButton settingButton;
    private ImageButton quitButton;

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

        // 화면 캡쳐 방지
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
//                WindowManager.LayoutParams.FLAG_SECURE);

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

        userProfileImageView = (CircularImageView)this.findViewById(R.id.user_profile_image_view);
        userIdTextView = (TextView)this.findViewById(R.id.user_id_text_view);
        userNameTextView = (TextView)this.findViewById(R.id.user_name_text_view);

        menuButton = (ImageView)this.findViewById(R.id.menu_button);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        drawerArrowDrawable.setColor(Color.WHITE);
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
//        optionPopup = new PopupMenu(SABaseActivity.this, optionButton, Gravity.NO_GRAVITY, android.support.v7.appcompat.R.attr.popupMenuStyle, 0);
        optionPopup = new PopupMenu(SABaseActivity.this, optionButton);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionPopup.show();
            }
        });
        optionButton.setVisibility(View.INVISIBLE);

        waitingDialog = new WaitingDialog(this);
        waitingDialog.show();
        waitingDialog.hide();
//        waitingDialog.create();

        addressSearchButton = (Button)this.findViewById(R.id.address_search_button);
        demolishedSignButton = (Button)this.findViewById(R.id.demolished_sign_button);
        reviewSignButton = (Button)this.findViewById(R.id.review_sign_button);
        mapSearchButton = (Button)this.findViewById(R.id.map_search_button);
        userStatisticsButton = (Button)this.findViewById(R.id.user_statistics_button);
        homeButton = (ImageButton)this.findViewById(R.id.home_button);
        settingButton = (ImageButton)this.findViewById(R.id.setting_button);
        quitButton = (ImageButton)this.findViewById(R.id.quit_button);
        addAddressButton = (Button)this.findViewById(R.id.add_address_button);
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
        waitingDialog.hide();
        
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        waitingDialog.dismiss();

        super.onDestroy();
    }


    public void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    public void addDrawerListener(DrawerLayout.DrawerListener listener) {
        drawer.addDrawerListener(listener);
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

    public void setMapSearchButtonOnClickListener(View.OnClickListener listener) {
        mapSearchButton.setOnClickListener(listener);
    }

    public void setUserStatisticsButtonOnClickListener(View.OnClickListener listener) {
        userStatisticsButton.setOnClickListener(listener);
    }

    public void setHomeButtonOnClickListener(View.OnClickListener listener) {
        homeButton.setOnClickListener(listener);
    }

    public void setSettingButtonOnClickListener(View.OnClickListener listener) {
        settingButton.setOnClickListener(listener);
    }

    public void setQuitButtonOnClickListener(View.OnClickListener listener) {
        quitButton.setOnClickListener(listener);
    }

    public void setAddAddressButtonOnClickListener(View.OnClickListener listener) {
        addAddressButton.setOnClickListener(listener);
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
        menuButton.setVisibility(View.INVISIBLE);
    }

    public void setUserIdText(String userId) {
        userIdTextView.setText(userId);
    }

    public void setUserNameText(String userName) {
        userNameTextView.setText(userName);
    }

    public void setUserProfileImageViewOnClickListener(View.OnClickListener listener) {
        userProfileImageView.setOnClickListener(listener);
    }

    public void setUserProfileImage(Bitmap image) {
        userProfileImageView.setImageBitmap(image);
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
