/* To-do:
Create styles
Recreate images
Remake settings logo
No watering for today
Watering for today scrollable
Watering for today
Plus and minus watering
To my garden and back
Yandex-API hum (region + without key + cash + exception)
When was missed recalculate

*/


package com.archi.plants;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        String key = getString(R.string.API_KEY);

        Button settings = (Button) findViewById(R.id.settings);
        TextView humidity = (TextView) findViewById(R.id.humidity);
        ImageView yandex_logo = (ImageView) findViewById(R.id.yandex_logo);
        Log.d ("WebLog", "Hi");
        DBHelper dbHelper;
        dbHelper = new DBHelper(this);

    /*    humidity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Yandex(key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
*/

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_settings();
            }
        });

        yandex_logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_yandex_logo();
            }
        });


    }

    public static void Yandex(String key) throws Exception {

        Log.d ("WebLog", "We run Yandex");
        String JSON =  new HttpClient().sendGet(key);
        Log.d ("WebLog", "Tried new HTTP");
        JSONObject json = new JSONObject(JSON);
        Log.d ("WebLog", "Start parse");
        JSONArray forecasts = json.getJSONArray("forecasts");
        JSONObject f_0 = forecasts.getJSONObject(0);
        JSONObject parts = f_0.getJSONObject("parts");
        JSONObject day = parts.getJSONObject("day");
        double temp_avg = day.getDouble("temp_avg");
        //System.out.println("temp_avg =" + temp_avg);
        Log.d("MyLogs", "Temp av" + temp_avg);


    }


    public void onclick_settings() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void onclick_yandex_logo() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://yandex.ru/pogoda"));
        startActivity(browserIntent);
    }


 }

class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "appDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table plants ("
                + "id integer primary key autoincrement,"
                + "name text,"
                + "type text,"
                + "waterplan integer,"
                + "image blob" + ");");
        db.execSQL("create table waterings ("
                + "id integer primary key autoincrement,"
                + "date integer,"
                + "status integer" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}




class HttpClient {

    // one instance, reuse
    private final OkHttpClient httpClient = new OkHttpClient();

    public HttpClient() throws FileNotFoundException {
    }

    public static void main(String key) throws Exception {

        HttpClient obj = new HttpClient();
        String response = obj.sendGet(key);


    }







    public String sendGet(String key) throws Exception {

        String httpresponse;
        Request request = new Request.Builder()
                .url("https://api.weather.yandex.ru/v2/forecast?lat=55.75396&lon=37.620393&limit=3&hours=false")
                .addHeader("X-Yandex-API-Key", key)  // add request headers
                .build();

        try (Response response = httpClient.newCall(request).execute()) {

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            return response.body().string();

        }

    }





}