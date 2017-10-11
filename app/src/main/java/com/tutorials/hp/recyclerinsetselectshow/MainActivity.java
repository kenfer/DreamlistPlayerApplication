package com.tutorials.hp.recyclerinsetselectshow;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText nameTxt,posTxt;
    RecyclerView rv;
    MyAdapter adapter;
    public static final int CAMERA_PIC_REQUEST = 1;
    private ImageView imageViewer;


    ArrayList<Player> players=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog();
            }
        });



       rv= (RecyclerView) findViewById(R.id.mRecycler);

        rv.setLayoutManager(new LinearLayoutManager(this));

        rv.setItemAnimator(new DefaultItemAnimator());


        adapter=new MyAdapter(this,players);


        retrieve();


    }


    private void showDialog()
    {

        Dialog d=new Dialog(this);

        d.requestWindowFeature(Window.FEATURE_NO_TITLE);

        d.setContentView(R.layout.custom_layout);

        nameTxt= (EditText) d.findViewById(R.id.nameEditTxt);
        posTxt= (EditText) d.findViewById(R.id.posEditTxt);
        Button saveBtn= (Button) d.findViewById(R.id.saveBtn);
        Button addphoto = (Button) d. findViewById(R.id.addph);
        final Button retrievebtn= (Button) d.findViewById(R.id.retrieveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(nameTxt.getText().toString(),posTxt.getText().toString());

            }
        });



        retrievebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 retrieve();
            }
        });
        addphoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
            }
        });

        d.show();

    }

    private void save(String name,String pos)
    {
        DBAdapter db=new DBAdapter(this);


        db.openDB();


        long result=db.add(name,pos);

        if(result>0)
        {
            nameTxt.setText("");
            posTxt.setText("");
        }else
        {
            Snackbar.make(nameTxt,"Unable To Save",Snackbar.LENGTH_SHORT).show();
        }

        db.closeDB();


        retrieve();
    }


    private void retrieve()
    {
        players.clear();

        DBAdapter db=new DBAdapter(this);
        db.openDB();


        Cursor c=db.getAllPlayers();


        while (c.moveToNext())
        {
            int id=c.getInt(0);
            String name=c.getString(1);
            String pos=c.getString(2);


            Player p=new Player(id,name,pos,R.id.playerImage);


            players.add(p);
        }


        if(!(players.size()<1))
        {
            rv.setAdapter(adapter);
        }

        db.closeDB();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        imageViewer = (ImageView) findViewById(R.id.playerImage);
        imageViewer.setScaleType(ImageView.ScaleType.CENTER_CROP);

        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode){
            case CAMERA_PIC_REQUEST:
                if(resultCode==RESULT_OK){
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    imageViewer.setImageBitmap(thumbnail);


                }
        }
    }


}













