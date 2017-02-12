#version 120
uniform sampler2D sampler;
uniform sampler2D lightTex;
uniform sampler2D lightColorTex;
uniform vec3 ambientLight;
void main()
{
	vec4 terrainColor = texture2D(sampler, gl_TexCoord[0].xy);
	vec4 lightTexColor = texture2D(lightTex, gl_TexCoord[0].xy);
	vec4 lightColor = texture2D(lightColorTex, gl_TexCoord[0].xy);
	float brightness = (lightTexColor.r + lightTexColor.g + lightTexColor.b) / 3.0;
	vec4 color = vec4(0.0,0.0,0.0,1.0);
	color.r = terrainColor.r * (brightness * lightColor.r) * (1.0 - ambientLight.r) + ambientLight.r * terrainColor.r;
	color.g = terrainColor.g * (brightness * lightColor.g) * (1.0 - ambientLight.g) + ambientLight.g * terrainColor.g;
	color.b = terrainColor.b * (brightness * lightColor.b) * (1.0 - ambientLight.b) + ambientLight.b * terrainColor.b;
    gl_FragColor = color;
}
