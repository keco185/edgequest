#version 120
varying float radiusOut;
void main() {
	vec4 color = vec4(1.0, 1.0, 1.0, radiusOut * radiusOut);
	gl_FragColor = color;
}
