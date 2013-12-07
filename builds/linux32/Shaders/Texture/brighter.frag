uniform sampler2D textureImage;

void main( void )
{
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	float red = color.x + 0.1;
	float green = color.y + 0.1;
	float blue = color.z + 0.1;
	gl_FragColor = vec4(red, green, blue, color.a);
}
