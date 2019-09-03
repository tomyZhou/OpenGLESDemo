package com.example.airhockey2;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * OpenGL 入门学习 https://yq.aliyun.com/articles/118206?spm=a2c4e.11153940.0.0.28231bbdHNBCF9
 */
public class Renderer1 implements GLSurfaceView.Renderer {

    private Context mContext;
    private final FloatBuffer vertexData;
    private int point_dimission = 2; //有多少个分量与每一个顶点相关联。点数据的维度，我们这里只定义了二维。但是在着色器是我们用的是vec4四维，如果一个维度没有赋值，OpenGL会把前三个rgb设置为0，最后一个透明度设置为1
    private float[] pointsArray = {
            // Triangle 1
            -0.5f, -0.5f,
            0.5f, 0.5f,
            -0.5f, 0.5f,
            // Triangle 2
            -0.5f, -0.5f,
            0.5f, -0.5f,
            0.5f, 0.5f,
            // Line 1
            -0.5f, 0f,
            0.5f, 0f,
            // Mallets
            0f, -0.25f,
            0f, 0.25f,

            //Point  中心处画一个点
            0f, 0f
    };
    private int uColorLocation;
    private int aPositionLocation;

    public Renderer1(Context context) {
        mContext = context;

        //申请一块本地内存（不是虚拟机的空间,不会被divaik虚拟机回收）存放我们的数据
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(pointsArray.length * 4); //一共申请这么多个点的空间，一个点占用4个字节
        byteBuffer.order(ByteOrder.nativeOrder());
        byteBuffer.position(0); //从0开始
        vertexData = byteBuffer.asFloatBuffer();

        //在本地内存中创建了一个缓冲区，称为vertexData，并把这些位置点数据复制到这个缓冲区内
        vertexData.put(pointsArray);
    }


    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //获取OpenGl 着色代码 点着色器，片着色器
        String vertexShader = ReadGLSLHelper.ReadGLSL(mContext, "simple_vertex_shader.glsl");  //点着色器，控制点的位置
        String fragmentShader = ReadGLSLHelper.ReadGLSL(mContext, "simple_frag_shader.glsl");   //面着色器，控制颜色

        //创建点着色器id和shader代码关联起来，并编译shader
        int vertexShaderId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertexShaderId, vertexShader);
        GLES20.glCompileShader(vertexShaderId);

        //创建面着色器id并和shader代码关联起来，并编译shader
        int fragmentShaderId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderId, fragmentShader);
        GLES20.glCompileShader(fragmentShaderId);

        //创建OpenGL程序对象
        int programId = GLES20.glCreateProgram();
        //附上点着色器
        GLES20.glAttachShader(programId, vertexShaderId);
        //附上片着色器
        GLES20.glAttachShader(programId, fragmentShaderId);
        //链接程序 将这些着色器联合起来
        GLES20.glLinkProgram(programId);

        /**
         * 做最后的拼接
         *
         * 我们学习了如何使用属性数组定义一个物体的结构，
         * 也学习了如何创建着色器、
         * 加载并编译它们，
         * 以及把它们链接起来形成一个OpenGL的程序。
         * 现在是时候在这个基础上开始构建并把它们拼接起来了
         */

        //告诉OpenGL绘制图形的时候用上面定义的程序
        GLES20.glUseProgram(programId);

        //定义这个变量就是给simple_frag_shader.glsl里定义的u_Color赋值的
        uColorLocation = GLES20.glGetUniformLocation(programId, "u_Color");

        //定义这个变量就是给simple_vertex_shader.glsl里定义的a_Position赋值的
        aPositionLocation = GLES20.glGetAttribLocation(programId, "a_Position");

        //告诉OpenGL从这个缓冲区中读取数据之前，需要确保它会从开头处开始读取数据，而不是中间或者结尾处。
        vertexData.position(0);
        //绑定顶点数据vertextData到着色器的a_Position变量
        GLES20.glVertexAttribPointer(aPositionLocation, point_dimission, GLES20.GL_FLOAT, false, 0, vertexData);

        //enable顶点数据
        GLES20.glEnableVertexAttribArray(aPositionLocation);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        //绘制图形
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        //画两个三角形
        GLES20.glUniform4f(uColorLocation, 1.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        //画两个三角形
        GLES20.glUniform4f(uColorLocation, 0.0f, 1.0f, 1.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 3, 5);

        GLES20.glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        GLES20.glDrawArrays(GLES20.GL_LINES, 6, 2);

        GLES20.glUniform4f(uColorLocation,0.8f,1.0f,0.3f,1.0f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,8,1);

        GLES20.glUniform4f(uColorLocation,1.0f,0.3f,0.3f,0.8f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,9,1);

        GLES20.glUniform4f(uColorLocation,0.0f,0.8f,0.3f,0.8f);
        GLES20.glDrawArrays(GLES20.GL_POINTS,10,1);
    }
}
