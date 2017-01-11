package com.hackingbuzz.importfromsdcard;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView imageView;


    public void getLocationOfPhoto() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);  // content:// (url) - External_Content_Uri ..location location of images in sd card (MediaStore)
        startActivityForResult(intent, 1);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                getLocationOfPhoto();
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);

        Button button = (Button)findViewById(R.id.button);

        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {


        if(Build.VERSION.SDK_INT < 23) {

            getLocationOfPhoto();

        } else
        // for alove L version we need to get permission at the door to get throught that location ...
        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {  // checkSelfPermission is a method avail in 23 api ..without if condition of (SDK_INT < 23 ) you cant implement it..

            requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE},1 );

        } else {

            getLocationOfPhoto();
        }



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  // data here has got the location we need to get image from
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri uri =  data.getData();  // getting location (ofcourse it a uri) ..got that in uri variable so that we can get image from there
            // lets get the image from there //  lets go to media store to get the image

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);  // we need contentResolver to get the content...without him we cant get the content...
                    imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }


    }
}
// read external store means we are not putting anything in external store ..we are getting something.......
