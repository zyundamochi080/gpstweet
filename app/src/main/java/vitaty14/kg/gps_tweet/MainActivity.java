//created by KG 2018/05/26
package vitaty14.kg.gps_tweet;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements LocationListener {

    LocationManager locationManager;
    Double lat,lon,alt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,},
                    1000);
        }
        else{
            locationStart();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    100, 5, this);
        }
    }

    private void locationStart(){
        Log.d("debug","locationStart()");
        locationManager =
                (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Log.d("debug", "location manager Enabled");
        } else {
            Intent settingsIntent =
                    new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
            Log.d("debug", "not gpsEnable, startActivity");
        }

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);

            Log.d("debug", "checkSelfPermission false");
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                1000, 50, this);
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,  @NonNull String[]permissions,  @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            // 許可
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("debug","checkSelfPermission true");
                locationStart();
            } else {
                // 拒絶
                Toast toast = Toast.makeText(this,
                        "何もできません", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        switch (status) {
            case LocationProvider.AVAILABLE:
                Log.d("debug", "LocationProvider.AVAILABLE");
                break;
            case LocationProvider.OUT_OF_SERVICE:
                Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                break;
            case LocationProvider.TEMPORARILY_UNAVAILABLE:
                Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // 取得日時の表示
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TextView textView_time = findViewById(R.id.textView_time);
        textView_time.setText("Time:"+df.format(location.getTime()));

        // 緯度の表示
        TextView textView_lat = findViewById(R.id.textView_lat);
        lat = location.getLatitude();
        textView_lat.setText("Latitude:"+String.format("%.5f",lat));

        // 経度の表示
        TextView textView_lon = findViewById(R.id.textView_lon);
        lon = location.getLongitude();
        textView_lon.setText("Longitude:"+String.format("%.5f",lon));

        // 高度の表示
        TextView textView_alt = findViewById(R.id.textView_alt);
        alt = location.getAltitude();
        textView_alt.setText("Altitude:"+String.format("%.2f",alt));
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tweet:
                Uri uri = Uri.parse("https://twitter.com/intent/tweet?text="+"今 "+ String.format("%.5f",lat) +","+ String.format("%.5f",lon) +" にいるよ");
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
