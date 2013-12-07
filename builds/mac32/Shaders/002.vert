// simple vertex shader

float rand(vec2 co)
{
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}

void main()
{
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	float r = rand(gl_Vertex.xy);
	float density = 0.8;
	if (r < density) gl_FrontColor  = vec4(0, rand(vec2(gl_Vertex.z, 1))*0.4+0.5, 0, 1);
	else gl_FrontColor = vec4(0, 0, 0, 0);
	gl_TexCoord[0] = gl_MultiTexCoord0;
}
