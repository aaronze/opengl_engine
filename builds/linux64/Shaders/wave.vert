// wave.vert - moves vertices along the normals

// time contains the seconds sind the progam was linked.
uniform float time;


float rand(vec2 co) {
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}
//
// entry point.
//
void main( void )
{
	float scale = 2.0;
	float x = gl_Vertex.x;
	float y = gl_Vertex.y;

	// calculate a scale factor.
	float s = sin( (time+3.0*y)*scale );
	float c = cos( (time+5.0*x)*scale );
	float z = 0.05 * s * c;

	// offset the position along the normal.
	vec3 v = vec3(gl_Vertex.x, gl_Vertex.y, gl_Vertex.z + z*100.0);

	// transform the attributes.
	gl_Position = gl_ModelViewProjectionMatrix * vec4( v, 1.0 );
	gl_FrontColor = vec4(vec3(0, 0, 0.5), 0.5);
	gl_TexCoord[0] = gl_MultiTexCoord0;
}

