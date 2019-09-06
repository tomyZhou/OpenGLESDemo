//暖色滤镜
precision  mediump float;
varying vec2 vTextureCoord;
uniform sampler2D vTexture;

void main(){
    vec4 nColor = texure2D(vTexture, vTextureCoord);//进行纹理采样，拿到当前颜色
    vec4 warmColor = nColor + vec4(0.2, 0.2, 0.0, 0.0);//暖色就是多加点红跟绿
    gl_FragColor = warmColor;
}