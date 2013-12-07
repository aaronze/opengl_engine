uniform sampler2D textureImage,maskImage;

void main( void )
{
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	vec4 mask = texture2D( maskImage, gl_TexCoord[0].st );
	if (mask.g > 0.5)
		gl_FragColor = color;
	else
		discard;
}
