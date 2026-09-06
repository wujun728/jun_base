import { ref, shallowReactive, watch } from "vue";
import { Director, TreeNode } from "./three-plus/ThreeHelper";
import * as THREE from 'three'
import { explodeModel, initExplodeModel } from "./three-plus/ExplodeControls";
const director = new Director({
    width: window.innerWidth,
    height: window.innerHeight,
})
function initFastKey() {
    document.addEventListener('keydown', (e) => {
        console.log(e.altKey);
        if (e.key === 'Escape') {
            e.preventDefault();
            unselectObject()
        }
        if (e.key === 'Backspace') {
            e.preventDefault();
            if (transform.object) {
                let o = director.getObjectBySingleUUID(transform.object.uuid)
                if (o) {
                    if (o.parent) {
                        o.parent.remove(o)
                    } else {
                        director.scene.remove(o)
                    }
                    refreshThree()
                }
            }
        }
    })
}
export function initDirector(dom: HTMLElement) {
    dom.appendChild(director.renderer.domElement)
    initFastKey()
    director.scene.background = new THREE.TextureLoader().load("/glb/bg.jpeg")
    director.switchAxesHelper(true)
    director.switchGridHelper(true)
    director.switchStats(true)
    director.startRender()
}

export default director


const transform = shallowReactive<{
    mode: "translate" | "rotate" | "scale",
    object: THREE.Object3D | null,
    explode: number
}>({
    object: null,
    mode: 'translate',
    explode: 0,
})

export function selectObject(object: THREE.Object3D) {
    transform.object = object
    director.controls.selectControls.outlinePass.selectedObjects = [transform.object]
    director.controls.dragControls.objects = [transform.object]
    initExplodeModel(transform.object)
}
export function selectNode(object: THREE.Object3D) {
    transform.object = object
    director.controls.selectControls.outlinePass.selectedObjects = [transform.object]
    director.controls.transformControls.attach(transform.object)
    director.controls.selectControls.enabled = false
    director.controls.dragControls.enabled = false
    director.scene.add(director.controls.transformControls.getHelper())
    initExplodeModel(transform.object)
}
export function unselectObject() {
    transform.object = null
    director.controls.selectControls.outlinePass.selectedObjects = []
    director.controls.transformControls.detach()
    director.controls.selectControls.enabled = true
    director.controls.dragControls.enabled = true
    director.scene.remove(director.controls.transformControls.getHelper())
}
watch(() => transform.mode, () => {
    if (!director) return
    director.controls.transformControls.setMode(transform.mode)
})
watch(() => transform.explode, value => {
    if (!director || !transform.object) return
    explodeModel(transform.object, value)
})
export { transform }

export const modelthree = ref(new Array<TreeNode>())
export function refreshThree() {
    if (!director) return
    modelthree.value = director.generateTreeData()
}