package com.example.opengldemo;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class Square {

    private final int mProgram;

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position = vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";



    //第一个顶点占用几个变量
    private static final int COORDS_PER_VERTEX = 3;
    /**
     * 首先它是三维的笛卡尔坐标系：原点在屏幕正中，x轴从屏幕左向右，最左是-1，最右是1；y轴从屏幕下向上，
     * 最下是-1，最上是1；z轴从屏幕里面向外，最里面是-1，最外面是1。
     */
    static float squareCoords[] = {
            -0.5f, 0.5f, 0.0f,   // top left
            -0.5f, -0.5f, 0.0f,   // bottom left
            0.5f, -0.5f, 0.0f,   // bottom right
            0.5f, 0.5f, 0.0f}; // top right
    
    private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;

    private short drawOrder[] = {0, 1, 2, 1, 2, 3}; // 绘制顺序，绘制两个三角形，一共中有四个点，有些点是重复使用。
    float color[] = {200, 0, 0, 1.0f};


    public Square() {

        //准备缓冲区空间
        // 初始化ByteBuffer，长度为arr数组长度*4，因为一个float占四个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length * 4);

        //数组排列用nativeOrder
        bb.order(ByteOrder.nativeOrder());

        //从ByteBuffer创建一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();

        //将坐标添加到FloatBuffer
        vertexBuffer.put(squareCoords);

        //设置缓冲区来读取第一个坐标
        vertexBuffer.position(0);

        //索引对象绘制需要多一个drawListBuffer缓冲区，指定绘制顺序
        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        //准备着色器
        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // 创建空的OpenGL ES程序
        mProgram = GLES20.glCreateProgram();

        // 添加顶点着色器到程序中
        GLES20.glAttachShader(mProgram, vertexShader);

        // 添加片段着色器到程序中
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 创建OpenGL ES程序可执行文件
        GLES20.glLinkProgram(mProgram);


    }

    public static int loadShader(int type, String shaderCode) {

        // 创造顶点着色器类型(GLES20.GL_VERTEX_SHADER)
        // 或者是片段着色器类型 (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // 添加上面编写的着色器代码并编译它
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    private int mPositionHandle;
    private int mColorHandle;

    //9个值，每个点占用3个值，相除得到的就是顶点的个数
    private final int vertexCount = squareCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 一个点是三维坐标，需要3个值来表示，一个值要4个字节的空间

    public void draw() {
        // 将程序添加到OpenGL ES环境
        GLES20.glUseProgram(mProgram);

        // 获取顶点着色器的位置的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 启用三角形顶点位置的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //填充坐标数据到点着色器代码中
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);

        // 获取片段着色器的颜色的句柄
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        // 设置绘制三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // 使用glDrawArrays绘制索引对象，对复杂的对象（有顶点重合）使用绘制索引对象的方式 。
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // 禁用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }
}