uniform vec3 clippingLow;
uniform vec3 clippingHigh;

varying vec3 pixelNormal;
varying vec4 worldPosition;
varying vec3 camPosition;

varying vec2 vUv;  // 纹理坐标
void main() {
    vUv = uv;
    pixelNormal = normal;
    worldPosition = modelMatrix * vec4(position, 1.0);
    camPosition = cameraPosition;

    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);

}