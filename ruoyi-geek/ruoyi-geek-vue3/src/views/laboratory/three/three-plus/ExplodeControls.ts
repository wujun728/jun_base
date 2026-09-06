import * as THREE from 'three'
import { getWorldCenterPosition } from './utils';
export class ExplodeControls extends THREE.Controls<{}> {
    constructor(object: THREE.Object3D) {
        super(object, null)
    }
}
// 初始化爆炸数据保存到每个mesh的userdata上
export function initExplodeModel(modelObject: THREE.Object3D) {
    if (!modelObject) return;

    // 计算模型中心
    const explodeBox = new THREE.Box3();
    explodeBox.setFromObject(modelObject);
    const explodeCenter = getWorldCenterPosition(explodeBox);

    const meshBox = new THREE.Box3();
    modelObject.userData.canExplode = true;
    // 遍历整个模型，保存数据到userData上，以便爆炸函数使用
    modelObject.traverse(function (value: any) {
        if (value.isLine || value.isSprite) return;
        if (value.isMesh) {
            meshBox.setFromObject(value);
            const meshCenter = getWorldCenterPosition(meshBox);
            // 爆炸方向
            value.userData.canExplode = true;
            value.userData.worldDir = new THREE.Vector3()
                .subVectors(meshCenter, explodeCenter)
                .normalize();
            // 爆炸距离 mesh中心点到爆炸中心点的距离
            value.userData.worldDistance = new THREE.Vector3().subVectors(meshCenter, explodeCenter);
            // 原始坐标
            value.userData.originPosition = value.getWorldPosition(new THREE.Vector3());
            // mesh中心点
            value.userData.meshCenter = meshCenter.clone();
            value.userData.explodeCenter = explodeCenter.clone();
        }
    });
}

// 模型爆炸函数 
export function explodeModel(model: THREE.Object3D, scalar: number) {
    model.traverse(function (value) {
        // @ts-ignore
        if (!value.isMesh || !value.userData.originPosition) return;
        const distance = value.userData.worldDir
            .clone()
            .multiplyScalar(value.userData.worldDistance.length() * scalar);
        const offset = new THREE.Vector3().subVectors(
            value.userData.meshCenter,
            value.userData.originPosition
        );
        const center = value.userData.explodeCenter;
        const newPos = new THREE.Vector3().copy(center).add(distance).sub(offset);
        const localPosition = value.parent?.worldToLocal(newPos.clone());
        localPosition && value.position.copy(localPosition);
    });
};