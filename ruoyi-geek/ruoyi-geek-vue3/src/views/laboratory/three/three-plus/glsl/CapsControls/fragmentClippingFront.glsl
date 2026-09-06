uniform vec3 color;
uniform vec3 clippingLow;
uniform vec3 clippingHigh;

varying vec3 pixelNormal;
varying vec4 worldPosition;
varying vec3 camPosition;

void main(void) {

    float shade = (3.0 * pow(abs(pixelNormal.y), 2.0) + 2.0 * pow(abs(pixelNormal.z), 2.0) + 1.0 * pow(abs(pixelNormal.x), 2.0)) / 3.0;

    if(worldPosition.x < clippingLow.x && camPosition.x < clippingLow.x || worldPosition.x > clippingHigh.x && camPosition.x > clippingHigh.x || worldPosition.y < clippingLow.y && camPosition.y < clippingLow.y || worldPosition.y > clippingHigh.y && camPosition.y > clippingHigh.y || worldPosition.z < clippingLow.z && camPosition.z < clippingLow.z || worldPosition.z > clippingHigh.z && camPosition.z > clippingHigh.z) {

        discard;

    } else {

        gl_FragColor = vec4(color * shade, 1.0);

    }

}