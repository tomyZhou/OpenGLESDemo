package com.example.camera;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.opengl.GLES11Ext;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * https://blog.csdn.net/weixin_41101173/article/details/80785397
 * <p>
 * 这次我们讲的GLSurfaceView预览Camera其实跟纹理贴图的思路差不多。可以理解成在 GLSurfaceView上画一个全屏大小的矩形，
 * <p>
 * 把图片换成了相机Camera数据，把Camera数据 纹理贴图到 这个画的矩形上面，就形成了预览，是不是瞬间秒懂！
 * <p>
 * 只不过还多了一个东西叫 SurfaceTexture ,这个东西是什么？我把它理解为 纹理层，相当于 view表面的一层衣服，它与GLSurfaceView的表面纹理id 挂钩。
 */
public class CameraGLSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, SurfaceTexture.OnFrameAvailableListener {

    private Context mContext;
    private String TAG = CameraGLSurfaceView.class.getSimpleName();
    private int mTextureID;
    private SurfaceTexture mSurface;
    private DirectDrawer mDirectDrawer;

    public CameraGLSurfaceView(Context context) {
        this(context, null);
    }

    public CameraGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        setEGLContextClientVersion(2);
        setRenderer(this);
        //根据纹理层的监听，有数据就绘制。正因是RENDERMODE_WHEN_DIRTY所以就要告诉GLSurfaceView什么时候Render，也就是啥时候进到onDrawFrame()这个函数里。
        setRenderMode(RENDERMODE_WHEN_DIRTY);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Log.i(TAG, "onSurfaceCreated");
        mTextureID = createTextureID();
        mSurface = new SurfaceTexture(mTextureID);
        mSurface.setOnFrameAvailableListener(this);
        mDirectDrawer = new DirectDrawer(mTextureID);
        CameraInterface.getInstance().doOpenCamera(); //①在创建surface时打开相机

    }

    private int createTextureID() {
        int[] texture = new int[1];

        GLES20.glGenTextures(1, texture, 0);
        GLES20.glBindTexture(GLES11Ext.GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameterf(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES11Ext.GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }


    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);

        if (!CameraInterface.getInstance().isPreviewing()) {
            CameraInterface.getInstance().doStartPreview(mSurface); //②预览
        }
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //绘制图形
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurface.updateTexImage(); //SurfaceTexture的关键方法
        float[] mtx = new float[16];
        mSurface.getTransformMatrix(mtx); //SurfaceTexture的关键方法
        mDirectDrawer.draw(mtx);  //③调用图形类的draw()方法
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        Log.i(TAG, "纹理层有新数据，就通知view绘制");
        this.requestRender();
    }

}
