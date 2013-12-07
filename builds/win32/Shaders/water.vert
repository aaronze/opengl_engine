// simple vertex shader

void main()
{
	gl_Position    = gl_ModelViewProjectionMatrix * gl_Vertex;
	gl_FrontColor  = vec4(1, 0, 0, 1);//gl_Color;
	gl_TexCoord[0] = gl_MultiTexCoord0;
}
