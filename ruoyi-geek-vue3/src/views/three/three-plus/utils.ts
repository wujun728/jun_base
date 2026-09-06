import * as THREE from 'three'
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader'
import { DRACOLoader } from 'three/examples/jsm/loaders/DRACOLoader';
import { OBJLoader } from 'three/examples/jsm/loaders/OBJLoader'
import { MTLLoader } from 'three/examples/jsm/loaders/MTLLoader'
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader'

/**
 * 将事件坐标转换为标准化设备坐标系(NDC)
 * 
 * @param vector 接收坐标转换结果的Vector2对象
 * @param event 触发的鼠标或触摸事件
 * @param window 窗口对象
 */
export function setToNormalizedDeviceCoordinates(
    vector: THREE.Vector2,
    event: MouseEvent | TouchEvent,
    window: Window
) {
    vector.x = event instanceof MouseEvent ? event.clientX : (event.touches && event.touches[0].clientX);
    vector.y = event instanceof MouseEvent ? event.clientY : (event.touches && event.touches[0].clientY);
    vector.x = (vector.x / window.innerWidth) * 2 - 1;
    vector.y = - (vector.y / window.innerHeight) * 2 + 1;
}

/**
 * gltf/glb  fbx  obj+mtl(材质) 
 * @param model 
 * @param type 
 * @returns 
 */
export function loadModel(model: { gltf?: string, obj?: string, mtl?: string, fbx?: string }, type: "gltf" | "glb" | "obj" | "fbx", onProgress?: (progress: ProgressEvent<EventTarget>) => void): Promise<any> {
    return new Promise((resolve, reject,) => {
        if ((type == "gltf" || type == "glb") && !!model.gltf) {
            const gltfloader = new GLTFLoader()
            const dracoLoader = new DRACOLoader();
            dracoLoader.setDecoderPath('/draco/gltf/');
            dracoLoader.setDecoderConfig({ type: "js" });
            dracoLoader.preload();
            gltfloader.setDRACOLoader(dracoLoader);
            gltfloader.load(
                model.gltf,
                load => {
                    console.log(load);
                    resolve(load.scene)
                },
                progress => {
                    if (onProgress) onProgress(progress)
                },
                error => {
                    console.log(error);
                }
            )
        } else if (type == "obj" && !!model.obj) {
            if (!!model.mtl) {
                const mtlLoader = new MTLLoader()
                mtlLoader.load(model.mtl, (mtl) => {
                    mtl.preload()
                    const objLoader = new OBJLoader()
                    objLoader.setMaterials(mtl)
                    if (!!model.obj) {
                        objLoader.load(model.obj, resolve)
                    }
                })
            } else {
                const objLoader = new OBJLoader()
                objLoader.load(model.obj, resolve)
            }
        } else if (type == "fbx" && !!model.fbx) {
            const fbxLoader = new FBXLoader()
            fbxLoader.load(model.fbx, resolve)
        }
    })
}

export function getWorldCenterPosition(box: THREE.Box3, scalar = 0.5): THREE.Vector3 {
    return new THREE.Vector3().addVectors(box.max, box.min).multiplyScalar(scalar);
}

export function MeshStandardMaterialToShaderMaterial(msm: THREE.MeshStandardMaterial, sm: THREE.ShaderMaterial) {
    // 迁移基础属性
    if (msm.color) {
        sm.uniforms['color'].value.copy(msm.color);
    }
    sm.uniforms['roughness'].value = msm.roughness;
    sm.uniforms['metalness'].value = msm.metalness;
    if (msm.emissive) {
        sm.uniforms['emissive'].value.copy(msm.emissive);
    }
    
    // 迁移纹理属性
    if (msm.map) {
        sm.uniforms['map'].value = msm.map;
    }

    if (msm.envMap) {
        sm.uniforms['envMap'].value = msm.envMap;
        sm.uniforms['envMapIntensity'].value = msm.envMapIntensity;
    }

    if (msm.lightMap) {
        sm.uniforms['lightMap'].value = msm.lightMap;
        sm.uniforms['lightMapIntensity'].value = msm.lightMapIntensity;
    }

    if (msm.aoMap) {
        sm.uniforms['aoMap'].value = msm.aoMap;
        sm.uniforms['aoMapIntensity'].value = msm.aoMapIntensity;
    }

    if (msm.bumpMap) {
        sm.uniforms['bumpMap'].value = msm.bumpMap;
        sm.uniforms['bumpScale'].value = msm.bumpScale;
    }

    if (msm.normalMap) {
        sm.uniforms['normalMap'].value = msm.normalMap;
        sm.uniforms['normalScale'].value.copy(msm.normalScale);
    }

    if (msm.displacementMap) {
        sm.uniforms['displacementMap'].value = msm.displacementMap;
        sm.uniforms['displacementScale'].value = msm.displacementScale;
        sm.uniforms['displacementBias'].value = msm.displacementBias;
    }

    if (msm.alphaMap) {
        sm.uniforms['alphaMap'].value = msm.alphaMap;
    }

    // 确保其他属性也被正确设置
    sm.side = msm.side;
    sm.transparent = msm.transparent;
    sm.opacity = msm.opacity;
    sm.depthTest = msm.depthTest;
    sm.depthWrite = msm.depthWrite;
    sm.blending = msm.blending;
    sm.alphaTest = msm.alphaTest;
    sm.blendSrc = msm.blendSrc;
    sm.blendDst = msm.blendDst;
    sm.blendEquation = msm.blendEquation;
    sm.dithering = msm.dithering;
    sm.premultipliedAlpha = msm.premultipliedAlpha;
    sm.visible = msm.visible;
    sm.toneMapped = msm.toneMapped;
}

export function MeshBasicMaterialToShaderMaterial(basicMat: THREE.MeshBasicMaterial, shaderMat: THREE.ShaderMaterial) {
    console.log(basicMat);

    // 迁移基础属性
    if (basicMat.color) {
        shaderMat.uniforms['color'].value.copy(basicMat.color);
    }
    shaderMat.uniforms['opacity'].value = basicMat.opacity;

    // 迁移纹理属性
    if (basicMat.map) {
        shaderMat.uniforms['map'].value = basicMat.map;
    }
    if (basicMat.alphaMap) {
        shaderMat.uniforms['alphaMap'].value = basicMat.alphaMap;
    }
    // 确保其他属性也被正确设置
    shaderMat.side = basicMat.side;
    shaderMat.transparent = basicMat.transparent;
    shaderMat.depthTest = basicMat.depthTest;
    shaderMat.depthWrite = basicMat.depthWrite;
    shaderMat.blending = basicMat.blending;
    shaderMat.alphaTest = basicMat.alphaTest;
    shaderMat.blendSrc = basicMat.blendSrc;
    shaderMat.blendDst = basicMat.blendDst;
    shaderMat.blendEquation = basicMat.blendEquation;
    shaderMat.dithering = basicMat.dithering;
    shaderMat.premultipliedAlpha = basicMat.premultipliedAlpha;
    shaderMat.visible = basicMat.visible;
    shaderMat.toneMapped = basicMat.toneMapped;
    shaderMat.wireframe = basicMat.wireframe;
    shaderMat.wireframeLinewidth = basicMat.wireframeLinewidth;
    shaderMat.fog = basicMat.fog;
    // 设置 needsUpdate 标志为 true 以确保着色器和uniforms得到更新
    shaderMat.needsUpdate = true;
}