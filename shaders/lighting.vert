#version 120
varying vec4 color1;
varying vec4 color2;
varying vec4 color3;
varying vec4 color4;
varying vec2 position;
uniform vec3 color;
uniform vec4 opacity;
attribute vec2 posIn;
void main() {
	color1 = vec4(color, opacity[0]);
	color2 = vec4(color, opacity[1]);
	color3 = vec4(color, opacity[2]);
	color4 = vec4(color, opacity[3]);
	gl_Position = ftransform();
	position = posIn;
}
