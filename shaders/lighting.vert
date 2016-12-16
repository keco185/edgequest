#version 120
varying vec4 opac;
varying vec2 position;
varying vec3 outColor;
uniform vec4 opacity;
uniform vec3 color;
attribute vec2 posIn;
void main() {
	opac = opacity;
	gl_Position = ftransform();
	position = posIn;
	outColor = color;
}
