package com.example.opengldemo;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer implements GLSurfaceView.Renderer {

    private Triangle mTriangle;

    /**
     * shader语言跟C语言很像，它有一个主函数，也叫void main(){}。
     * gl_Position是一个内置变量，用于指定顶点，它是一个点，三维空间的点，所以用一个四维向量来赋值。vec4是四维向量的类型，vec4()是它的构造方法。等等，三维空间，
     * 不是（x, y, z）三个吗？咋用vec4呢？四维是叫做齐次坐标，它的几何意义仍是三维，先了解这么多，记得对于2D的话，第四位永远传1.0就可以了。这里，是指定原点
     * (0, 0, 0)作为顶点，就是说想在原点（正中心）位置画一个点。gl_PointSize是另外一个内置变量，用于指定点的大小。这个shader就是想在原点画一个尺寸为20的点
     *
     * 作者：alexhilton
     * 链接：https://www.jianshu.com/p/d483cae905a8
     * 来源：简书
     * 简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
     */
    private String VERTEX_SHADER =
            "void main() {\n" +
                    "gl_Position = vec4(0.0, 0.0, 0.0, 1.0);\n" +
                    "gl_PointSize = 200.0;\n" +
                    "}\n";
    private String FRAGMENT_SHADER =
            "void main() {\n" +
                    "gl_FragColor = vec4(1., 0., 0.0, 1.0);\n" +
                    "}\n";
    private int mGLProgram;


    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
        GLES20.glClearColor(1.0f, 1.0f, 0.0f, 1.0f);  //设置清空屏幕用的颜色,设置背景底色

        /**
         * shader
         * GL ES 2.0与1.0版本最大的区别在于，把渲染相关的操作用一个专门的叫作着色语言的程序来表达，全名叫作OpenGL ES Shading language，它是一个编程语言，与C语言非常类似，
         * 能够直接操作矩阵和向量，运行在GPU之上专门用于图形渲染。它又分为两种，一个叫做顶点着色器（vertex shader），另一个叫做片元着色器（fragment shader）。
         *前者用来指定几何形状的顶点；后者用于指定每个顶点的着色。每个GL程序必须要有一个vertex shader和一个fragment shader，且它们是相互对应的。（相互对应，
         * 意思是vertex shader必须要有一个fragment shader，反之亦然，但并不一定是一一对应）。当然，也是可以复用的，比如同一个vertex shader，可能会多个fragment shader
         * 来表达不同的着色方案。
         *
         */


       // 得到一个着色器的ID
        int vertextShaderId = GLES20.glCreateShader(GLES20.GL_VERTEX_SHADER);
        GLES20.glShaderSource(vertextShaderId,VERTEX_SHADER);
        GLES20.glCompileShader(vertextShaderId);

        // 得到一个着色器的ID
        int fragmentShaderId = GLES20.glCreateShader(GLES20.GL_FRAGMENT_SHADER);
        GLES20.glShaderSource(fragmentShaderId,FRAGMENT_SHADER);
        GLES20.glCompileShader(fragmentShaderId);

        mGLProgram = GLES20.glCreateProgram(); //创建shader program 句柄
        GLES20.glAttachShader(mGLProgram,vertextShaderId); // 把vertex shader添加到program
        GLES20.glAttachShader(mGLProgram,fragmentShaderId);// 把fragment shader添加到program
        GLES20.glLinkProgram(mGLProgram);// 做链接，可以理解为把两种shader进行融合，做好投入使用的最后准备工作


    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);  //擦除屏幕上的所有颜色，并用 glClearColor 中的颜色填充整个屏幕,因为我们要开始新一帧的绘制了，所以先清理，以免有脏数据。
        GLES20.glUseProgram(mGLProgram); // 告诉OpenGL，使用我们在onSurfaceCreated里面准备好了的shader program来渲染
        GLES20.glDrawArrays(GLES20.GL_POINTS,0,1); // 开始渲染，发送渲染点的指令， 第二个参数是offset，第三个参数是点的个数。目前只有一个点，所以是1。
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {  //设置了视口尺寸，告诉 OpenGL 可以用来渲染的 surface 的大小。
        GLES20.glViewport(0, 0, width, height);//（0, 0）是左上角，然后是width和heigh

    }
}