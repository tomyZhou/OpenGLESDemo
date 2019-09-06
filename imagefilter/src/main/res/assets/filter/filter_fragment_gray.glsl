//黑白滤镜
precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D vTexture;

void main(){
    vec4 nColor = texture2D(vTexture,vTextureCoord);//进行纹理采样，拿到当前颜色
    float c = (nColor.r + nColor.g + nColor.b)/3.0; //黑色处理就是将RGB的颜色相加再除以3，再作为RGB通道的值
    gl_FragColor = vec4(c,c,c,nColor.a);
}