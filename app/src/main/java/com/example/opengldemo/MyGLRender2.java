package com.example.opengldemo;

import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRender2 implements GLSurfaceView.Renderer {


    public MyGLRender2(){
        //分配顶点空间，每个浮点型占4个字节空间
//        ByteBuffer.allocateDirect()

    }
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {

    }

    @Override
    public void onDrawFrame(GL10 gl) {

    }
}
