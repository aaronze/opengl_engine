void main()
{
	vec4 color = texture2D( textureImage, gl_TexCoord[0].st );
	float red = color.x;
	float green = color.y;
	float blue = color.z;
	gl_FragColor = vec4(red, green, blue, color.a);
}
