#version 120
varying vec4 opac;
varying vec2 position;
varying vec3 outColor;
void main() {
	gl_FragColor = vec4(outColor, mix(mix(opac.x,opac.y,position.x), mix(opac.w,opac.z,position.x), position.y));
}
