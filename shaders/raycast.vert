#version 120
uniform float opacity;
varying float opacityOut;
void main() {
	gl_Position = ftransform();
    gl_TexCoord[0] = gl_MultiTexCoord0;
    opacityOut = opacity;
}
