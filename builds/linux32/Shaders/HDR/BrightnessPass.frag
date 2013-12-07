uniform float exposure;
uniform sampler2D SrcColor;
uniform sampler2D SrcHDR;
 
varying vec2 texCoord;
 
const vec4 gloomStart = vec4(0.95,0.95,0.95,0.95);
 
float sqr(float x) { return x*x; }
 
vec4 expand_Hdr(vec4 color) {
	return color*(sqr(color.a*2.0)+1.0);
}
 
void main(void) {
	vec4 color  = texture2D(SrcColor,texCoord);
	gl_FragColor = expand_Hdr(color*exposure)-gloomStart;
}