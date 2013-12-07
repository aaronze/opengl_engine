
uniform float time;
varying vec3 position;

void main() {
	float val = noise1(position.xyz);
	if (val > 0.0) {
		float red = min(val*(position.x+1.0), 1.0);
		float green = 0.0;
		float blue = min(val*(2.0-position.x), 1.0);
		gl_FragColor = vec4(red, green, blue, 1.0);
	}
	else	gl_FragColor = vec4(0, 0, 0, 1);
}
