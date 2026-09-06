import * as THREE from 'three'
import { OutlinePass } from 'three/examples/jsm/postprocessing/OutlinePass';
export class SelectControls extends THREE.Controls<{}> {
    public outlinePass: OutlinePass
    onSelect?: (obj: THREE.Object3D<THREE.Object3DEventMap>, event: MouseEvent) => void
    constructor(scene: THREE.Scene, camera: THREE.Camera, domElement?: HTMLElement | null) {
        super(camera, domElement)
        if (!this.domElement) throw new Error("domElement is null")
        const v2 = new THREE.Vector2(this.domElement.offsetWidth, this.domElement.offsetHeight);
        this.outlinePass = new OutlinePass(v2, scene, camera);
        this.outlinePass.edgeStrength = 2; // 描边宽度
        this.outlinePass.edgeGlow = 0.3; // 边缘发光强度
        this.outlinePass.visibleEdgeColor.set(new THREE.Color(0, 255, 0)); // 红色描边
        this.domElement.addEventListener("click", (event: MouseEvent) => {
            if (!this.enabled) return
            if (!this.domElement) throw new Error("domElement is null")
            const rect = this.domElement.getBoundingClientRect();
            const mouse = new THREE.Vector2(
                ((event.clientX - rect.left) / rect.width) * 2 - 1,
                -((event.clientY - rect.top) / rect.height) * 2 + 1
            );
            const raycaster = new THREE.Raycaster();
            raycaster.setFromCamera(mouse, camera);
            const intersects = raycaster.intersectObjects(scene.children, true);
            for (let intersect of intersects) {
                let obj3D = intersect.object
                if (obj3D.type == 'GridHelper' || obj3D.type == 'AxesHelper' || obj3D.type == 'TransformControlsPlane') {
                    continue
                }
                if (obj3D.userData.isCanSelect === false) {
                    continue
                }
                this.outlinePass.selectedObjects = [obj3D]
                console.log(obj3D);
                if (this.onSelect) this.onSelect(obj3D, event)
                return
            }
        })
    }

}