attribute vec4 a_Position; //Attribute：顶点属性，也就是存储顶点的坐标信息(对于着色器只读)

void main(){
    gl_Position = a_Position;
    gl_PointSize = 30.0;
}