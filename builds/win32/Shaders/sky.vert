// simple vertex shader
uniform float time;

varying vec3 position;

void main()
{
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
	position = gl_Vertex.xyz;
	gl_FrontColor  = vec4(0, 0.6, 0.9, 1);
}
