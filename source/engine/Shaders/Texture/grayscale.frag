uniform sampler2D textureImage;

void main( void )
{
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	float gray = (0.3 * color.x) + (0.6 * color.y) + (0.1 * color.z);
	gl_FragColor = vec4(gray, gray, gray, color.a);
}
