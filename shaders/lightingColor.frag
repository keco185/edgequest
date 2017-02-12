#version 120
varying float radiusOut;
uniform vec3 color;
void main() {
	vec4 color = vec4(color * color, radiusOut * radiusOut);
	gl_FragColor = color;
}
