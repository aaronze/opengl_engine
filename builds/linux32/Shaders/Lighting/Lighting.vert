uniform sampler2D texBump;
uniform sampler2D texNormal;

varying vec3 position;
varying vec3 normal;

void main()
{
	gl_TexCoord[0] = gl_TextureMatrix[0] * gl_MultiTexCoord0;
	gl_FrontColor = gl_Color;

	gl_TexCoord[0].xy = gl_MultiTexCoord0.xy;
	
	vec4 texNormalVal = texture2D(texNormal, gl_TexCoord[0].st);
	vec3 norm = normalize(vec3((texNormalVal.x*2.0-1.0), (texNormalVal.y*2.0-1.0), (texNormalVal.z*2.0-1.0)));
	normal = norm;
	
	vec4 dv = texture2D(texBump, gl_MultiTexCoord0.xy );
	float df = 0.30*dv.x + 0.59*dv.y + 0.11*dv.z;
	vec4 newVertexPos = vec4(norm * df * 0.3, 0.0) + gl_Vertex;
	
	gl_Position = gl_ModelViewProjectionMatrix * newVertexPos;
	position = gl_Vertex.xyz;
}
