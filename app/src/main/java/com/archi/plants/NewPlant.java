/*
To-DO
Set images
Make photo
Set name
Set Type
Set watering plan
Save plant and go back
        */

package com.archi.plants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class NewPlant extends AppCompatActivity {

    DBmain dBmain;
    SQLiteDatabase sqLiteDatabase;
    final String SETTING_LAST_ID = "SETTING_LAST_ID";
    final String TABLENAME = "plants";
    public SharedPreferences sPref;
    public int id;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String[]cameraPermission;
    String[]storagePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_plant);
        ListView plan_list = (ListView) findViewById(R.id.plan_list);
        TextView check_wat = (TextView) findViewById(R.id.check_wat);
        Button bt_save = (Button) findViewById(R.id.bt_save);

        sPref = getPreferences(MODE_PRIVATE);
        id = sPref.getInt(SETTING_LAST_ID, 0);

        plan_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        plan_list.setVisibility(View.GONE);
        check_wat.setText("Everyday");

        dBmain=new DBmain(this);

        // editData();
        imagePick();
        insertData();




        check_wat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick_wat();
            }
        });

  /*      bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                finish();
            }
        });
*/



    };



    public void onclick_wat() {

        ListView plan_list = (ListView) findViewById(R.id.plan_list);
        plan_list.setVisibility(View.VISIBLE);
        plan_list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.w_plan,
                android.R.layout.simple_list_item_single_choice);
        plan_list.setAdapter(adapter);


        plan_list.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(android.widget.AdapterView<?> parent, View view, int position, long id) {
                String text = (String) plan_list.getItemAtPosition(position);
                TextView check_wat = (TextView) findViewById(R.id.check_wat);
                check_wat.setText(text);
                plan_list.setVisibility(View.GONE);

            }}

        );};


    private void editData() {
        Log.v("ArchiDebug", "Edit");


        if (getIntent().getBundleExtra("userdata")!=null){
            ImageView image = (ImageView) findViewById(R.id.image_new_plant);
            Bundle bundle=getIntent().getBundleExtra("userdata");
            id=bundle.getInt("id");
            //for image
            byte[]bytes=bundle.getByteArray("image");
            Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
            image.setImageBitmap(bitmap);
        }
    }

    private void imagePick() {
        Log.v("ArchiDebug", "imagePick");
        ImageView image = (ImageView) findViewById(R.id.image_new_plant);
        image.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                int avatar=0;
                if (avatar==0){
                    if (!checkCameraPermission()){
                        requestCameraPermission();
                    }else {
                        pickFromGallery();
                    }
                }else if (avatar==1){
                    if (!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission,CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result2=ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    private void insertData() {
        Log.v("ArchiDebug", "insertData");
        Button bt_save = (Button) findViewById(R.id.bt_save);
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues cv=new ContentValues();

                ImageView image = (ImageView) findViewById(R.id.image_new_plant);
                EditText text_name_in = (EditText) findViewById(R.id.text_name_in);
                EditText text_type_in = (EditText) findViewById(R.id.text_type_in);
                String name = text_name_in.getText().toString();
                String type = text_type_in.getText().toString();
                TextView check_wat = (TextView) findViewById(R.id.check_wat);
                int waterplan;

                switch (check_wat.getText().toString())
                {
                    case  ("Every 2 days"):
                        waterplan=2;
                        break;
                    case  ("Every 3 days"):
                        waterplan=3;
                        break;
                    case  ("Every 4 days"):
                        waterplan=4;
                        break;
                    case  ("Every 5 days"):
                        waterplan=5;
                        break;
                    case  ("Every 6 days"):
                        waterplan=6;
                        break;
                    case  ("Every week"):
                        waterplan=7;
                        break;
                    case  ("Every 2 weeks"):
                        waterplan=14;
                        break;
                    default:
                        waterplan=1;;
                        break;

                }



                //cv.put("image",ImageViewToByte(image));

                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.def_ava);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                cv.put("image",bitMapData);
                cv.put("name",name);

                Log.v("ArchiDebug", "name " + name);

                cv.put("type",type);

                Log.v("ArchiDebug", "type " + type);

                cv.put("wat",waterplan);

                Log.v("ArchiDebug", "waterplan " + waterplan);



                sqLiteDatabase=dBmain.getWritableDatabase();
                Long recinsert=sqLiteDatabase.insert(TABLENAME,null,cv);

                Log.v("ArchiDebug", "recinsert " + recinsert);

                finish();
            }
        });

    }

    private byte[] ImageViewToByte(ImageView image) {
        Bitmap bitmap=((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0){
                    boolean camera_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if (camera_accept&&storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if (grantResults.length>0){
                    boolean storage_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if (storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }


    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result=CropImage.getActivityResult(data);
            if (resultCode==RESULT_OK){
                Uri resultUri=result.getUri();
                ImageView image = (ImageView) findViewById(R.id.image_new_plant);
                Picasso.with(this).load(resultUri).into(image);
            }
        }
    }


}