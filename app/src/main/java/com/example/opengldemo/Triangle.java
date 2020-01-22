package com.example.opengldemo;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Triangle {


    /**
     * shader的变量类型(uniform，attribute和varying)的区别
     * 关于shader的变量类型(uniform，attribute和varying)的区别及使用，下面做下说明：
     *
     * uniform:uniform变量在vertex和fragment两者之间声明方式完全一样，则它可以在vertex和fragment共享使用。
     * （相当于一个被vertex和fragment shader共享的全局变量）uniform变量一般用来表示：变换矩阵，材质，
     * 光照参数和颜色等信息。在代码中通过GLES20.glGetUniformLocation(int program, String name)来获取属性值。
     * 并通过 GLES20.glUniformMatrix4fv(int location, int count, boolean transpose, float[] value, int offset);
     * 方法将数据传递给着色器。
     * attribute:这个变量只能在顶点着色器中使用(vertex Shader),用来表示顶点的数据，比如顶点坐标，顶点颜色，法线，
     * 纹理坐标等。在绘制的时候通过GLES20.glGetAttribLocation（int program, String name）来获取变量值，通过
     * GLES20.glEnableVertexAttribArray(int index)来启动句柄，最后通过
     * GLES20.glVertexAttribPointer(int indx,int size,int type,boolean normalized,int stride,java.nio.Buffer ptr)
     * 来设置图形数据。
     * varying变量：这个变量只能用来在vertex和fragment shader之间传递数据时使用，不可以通过代码获取其变量值。
     *
     * 作者：aserbao
     * 链接：https://www.jianshu.com/p/92d02ac80611
     * 来源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     */
    private final String vertexShaderCode =
                    "uniform mat4 uMVPMatrix;" +
                    "varying   vec4 vColor;" +
                    "attribute vec4 aColor;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    "  gl_Position =  uMVPMatrix * vPosition;" +
                    "  vColor = aColor;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "varying  vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private final FloatBuffer colorBuffer;

    private FloatBuffer vertexBuffer;

    //一个点是几维的点，占几个变量,一个变量4个字节
    private static final int COORDS_PER_VERTEX = 3;

    private static float triangleCoords[] = {   // in counterclockwise order:
            0.0f, 0.2f, 0.0f, // top
            -0.2f, -0.2f, 0.0f, // bottom left
            0.2f, -0.2f, 0.0f  // bottom right
    };
    private int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;

    float color[] = {255, 110, 0, 1.0f};

    float vertexColor[] = {
            1.0f, 0f, 0f, 1.0f,
            0f, 1.0f, 0f, 1.0f,
            0f, 0f, 1.0f, 1.0f
    };
    private int mProgram;

    public Triangle() {

        //准备空间
        // 初始化ByteBuffer，长度为arr数组的长度*4，因为一个float占4个字节
        ByteBuffer bb = ByteBuffer.allocateDirect(triangleCoords.length * 4);
        // 数组排列用nativeOrder
        bb.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲区
        vertexBuffer = bb.asFloatBuffer();
        // 将坐标添加到FloatBuffer
        vertexBuffer.put(triangleCoords);
        // 设置缓冲区来读取第一个坐标
        vertexBuffer.position(0);

        colorBuffer = (FloatBuffer) ByteBuffer.allocateDirect(vertexColor.length * 4).
                order(ByteOrder.nativeOrder()).asFloatBuffer().put(vertexColor).position(0);


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
    private int mMVPMartrixHandle;
    private int mColorHandle;
    private int mVertexColorHandle;

    public void draw(float[] mvpMatrix) {

        //擦除前一帧的图形
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        // 将程序添加到OpenGL ES环境
        GLES20.glUseProgram(mProgram);

        // 获取顶点着色器的位置的句柄
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        mMVPMartrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");

        // 启用三角形顶点位置的句柄
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        //将坐标值设置到着色程序代码中去：attribute vec4 vPosition
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                COORDS_PER_VERTEX * 4, vertexBuffer); //一个点用三个坐标表示，一个坐标点4个字节

        GLES20.glUniformMatrix4fv(mMVPMartrixHandle, 1, false, mvpMatrix, 0);

        // 将颜色设置到着色代码中去:uniform vec4 vColor
//        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
//        GLES20.glUniform4fv(mColorHandle, 1, color, 0);


        mVertexColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");
        GLES20.glEnableVertexAttribArray(mVertexColorHandle);//修改顶点attribute属性都要爱enable
        GLES20.glVertexAttribPointer(mVertexColorHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, 0, colorBuffer);

        // 使用glDrawArrays 绘制三角形，下一节将讲解使用drawElements绘制索引对象。从第0个点开始绘制，绘制3个点
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // 禁用顶点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
        GLES20.glDisableVertexAttribArray(mVertexColorHandle);
    }
}