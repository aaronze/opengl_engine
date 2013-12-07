// simple fragment shader

// 'time' contains seconds since the program was linked.
uniform sampler2D texture;
uniform vec3 CAMERA_POSITION;
varying vec3 normal;
varying vec3 position;

void main()
{
	vec4 specular = vec4(0.0);
	vec4 diffuse;
	vec3 norm = normalize(normal);
	vec3 lightVector = gl_LightSource[0].position.xyz - position;
	float dist = length(lightVector);
	float attenuation = 1.0 / (gl_LightSource[0].constantAttenuation + gl_LightSource[0].linearAttenuation * dist + gl_LightSource[0].quadraticAttenuation * dist * dist);
	lightVector = normalize(lightVector);
	float nxDir = max(0.0, dot(norm, lightVector));
	diffuse = gl_LightSource[0].diffuse * nxDir * attenuation;

	if (nxDir != 0.0) {
		vec3 cameraVector = normalize(CAMERA_POSITION - position.xyz);
		vec3 halfVector = normalize(lightVector + cameraVector);
		float nxHalf = max(0.0, dot(norm, halfVector));
		float specularPower = pow(nxHalf, gl_FrontMaterial.shininess);
		specular = gl_LightSource[0].specular * specularPower * attenuation;
	}
	vec4 texColor = texture2D(texture, gl_TexCoord[0].st);
	gl_FragColor = gl_LightSource[0].ambient + (diffuse * vec4(texColor.rgb, 1.0)) + (specular * texColor.a);
}
