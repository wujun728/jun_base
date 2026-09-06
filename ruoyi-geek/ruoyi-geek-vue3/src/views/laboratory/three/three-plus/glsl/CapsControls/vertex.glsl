uniform vec3 color;
varying vec3 pixelNormal;

void main() {

    pixelNormal = normal;
    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);

}