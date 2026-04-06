package io.hextree.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class hijackReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(
                "Hijacked", "Recieving Intent"
        );
        String flag = intent.getStringExtra("flag");
        Log.i(
                "Hijacked, FLag", flag);
        setResultCode(1);

    }
}