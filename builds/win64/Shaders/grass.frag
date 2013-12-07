// simple fragment shader

// 'time' contains seconds since the program was linked.
uniform float time;

float rand(vec2 co)
{
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main()
{
	float r = rand(gl_FragCoord.xy);
	if (gl_Color.g < 0.4) gl_FragColor = gl_Color*r;
	else gl_FragColor = gl_Color;
}
