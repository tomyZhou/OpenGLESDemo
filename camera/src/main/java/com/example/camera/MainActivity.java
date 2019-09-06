package com.example.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;


public class MainActivity extends Activity {
    private static final String TAG = "TAG";
    CameraGLSurfaceView glSurfaceView = null;
    ImageView shutterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED) {
            Log.i("TEST", "Granted");
            onCreateView();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);//1 can be another integer
        }
    }

    private void onCreateView() {
        setContentView(R.layout.activity_main);
        glSurfaceView = findViewById(R.id.camera_textureview);
        shutterBtn = findViewById(R.id.ib_take_photo);
        //拍照
        shutterBtn.setOnClickListener(new BtnListeners());
    }

    //拍照
    private class BtnListeners implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ib_take_photo:
                    CameraInterface.getInstance().doTakePicture();
                    Toast.makeText(MainActivity.this, "拍照", 0).show();
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        glSurfaceView.onPause();
    }

}

