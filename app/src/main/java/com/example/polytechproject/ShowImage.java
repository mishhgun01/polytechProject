package com.example.polytechproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;

public class ShowImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        ImageView imageView = (ImageView) findViewById(R.id.imageBitmap);
        Bitmap b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"),0,
                getIntent().getByteArrayExtra("image").length);
        imageView.setImageBitmap(b);
    }
}