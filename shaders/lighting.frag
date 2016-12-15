#version 120
varying vec4 color1;
varying vec4 color2;
varying vec4 color3;
varying vec4 color4;
varying vec2 position;

void main() {
	//gl_FragColor = vec4(0.5,0.5,0.5,0.1);
	gl_FragColor = mix(mix(color1,color2,position.x), mix(color4,color3,position.x), position.y);
}
