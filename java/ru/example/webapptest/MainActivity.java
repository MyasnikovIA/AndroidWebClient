package ru.example.webapptest;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.os.PowerManager;
import android.view.Menu;
import android.webkit.WebView;

public class MainActivity extends Activity {
    public PowerManager.WakeLock wl= null;
    SensorAndroid sens;
    public WebView webView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new LockOrientation(this).lock();
        // Чтобы снять запрет на смену ориентации делаем так:
        // context.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        webView = new WebView(this);
        setContentView(webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new Android(this,webView), "Android");

           sens=new SensorAndroid(this,webView);
           webView.addJavascriptInterface(sens, "Sensor");

           webView.loadUrl("file:///android_asset/index.htm");
           webView.loadUrl("file:///android_asset/examples/index.html");
           //  webView.loadUrl("https://www.cam-recorder.com/");


        PowerManager pm = (PowerManager)getSystemService( Context.POWER_SERVICE);
        wl = pm.newWakeLock( PowerManager.SCREEN_DIM_WAKE_LOCK | PowerManager.ON_AFTER_RELEASE,"UMSE PowerTest");
        if (wl != null) {
            wl.acquire();
        }


    }
/*
    @Override
    protected void onDestroy() {
        if (wl != null) {
            wl.release();
            wl = null;
        }
    }
*/
    @Override
    protected void onStop() {
        sens.StopSensor();
        super.onStop();
    }

}
