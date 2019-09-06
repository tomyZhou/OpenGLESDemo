//发光滤镜
/**
*发光滤镜利用sin函数（取值是-1到1），然后abs取正（0到1），然后除以4，最终lightUpValue 的值是0到0.25，
*然后给rgb通道都加上一个 lightUpValue 值，就能实现变亮和变暗的效果，像在发光。
*/
precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D vTexture;
uniform float uTime; //应用传时间戳过来
void main() {
    float lightUpValue = abs(sin(uTime / 1000.0)) / 4.0;  //计算变化值，sin函数
    vec4 src = texture2D(vTexture, vTextureCoord);
    vec4 addColor = vec4(lightUpValue, lightUpValue, lightUpValue, 1.0);
    gl_FragColor = src + addColor;  //不断地添加一个颜色
}