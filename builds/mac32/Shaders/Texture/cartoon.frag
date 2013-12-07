uniform sampler2D textureImage;

varying vec3 normal;

void main( void )
{
	normal = normalize(normal);
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	gl_FragColor = vec4(color.x, color.y, color.z, color.a);
}
