attribute vec4 a_Position; // //gl_Position、gl_PointSize、gl_FrontFacing、gl_FragColor、gl_FragCoord、gl_FrontFacing和gl_PointCoord：这些都是着色器内置的特殊变量，编译器已经定义好的

void main(){
    gl_Position = a_Position;
    gl_PointSize = 10.0;
}