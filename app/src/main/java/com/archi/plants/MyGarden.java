/*
To-DO:
Add new
Show all plants
Go to plant card
Delete plant

 */


package com.archi.plants;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class MyGarden extends AppCompatActivity {

    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    final String TABLENAME = "plants";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_garden);
        Button new_plant = (Button) findViewById(R.id.new_plant);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.recycler_m);
        recyclerView.setLayoutManager(linearLayoutManager);


        dBmain=new DBmain(this);


        displayData();



        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));



        new_plant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_new_plant();
            }
        });




}

    public void onclick_new_plant() {
        Intent intent = new Intent(this, NewPlant.class);
        startActivity(intent);
    }

    private void displayData() {
        sqLiteDatabase=dBmain.getReadableDatabase();

        Log.v("sqLiteDatabase", "cursor " + sqLiteDatabase);

        Cursor cursor=sqLiteDatabase.rawQuery("select *from "+TABLENAME+"",null);

        Log.v("ArchiDebug", "cursor " + cursor);


        ArrayList<Model>models=new ArrayList<>();

        Log.v("ArchiDebug", "models " + models);



        while (cursor.moveToNext()){



            int id=cursor.getInt(0);
            String name=cursor.getString(1);
            String type=cursor.getString(2);
            int wat = cursor.getInt(3);
            byte[]image=cursor.getBlob(4);



            models.add(new Model(id,name,type, wat, image));



        }

        cursor.close();

        myAdapter=new MyAdapter(this,R.layout.recyclerview_item,models,sqLiteDatabase);


        recyclerView = findViewById(R.id.recycler_m);
        recyclerView.setAdapter(myAdapter);
    }




}


