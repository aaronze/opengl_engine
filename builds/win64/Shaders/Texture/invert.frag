uniform sampler2D textureImage;

void main( void )
{
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	gl_FragColor = vec4(1.0-color.r, 1.0-color.g, 1.0-color.b, color.a);
}
