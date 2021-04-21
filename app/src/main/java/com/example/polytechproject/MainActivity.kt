package com.example.polytechproject

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import java.io.ByteArrayOutputStream

class MainActivity : AppCompatActivity() {
    //присвиаиваем переменной о совершении операции 1, чтобы активность заппустилась
    private val REQUEST_TAKE_PHOTO=1
    private val Pick_image = 2
    //создаем ImageView - элемент разметки для отображения изображений
    private lateinit var imageView: ImageView
    // вызываем метод создания активности
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // устанавливаем разметку
        setContentView(R.layout.activity_main)
        //теперь находим эту переменную в разметке, чтобы она выполняла функции
        imageView = findViewById(R.id.image)
        // и находим кнопку, чтобы её нажатие к чему-то приводило
        val button: Button = findViewById(R.id.button1)
        val btnPick : Button = findViewById(R.id.buttonPick)
        //обрабатываем нажатие кнопки
        btnPick.setOnClickListener(View.OnClickListener {
            val photoPickerIntent : Intent = Intent(Intent.ACTION_PICK)
            photoPickerIntent.type="image/*"
            startActivityForResult(photoPickerIntent, Pick_image)
        })
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
        if (requestCode==REQUEST_TAKE_PHOTO && resultCode== RESULT_OK) {
            //берем снимок и конвертируем в битмап формат для отображения
            val thumbnailBitmap = data?.extras?.get("data") as Bitmap
            val bs = ByteArrayOutputStream()
            thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs)
            val ba = bs.toByteArray()
            val i = Intent(this@MainActivity, ShowImage::class.java)
            i.putExtra("image", ba)
            startActivity(i)
//            //устанавливаем на экран изображение
//            imageView.setImageBitmap(thumbnailBitmap)
        }
            if (requestCode==Pick_image && resultCode== RESULT_OK){
                //берем снимок и конвертируем в битмап формат для отображения
                val thumbnailBitmap = data?.extras?.get("data") as Bitmap
                val bs = ByteArrayOutputStream()
                thumbnailBitmap.compress(Bitmap.CompressFormat.PNG, 50, bs)
                val ba = bs.toByteArray()
                val i = Intent(this@MainActivity, ShowImage::class.java)
                i.putExtra("image", ba)
                startActivity(i)
//            //устанавливаем на экран изображение
      }
    }
}