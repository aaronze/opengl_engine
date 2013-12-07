uniform sampler2D texDiffuse;
uniform sampler2D texEmissive;

uniform vec3 eyeVec;

varying vec3 position;
varying vec3 normal;

void main()
{
	vec4 texDiffuseVal = texture2D(texDiffuse, gl_TexCoord[0].st);
	vec4 texEmissiveVal = texture2D(texEmissive, gl_TexCoord[0].st);
	
	vec3 lightPos = normalize(gl_LightSource[0].position.xyz);
	vec3 vecToLight = normalize(lightPos - position);
	
	float NDotL = 1.0 - max(dot(normal, vecToLight), 0.0);
	float spec = 0.0;
	
	float lambert = dot(normal, vecToLight);
	if (lambert > 0.0) {
		vec3 E = normalize(eyeVec);
		vec3 R = reflect(-vecToLight, normal);
		float shininess = 1.5;
		spec = pow(max(dot(R, E), 0.0), shininess);
	}

	gl_FragColor = vec4((texDiffuseVal * NDotL + texEmissiveVal + gl_LightSource[0].specular * spec).xyz, 1.0);
	
}
