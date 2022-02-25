package com.archi.plants;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    int singledata;
    ArrayList<Model>modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    //generate constructor

    public MyAdapter(Context context, int singledata, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.singledata = singledata;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.recyclerview_item,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Model model=modelArrayList.get(position);
        byte[]pro_image=model.getImage();
        Bitmap bitmap= BitmapFactory.decodeByteArray(pro_image,0,pro_image.length);

        holder.image.setImageBitmap(bitmap);
        holder.name.setText(model.getUName());
        holder.type.setText(model.getType());

        switch (String.valueOf(model.getWat()))
        {
            case  ("2"):
                holder.wat.setText("Every 2 days");
                break;
            case  ("3"):
                holder.wat.setText("Every 3 days");
                break;
            case  ("4"):
                holder.wat.setText("Every 4 days");
                break;
            case  ("5"):
                holder.wat.setText("Every 5 days");
                break;
            case  ("6"):
                holder.wat.setText("Every 6 days");
                break;
            case  ("7"):
                holder.wat.setText("Every week");
                break;
            case  ("14"):
                holder.wat.setText("Every 2 week");
                break;
            default:
                holder.wat.setText("Everyday");
                break;

        }


            }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView name;
        TextView wat;
        TextView type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image=(ImageView)itemView.findViewById(R.id.image_plant);
            name=(TextView)itemView.findViewById(R.id.text_name);
            type=(TextView)itemView.findViewById(R.id.text_type);
            wat=(TextView)itemView.findViewById(R.id.text_wat);
            itemView.setOnCreateContextMenuListener(this::onCreateContextMenu);



        }



        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            menu.setHeaderTitle("Select The Action");
            menu.add(0, 1, 0, "Edit");//groupId, itemId, order, title
            menu.add(0, 2, 0, "Delete");

        }

    }
}