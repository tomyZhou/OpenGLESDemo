package com.example.opengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRender3 implements GLSurfaceView.Renderer {

    private Square mSqure;
    private Triangle mTriangle;
    private int angle = 0;

    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];

    private float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

//        mSqure = new Square();
        mTriangle = new Triangle();
    }

    //设置视图展示窗口
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float ratio = (float) width / height;

        //透视投影：随观察点的距离变化而变化，观察点越远，视图越小，反之越大，我们可以通过如下方法来设置透视投影：
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
//        mSqure.draw();
//        mTriangle.draw();

        //相机视图和透视视图合并
        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        //将mProjectionMatrix和mViewMatrix视图合并到mMVPMatrix视图中去
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

//        mTriangle.draw(mMVPMatrix);


        //设置一个旋转矩阵
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        angle++;

        //弄一个默认视图，把mMVPMatrix和mRotationMatrix合并到这里去
        float[] scratch = new float[16];

        //把mMVPMatrix视图和mRotationMatrix视图合并到stratch视图中去
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        mTriangle.draw(scratch);


    }
}
