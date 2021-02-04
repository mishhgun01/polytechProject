package com.example.polytechproject

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Camera
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.SurfaceHolder
import android.view.View
import android.widget.Button
import android.widget.ImageView

class MainActivity : AppCompatActivity() {
    //присвиаиваем переменной о совершении операции 1, чтобы активность заппустилась
    private val REQUEST_TAKE_PHOTO=1
    //создаем ImageView - элемент разметки для отображения изображений
    private lateinit var imageView: ImageView
    // вызываем метод создания активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // устанавливаем разметку
        setContentView(R.layout.activity_main)
        //теперь находим эту переменную в разметке, чтобы она выполняла функции
        imageView = findViewById(R.id.image)
        // и находим кнопку, чтобы её нажатие к чеу-то приводило
        val button: Button = findViewById(R.id.button1)
        //обрабатываем нажатие кнопки
        button.setOnClickListener(View.OnClickListener {
            //запускаем камеру
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO)
            }catch (e: ActivityNotFoundException){
                //ловим ошибки
                e.printStackTrace()
            }
        })
    }

    //обрабатываем вывод снимка на экран
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //если все ок, то:
        if (requestCode==REQUEST_TAKE_PHOTO && resultCode== RESULT_OK){
            //берем снимок и конвертируем в битмап формат для отображения
            val thumbnailBitmap = data?.extras?.get("data") as Bitmap
            //устанавливаем на экран изображение
            imageView.setImageBitmap(thumbnailBitmap)
        }
    }
}