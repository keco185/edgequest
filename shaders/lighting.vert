#version 120
varying float radiusOut;
attribute float radius;
void main() {
	gl_Position = ftransform();
	radiusOut = radius;
}
