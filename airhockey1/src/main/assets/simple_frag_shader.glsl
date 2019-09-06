precision mediump  float;  // 我们将默认精度设置为中等，我们不需要片段着色器中的高精度
uniform vec4 u_Color;
void main(){
    gl_FragColor = u_Color;
}