uniform sampler2D textureImage;
uniform float factor;

void main( void )
{
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	float red = color.x * factor;
	float green = color.y * factor;
	float blue = color.z * factor;
	gl_FragColor = vec4(red, green, blue, color.a);
}
