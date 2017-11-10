package ru.example.webapptest;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by SMWrap on 01.11.17.
 *
 *   https://www.tutorialspoint.com/android/android_sensors.htm
 *
 *  <pre> <div id="info" ></div></pre>
 *  <pre> <div id="info2"></div></pre>
 *  <script>
 *     document.getElementById("info2").innerHTML="---"+AccelInfo;
 *     var timerId = setInterval(function() {
 *        document.getElementById("info").innerHTML=Accel;
 *        document.getElementById("info").innerHTML+="\n"+Light;
 *        document.getElementById("info").innerHTML+="\n"+Prox;
 *     }, 1);
 *  </script>
 *
 *   SensorAndroid sens=new SensorAndroid(this,webView);
 *   webView.addJavascriptInterface(sens, "Sensor");
 */
public class SensorAndroid {

    private long lastUpdate;
    private WebView webView;
    List<Sensor> list;
    //       webView.loadUrl("javascript: Accel="+jsonObject.toString()   );
    private MainActivity parentActivity;
    public  SensorAndroid(MainActivity activity, WebView webViewPar)  {
         webView=webViewPar;
         parentActivity = activity;
         lastUpdate = System.currentTimeMillis();
         sm = (SensorManager)parentActivity.getSystemService(parentActivity.SENSOR_SERVICE);
         list = sm.getSensorList(Sensor.TYPE_ALL);
         String txt=""; //+list.size()+" "+list.get(0).getName() ;
         //  for(Sensor sensor: list){     txt+="\n"+sensor.getName();  }
         for (int i = 0; i < list.size(); i++) {
            txt+="<br>"+list.get(i).getName() + "  " + list.get(i).getVendor() + "  " + list.get(i).getVersion()+"" ;
         }
        webView.loadUrl("javascript: AccelInfo='"+txt+"'");
        webView.loadUrl("javascript: Light=-1"  );
        webView.loadUrl("javascript: Accel=''");
        webView.loadUrl("javascript: Prox=-1" );

        if(list.size()>0){
            sm.registerListener(sel, (Sensor) list.get(0), SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Toast.makeText(parentActivity, "В устройстве нет сенсоров.", Toast.LENGTH_LONG).show();
        }
    }

    public void StopSensor(){
        if(list.size()>0){
            sm.unregisterListener(sel);
        }
    }
    SensorManager sm = null;
    SensorEventListener sel = new SensorEventListener(){
        public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        public void onSensorChanged(SensorEvent event) {
            float[] values = event.values;
            if( event.sensor.getType() == Sensor.TYPE_LIGHT)
            {
                webView.loadUrl("javascript: Light="+values[0]  );
            }
            if( event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
            {
               webView.loadUrl("javascript: Accel="+"\"{'x':"+values[0]+" ,'y': "+values[1]+"'z': "+values[2]+"}\""  );
            }
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                webView.loadUrl("javascript: Prox="+values[0] );
            }

            /*
            TYPE_AMBIENT_TEMPERATURE	event.values[0]	°C	Ambient air temperature.
            TYPE_LIGHT	event.values[0]	lx	Illuminance.
            TYPE_PRESSURE	event.values[0]	hPa or mbar	Ambient air pressure.
            TYPE_RELATIVE_HUMIDITY	event.values[0]	%	Ambient relative humidity.
            TYPE_TEMPERATURE	event.values[0]	°C	Device temperature.1
            */
        }
    };
}
