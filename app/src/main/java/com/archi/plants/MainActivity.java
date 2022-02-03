// To-do: разнести по методам
// Поправить тему письма
// Добавить комментариев


package com.archi.plants;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.*;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    boolean setting_notification_enabled;
    boolean setting_every_hour;
    final String SETTING_NOTIFICATION_ENABLED = "SETTING_NOTIFICATION_ENABLED";
    final String SETTING_EVERY_HOUR = "SETTING_EVERY_HOUR";
    final String SETTING_NOTIFICATION_HOUR = "SETTING_NOTIFICATION_HOUR";
    final String SETTING_NOTIFICATION_MIN = "SETTING_NOTIFICATION_MIN";
    final String SETTING_REGION = "SETTING_REGION";
    int notHour;
    int notMin;
    String region;
    SharedPreferences sPref;
    private Object AdapterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox check_setting_enable = findViewById(R.id.check_setting_enable);
        CheckBox check_setting_every = findViewById(R.id.check_setting_every);
        TextView check_setting_time = (TextView) findViewById(R.id.check_setting_time);
        TextView check_setting_region = (TextView) findViewById(R.id.check_setting_region);
        ListView region_list = (ListView) findViewById(R.id.region_list);
        Button button_support = (Button) findViewById(R.id.button_support);
        region_list.setVisibility(View.GONE);
        String build_value = getString(R.string.release);
        sPref = getPreferences(MODE_PRIVATE);
        setting_notification_enabled = sPref.getBoolean(SETTING_NOTIFICATION_ENABLED, true);
        setting_every_hour = sPref.getBoolean(SETTING_EVERY_HOUR, true);
        notHour = sPref.getInt(SETTING_NOTIFICATION_HOUR, 14);
        notMin = sPref.getInt(SETTING_NOTIFICATION_MIN, 00);
        region = sPref.getString(SETTING_REGION, "Moscow");
        check_setting_region.setText(region);
        button_support.setText("Press to contact support. Build " + build_value);

        if (notMin<9)
        {check_setting_time.setText(notHour + ":" + "0" + notMin );}
        else
        {check_setting_time.setText(notHour + ":" + notMin );}



        check_setting_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_time();
            }
        });

        check_setting_region.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_region();
            }
        });

        button_support.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_support();
            }
        });




        check_setting_enable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setting_notification_enabled = true;
                }
                else {
                    setting_notification_enabled = false;
                }
            }
        });

        check_setting_every.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setting_every_hour = true;
                }
                else {
                    setting_every_hour = false;
                }
            }
        });
    }


    public void onclick_time() {
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int notHour, int notMin) {
                TextView check_setting_time = (TextView) findViewById(R.id.check_setting_time);
                if (notMin<9)
                {check_setting_time.setText(notHour + ":" + "0" + notMin );}
                else
                {check_setting_time.setText(notHour + ":" + notMin );}


            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, timeSetListener, notHour, notMin, true);
        timePickerDialog.show();
    }

    public void onclick_support() {
        String email = getString(R.string.email);
        String build_value = getString(R.string.release);
        Intent i = new Intent(Intent.ACTION_SENDTO);
        i.putExtra(Intent.EXTRA_EMAIL  , new String[] { "Troubles with application" + build_value });
        i.setData(Uri.parse("mailto:" + email));
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }





    public void onclick_region() {

        ListView region_list = (ListView) findViewById(R.id.region_list);
        region_list.setVisibility(View.VISIBLE);
        region_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.cities,
                android.R.layout.simple_list_item_single_choice);
        region_list.setAdapter(adapter);


        region_list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String text = (String) region_list.getItemAtPosition(position);
                TextView check_setting_region = (TextView) findViewById(R.id.check_setting_region);
                check_setting_region.setText(text);
                region_list.setVisibility(View.GONE);

            }}

            );};





    @Override
    protected void onDestroy() {
        super.onDestroy();
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putBoolean(SETTING_NOTIFICATION_ENABLED, setting_notification_enabled);
        ed.putBoolean(SETTING_NOTIFICATION_HOUR, setting_every_hour);
        ed.putInt(SETTING_NOTIFICATION_HOUR, notHour);
        ed.putInt(SETTING_NOTIFICATION_MIN, notMin);
        ed.putString(SETTING_REGION, region);
        ed.commit();;
    }

    }