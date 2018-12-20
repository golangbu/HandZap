package com.handzap.pruthwirajs.handzap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;



public class FlowerGalleryActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayList<Flower> mFlowerDataSet = new ArrayList<>();
    ImageButton back_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flower);


        //Prepare DataSet
        mFlowerDataSet = prepareDataSet();
        back_btn = (ImageButton) findViewById(R.id.back_btn);


        //Initialize Grid View for programming
        GridView gridview = (GridView) findViewById(R.id.gridView);

        //Connect DataSet to Adapter
        FlowerAdapter flowerAdapter = new FlowerAdapter(this, mFlowerDataSet);

        //Now Connect Adapter To GridView
        gridview.setAdapter(flowerAdapter);

        //Add Listener For Grid View Item Click
        gridview.setOnItemClickListener(this);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        //Show Name Of The Flower
        Toast.makeText(FlowerGalleryActivity.this, mFlowerDataSet.get(position).getFlowerName(),
                Toast.LENGTH_SHORT).show();
    }


    //Creating Data Set By Adding 6 flower objects
    private ArrayList<Flower> prepareDataSet() {

        ArrayList<Flower> flowerData = new ArrayList<>();

        Flower flower;

        //1st Item
        flower = new Flower();
        flower.setFlowerName("c 001");
        flower.setPhotoPath(R.drawable.alyssum);
        flowerData.add(flower);

        //2nd Item
        flower = new Flower();
        flower.setFlowerName("c 002");
        flower.setPhotoPath(R.drawable.daisy);
        flowerData.add(flower);


        //3rd Item
        flower = new Flower();
        flower.setFlowerName("c 003");
        flower.setPhotoPath(R.drawable.jasmine);
        flowerData.add(flower);


        //4th Item
        flower = new Flower();
        flower.setFlowerName("c 004");
        flower.setPhotoPath(R.drawable.lily);
        flowerData.add(flower);


        //5th Item
        flower = new Flower();
        flower.setFlowerName("c 005");
        flower.setPhotoPath(R.drawable.poppy);
        flowerData.add(flower);


        //6th Item
        flower = new Flower();
        flower.setFlowerName("c 006");
        flower.setPhotoPath(R.drawable.rose);
        flowerData.add(flower);

        //1st Item
        flower = new Flower();
        flower.setFlowerName("c 007");
        flower.setPhotoPath(R.drawable.alyssum);
        flowerData.add(flower);

        //2nd Item
        flower = new Flower();
        flower.setFlowerName("c 008");
        flower.setPhotoPath(R.drawable.daisy);
        flowerData.add(flower);


        //3rd Item
        flower = new Flower();
        flower.setFlowerName("c 009");
        flower.setPhotoPath(R.drawable.jasmine);
        flowerData.add(flower);


        //4th Item
        flower = new Flower();
        flower.setFlowerName("c 010");
        flower.setPhotoPath(R.drawable.lily);
        flowerData.add(flower);


        //5th Item
        flower = new Flower();
        flower.setFlowerName("c 011");
        flower.setPhotoPath(R.drawable.poppy);
        flowerData.add(flower);


        //6th Item
        flower = new Flower();
        flower.setFlowerName("c 012");
        flower.setPhotoPath(R.drawable.rose);
        flowerData.add(flower);

        return flowerData;

    }
}
