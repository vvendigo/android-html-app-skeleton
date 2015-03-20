package com.example.htmltest4;

import android.app.Activity;
import android.os.Bundle;

import android.util.Log;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.view.View;
// disable title
import android.view.Window;
// menu
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
// confirm
import android.app.AlertDialog;
import android.content.DialogInterface;
// wait for layout settle
import android.widget.LinearLayout;
import android.view.ViewTreeObserver;
// logging console.log
import android.webkit.WebChromeClient;

public class MainActivity extends Activity
{
    WebView wv;
    final static String FILE = "file:///android_asset/index.html";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        wv = (WebView)findViewById(R.id.webview);
        //wv.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        WebSettings ws = wv.getSettings();
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        ws.setCacheMode(WebSettings.LOAD_NO_CACHE);
        ws.setJavaScriptEnabled(true);
        ws.setDomStorageEnabled(true);
        ws.setDatabasePath("/data/data/" + wv.getContext().getPackageName() + "/databases/");

        wv.setWebChromeClient(new WebChromeClient());/* {
            public void onConsoleMessage(String message, int lineNumber, String sourceID) {
                Log.d("HTMLtest", message + " (" + sourceID + ":" + lineNumber + ")");
            }
        });*/

        // wait for layout settlement
        final LinearLayout layout = (LinearLayout) findViewById(R.id.main);
        Log.d("Log", "Layout: " + layout);
        final ViewTreeObserver observer= wv.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(
            new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    wv.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    int h = layout.getHeight();
                    int w = layout.getWidth();
                    Log.d("Log", "Size: " + w + 'x' + h);
                    wv.loadUrl(FILE);
                }
        });
    }

    // app states handling

    @Override
    protected void onStop() {
        Log.d("HTMLtest", "onStop");
        super.onStop();
        //savePrefs();
    }

    @Override
    protected void onResume() {
        Log.d("HTMLtest", "onResume");
        super.onResume();
        wv.onResume();
        wv.resumeTimers();
    }

    @Override
    protected void onPause() {
        Log.d("HTMLtest", "onPause");
        super.onPause();
        wv.onPause();
        wv.pauseTimers();
    }

    // finish to really finish
    @Override
    public void finish() {
        Log.d("HTMLtest", "finish");
        wv.loadUrl("about:blank");
        wv.freeMemory();
        super.finish();
        Log.d("HTMLtest", "finish end");
    }


    // restart

    public void confirmRestart() {
        new AlertDialog.Builder(this)
           .setMessage("Are you sure you want reload?")
           .setCancelable(false)
           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                    wv.loadUrl(FILE+"restart");
               }
           })
           .setNegativeButton("No", null)
           .show();
    }

    //////// menu /////////

    @Override
    public void onBackPressed() {
        this.openOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.menu_quit:
            finish();
            return true;
        case R.id.menu_restart:
            confirmRestart();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }

}
