package com.swavlambibharat.myenglishvocab;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class ClipboardListernerBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        /*if(ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())){
            Toast.makeText(context, "ConnectivityChanged", Toast.LENGTH_SHORT).show();
        }*/
    }
}
