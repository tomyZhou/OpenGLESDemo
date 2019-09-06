//冷色滤镜
precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D vTexture;

void main(){
    vec4 nColor = texture2D(vTexture, vTextureCoord);
    vec4 coldColor = nColor + vec4(0.0, 0.0, 0.3, 0.0);//多加点划蓝色就是冷色调
    gl_FragColor = coldColor;
}