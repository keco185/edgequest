#version 120
uniform sampler2D sampler;
varying float opacityOut;
void main()
{
	vec4 color = texture2D(sampler, gl_TexCoord[0].xy);
	color.a = 1.0 - (color.r + color.g + color.b) / 3.0;
	color.a = color.a * opacityOut;
	color.r = 0.0;
	color.g = 0.0;
	color.b = 0.0;
    gl_FragColor = color;
}
