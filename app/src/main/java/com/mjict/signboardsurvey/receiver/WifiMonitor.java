package com.mjict.signboardsurvey.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2017-01-09.
 */
public class WiFiMonitor extends BroadcastReceiver {
    public final static int WIFI_STATE_DISABLED = 0x00;
    public final static int WIFI_STATE_DISABLING = WIFI_STATE_DISABLED + 1;
    public final static int WIFI_STATE_ENABLED = WIFI_STATE_DISABLING + 1;
    public final static int WIFI_STATE_ENABLING = WIFI_STATE_ENABLED + 1;
    public final static int WIFI_STATE_UNKNOWN = WIFI_STATE_ENABLING + 1;
    public final static int NETWORK_STATE_CONNECTED = WIFI_STATE_UNKNOWN + 1;
    public final static int NETWORK_STATE_CONNECTING = NETWORK_STATE_CONNECTED + 1;
    public final static int NETWORK_STATE_DISCONNECTED = NETWORK_STATE_CONNECTING + 1;
    public final static int NETWORK_STATE_DISCONNECTING = NETWORK_STATE_DISCONNECTED + 1;
    public final static int NETWORK_STATE_SUSPENDED = NETWORK_STATE_DISCONNECTING + 1;
    public final static int NETWORK_STATE_UNKNOWN = NETWORK_STATE_SUSPENDED + 1;

//        public interface OnChangeNetworkStatusListener {
//            public void OnChanged(int status);
//        }


    private WifiManager wifiManager = null;
    private ConnectivityManager connManager = null;
    private Context context;

    public WiFiMonitor() {

    }

    public WiFiMonitor(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

//        public void setOnChangeNetworkStatusListener(OnChangeNetworkStatusListener listener) {
//            onChangeNetworkStatusListener = listener;
//        }

    @Override
    public void onReceive(Context context, Intent intent) {
        String strAction = intent.getAction();
        if (strAction.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            switch (wifiManager.getWifiState()) {
                case WifiManager.WIFI_STATE_ENABLED:
                    disableWifi();
                    break;
                case WifiManager.WIFI_STATE_ENABLING:
                    disableWifi();
                    break;
            }
        }
    }

    private void disableWifi() {
        wifiManager.setWifiEnabled(false);
        Toast.makeText(context, R.string.wifi_is_prohibited, Toast.LENGTH_SHORT).show();
    }
}