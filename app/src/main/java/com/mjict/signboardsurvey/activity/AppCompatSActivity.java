package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;

import com.mjict.signboardsurvey.sframework.SActivityHandler;
import com.mjict.signboardsurvey.sframework.SIntent;


public abstract class AppCompatSActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private SActivityHandler handler = null;
//	private boolean handlerUsable = false;

    /**
     * ui 컨트롤의 인스턴스 초기화 작업(findViewbyId)을 여기서 수행. <br>
     * setContentView는 onActivityCreate 가 아니라 이 함수내에서 호출 되어야 한다.
     */
    abstract protected void init();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(intent == null) {
        }

        Class<?> handlerClass = null;
        Bundle bundle = intent.getExtras();
        if(bundle == null)
            return;

        if(intent instanceof SIntent)
            handlerClass = ((SIntent)intent).getHandlerClass();
        else
            handlerClass = (Class<?>) bundle.get(SActivityHandler.HANDLER_CLASS);

        // TODO 아래 에러 체크도 Exception 으로 묶으면 깔끔할듯.
        // handler 클래스가 설정되지 않거나 예외가 발생할 경우 일반 액티비티 처럼 작동.
        boolean status = false;
        if(handlerClass == null) {
        }

        if(handlerClass != SActivityHandler.class) {
        }

        if(handlerClass != null) {

            try {
                handler = (SActivityHandler) handlerClass.newInstance();    // TODO 타입 검사 추가 해야 함.
                handler.setActivity(this);
                status = true;
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } finally {
                // TODO
            }

            init();

            if(status == true)
                handler.onActivityCreate(savedInstanceState);

        }

//		handlerUsable = status;
    }

    @Override
    protected void onStart() {
        if(handler != null)
            handler.onActivityStart();

        super.onStart();
    }

    @Override
    protected void onPause() {
        if(handler != null)
            handler.onActivityPause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        if(handler != null)
            handler.onActivityResume();

        super.onResume();
    }

    @Override
    protected void onStop() {
        if(handler != null)
            handler.onActivityStop();

        super.onStop();
    }

    @Override
    protected void onRestart() {
        if(handler != null)
            handler.onActivityRestart();

        super.onRestart();
    }
    @Override
    protected void onDestroy() {
        if(handler != null)
            handler.onActivityDestroy();

        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(handler != null)
            handler.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(handler != null)
            handler.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(handler != null)
            handler.onConfigurationChanged(newConfig);

        super.onConfigurationChanged(newConfig);
    }

    boolean finishWithBackButton = true;
    public void setFinishWithBackButton(boolean answer) {
        finishWithBackButton = answer;
    }

    @Override
    public void onBackPressed() {
        if(handler != null)
            handler.onBackPressed();

        if(finishWithBackButton == true)
            super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        if(handler != null)
            handler.onSaveInstanceState(outState);

        super.onSaveInstanceState(outState, outPersistentState);
    }
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_base);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//
//        // junseo
//        ViewGroup appBarLayout = (ViewGroup)findViewById(R.id.app_bar_layout);
//        int resid = getContentLayout();
//        View mainContentView = getLayoutInflater().inflate(resid, appBarLayout);
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
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
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//    protected abstract int getContentLayout();
}
