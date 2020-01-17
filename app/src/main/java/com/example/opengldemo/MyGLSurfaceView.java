package com.example.opengldemo;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 *  GLSurfaceView 是一个可以绘制OpenGLES图像的专门视图，它本身没有处理功能，
 *
 *  所绘制的图像都是由你所设置的GLSurfaceView.Render来控制的。
 *
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer renderer;

    public MyGLSurfaceView(Context context){
        super(context);

        // 声明你使用的是OpenGlES2.0的API
        setEGLContextClientVersion(2);

        renderer = new MyGLRenderer();

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(renderer);
    }
}