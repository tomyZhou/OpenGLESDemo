uniform vec4 uMVPMatrix; //接收传入的转换矩阵

attribute vec4 aPosition;//接收传入的顶点
attribute vec2 aTextCoord;//接改传入的顶点纹理位置
varying vec2 vTextureCoord;//增加用于传给片元着色器的纹理位置变量
varying vec4 vPosition; //传顶点坐标给片元着色器

/**
 *  vTextureCoord （纹理坐标）
 *  vPosition （顶点坐标）
 *  varying  修饰的变量是传递给片元着色器
 */
void main(){
    gl_Position = aPosition;
    vPosition = uMVPMatrix * aPosition;
    vTextureCoord = aTextCoord;
}