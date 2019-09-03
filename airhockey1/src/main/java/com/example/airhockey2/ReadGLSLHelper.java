package com.example.airhockey2;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 读取 glsl 文本代码工具类
 * <p>
 * OpenGL Shading Language  OpenGL着色语言（OpenGL Shading Language）是用来在OpenGL中着色编程的语言，
 * <p>
 * 也即开发人员写的短小的自定义程序，他们是在图形卡的GPU （Graphic Processor Unit图形处理单元）上执行的，
 * <p>
 * 代替了固定的渲染管线的一部分，使渲染管线中不同层次具有可编程性
 */
public class ReadGLSLHelper {

    public static String ReadGLSL(Context context, String fileName) {
        StringBuilder body = new StringBuilder();

        try {
            InputStream is = context.getAssets().open(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(is); //把字节流转换成字符流，这样就可以一次都一行字符
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = reader.readLine()) != null) {
                body.append(line);
                body.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return body.toString();
    }

}
