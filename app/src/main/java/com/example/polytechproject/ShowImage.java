package com.example.polytechproject;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.googlecode.tesseract.android.TessBaseAPI;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ShowImage extends AppCompatActivity {


    private static final String TAG = MainActivity.class.getSimpleName();
        private TessBaseAPI tessBaseApi;
        TextView textView;
        Uri outputFileUri;
        private static final String lang = "eng";
        String result = "empty";

        private static final String DATA_PATH = Environment.getExternalStorageDirectory().toString() + "/TesseractSample/";
        private static final String TESSDATA = "tessdata";

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_image);
            ImageView imageView = (ImageView) findViewById(R.id.imageBitmap);
            Bitmap b = BitmapFactory.decodeByteArray(getIntent().getByteArrayExtra("image"),0,
                    getIntent().getByteArrayExtra("image").length);
            imageView.setImageBitmap(b);
            Button button = (Button) findViewById(R.id.detect);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = extractText(b);
                    Intent i = new Intent(ShowImage.this, AnswerActivity.class);
                    i.putExtra("text",text);
                    startActivity(i);
                }
            });
        }
//        private void doOCR() {
//            prepareTesseract();
//            startOCR(outputFileUri);
//        }
//
//        /**
//         * Prepare directory on external storage
//         *
//         * @param path
//         * @throws Exception
//         */
            private void prepareDirectory (String path){

                File dir = new File(path);
                if (!dir.exists()) {
                    if (!dir.mkdirs()) {
                        Log.e(TAG, "ERROR: Creation of directory " + path + " failed, check does Android Manifest have permission to write to external storage.");
                    }
                } else {
                    Log.i(TAG, "Created directory " + path);
                }
            }


            private void prepareTesseract () {
                try {
                    prepareDirectory(DATA_PATH + TESSDATA);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                copyTessDataFiles(TESSDATA);
            }

            /**
             * Copy tessdata files (located on assets/tessdata) to destination directory
             *
             * @param path - name of directory with .traineddata files
             */
            private void copyTessDataFiles (String path){
                try {
                    String fileList[] = getAssets().list(path);

                    for (String fileName : fileList) {

                        // open file within the assets folder
                        // if it is not already there copy it to the sdcard
                        String pathToDataFile = DATA_PATH + path + "/" + fileName;
                        if (!(new File(pathToDataFile)).exists()) {

                            InputStream in = getAssets().open(path + "/" + fileName);

                            OutputStream out = new FileOutputStream(pathToDataFile);

                            // Transfer bytes from in to out
                            byte[] buf = new byte[1024];
                            int len;

                            while ((len = in.read(buf)) > 0) {
                                out.write(buf, 0, len);
                            }
                            in.close();
                            out.close();

                            Log.d(TAG, "Copied " + fileName + "to tessdata");
                        }
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable to copy files to tessdata " + e.toString());
                }
            }


//            private void startOCR (Uri imgUri){
//                try {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 4; // 1 - means max size. 4 - means maxsize/4 size. Don't use value <4, because you need more memory in the heap to store your data.
//                    Bitmap bitmap = BitmapFactory.decodeFile(imgUri.getPath(), options);
//
//                    result = extractText(bitmap);
//
//                    textView.setText(result);
//
//                } catch (Exception e) {
//                    Log.e(TAG, e.getMessage());
//                }
//            }


            private String extractText (Bitmap bitmap){
                tessBaseApi = new TessBaseAPI();
                tessBaseApi.init(DATA_PATH, lang);
                tessBaseApi.setVariable(TessBaseAPI.VAR_CHAR_WHITELIST, "1234567890");
                tessBaseApi.setImage(bitmap);
                String extractedText = "empty result";
                try {
                    extractedText = tessBaseApi.getUTF8Text();
                } catch (Exception e) {
                    Log.e(TAG, "Error in recognizing text.");
                }
                tessBaseApi.end();
                return extractedText;
            }

        }