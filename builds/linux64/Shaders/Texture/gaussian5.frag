uniform sampler2D textureImage;

void main( void )
{
	float step = 1.0 / 278.0;
	
	vec4 result = vec4(0,0,0,0);
	
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step-step, -step-step);
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, -step-step);
	result += 7.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(0, -step-step);
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, -step-step);
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step+step, -step-step);
	
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step-step, -step);
	result += 16.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, -step);
	result += 26.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(0, -step);
	result += 16.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, -step);
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step+step, -step);
	
	result += 7.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step-step, 0);
	result += 26.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, 0);
	result += 41.0 * texture2D(textureImage, gl_TexCoord[0].xy;
	result += 26.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, 0);
	result += 7.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step+step, 0);
	
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step-step, step);
	result += 16.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, step);
	result += 26.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(0, step);
	result += 16.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, step);
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step+step, step);
	
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step-step, step+step);
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(-step, step+step);
	result += 7.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(0, step+step);
	result += 4.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step, step+step);
	result += 1.0 * texture2D(textureImage, gl_TexCoord[0].xy + vec2(step+step, step+step);

	result /= 273.0;
	
	gl_FragColor = result;
}
