uniform sampler2D textureImage;

void main( void )
{
	float step = 1.0 / 278.0;
	
	vec4 result = vec4(0,0,0,0);
	
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, -step));
	result += 2.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(0, -step));
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, -step));
	
	result += 2.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, 0));
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy);
	result += 2.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, 0));
	
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, step));
	result += 2.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(0, step));
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, step));

	result /= 16.0;
	gl_FragColor = result;
}
