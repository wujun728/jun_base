import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
import { EffectComposer } from 'three/examples/jsm/postprocessing/EffectComposer.js';
import * as THREE from 'three'
import { RenderPass } from 'three/examples/jsm/postprocessing/RenderPass.js';
import { OutputPass } from 'three/examples/jsm/postprocessing/OutputPass.js';
import Stats from 'three/examples/jsm/libs/stats.module.js'
import { SelectControls } from './SelectControls';
import CapsControls from './CapsControls';
import { DragControls } from 'three/examples/jsm/controls/DragControls.js';
import { TransformControls } from 'three/examples/jsm/controls/TransformControls.js'
import { RoomEnvironment } from 'three/examples/jsm/environments/RoomEnvironment.js'

interface DirectorOption {
    /** canvas DOM */
    canvas?: HTMLCanvasElement | THREE.OffscreenCanvas,
    /** 宽度 */
    width: number,
    /** 高度 */
    height: number,
    /** 渲染之后 */
    afterRender?: Function,
    FPS?: number
}
export interface TreeNode {
    label: string;
    id: string;
    children: TreeNode[];
    type: string
}

export class Director {
    /** 场景 */
    scene = new THREE.Scene()
    /** 相机 */
    camera = new THREE.PerspectiveCamera()
    /** 时钟 */
    clock = new THREE.Clock()
    /** 渲染器 */
    renderer: THREE.WebGLRenderer
    /** 轨道控制器 */
    controls: {
        orbitControls: THREE.Controls<{}>
        selectControls: SelectControls
        capsControls: CapsControls
        dragControls: DragControls
        transformControls: TransformControls
    }
    /** 渲染组合器 */
    composer: EffectComposer
    /** FPS */
    wait: number = 0
    /** 环境光 */
    ambientLight: THREE.AmbientLight = new THREE.AmbientLight(0xffffff, 2)
    /** 半球光 */
    hemisphereLight: THREE.HemisphereLight = new THREE.HemisphereLight(0xffffff, 0x444444, 0.6)
    /** 方向光 */
    directionalLight: THREE.DirectionalLight = new THREE.DirectionalLight(0xffffff, 1.2)
    /** 辅助坐标 */
    axesHelper: THREE.AxesHelper = new THREE.AxesHelper(100)
    showAxesHelper: boolean = false
    /** 辅助网格 */
    gridHelper: THREE.GridHelper = new THREE.GridHelper(100, 100)
    showGridHelper: boolean = false
    /** 状态监控 */
    stats: Stats = new Stats()
    /** 窗口大小 */
    width: number
    height: number

    get FPS() {
        return this.wait
    }
    set FPS(fps: number) {
        this.wait = 1 / fps
    }
    constructor(options: DirectorOption) {
        this.width = options.width
        this.height = options.height
        this.camera.position.z = 10
        this.camera.position.y = 2
        this.scene.add(this.ambientLight)
        this.hemisphereLight.position.set(0, 10, 0)
        this.scene.add(this.hemisphereLight)
        this.directionalLight.position.set(10, 20, 10)
        this.directionalLight.target.position.set(0, 0, 0)
        this.scene.add(this.directionalLight)
        this.scene.add(this.directionalLight.target)
        this.renderer = new THREE.WebGLRenderer({
            antialias: true,
            alpha: true,
            canvas: options.canvas
        });
        this.renderer.setSize(options.width, options.height)
        this.renderer.setClearColor(0xffffff);
        this.renderer.setPixelRatio(window.devicePixelRatio);
        this.renderer.outputColorSpace = THREE.SRGBColorSpace;
        this.renderer.toneMapping = THREE.ACESFilmicToneMapping;
        this.renderer.toneMappingExposure = 1.0;

        const pmremGenerator = new THREE.PMREMGenerator(this.renderer);
        const env = new RoomEnvironment();
        this.scene.environment = pmremGenerator.fromScene(env).texture;
        pmremGenerator.dispose();

        const renderPass = new RenderPass(this.scene, this.camera);
        const orbitControls = new OrbitControls(this.camera, this.renderer.domElement)
        this.composer = new EffectComposer(this.renderer);
        const selectControls = new SelectControls(this.scene, this.camera, this.renderer.domElement)
        const capsControls = new CapsControls(this.scene, this.camera, this.renderer, orbitControls)
        capsControls.enabled = false
        const dragControls = new DragControls([], this.camera, this.renderer.domElement)
        dragControls.addEventListener('dragstart', () => this.controls.orbitControls.enabled = false)
        dragControls.addEventListener('dragend', () => this.controls.orbitControls.enabled = true)
        const transformControls = new TransformControls(this.camera, this.renderer.domElement);
        transformControls.addEventListener('dragging-changed', function (event) {
            orbitControls.enabled = !event.value;
        })
        this.controls = { orbitControls, selectControls, capsControls, dragControls, transformControls }
        this.composer.addPass(renderPass);
        this.composer.addPass(selectControls.outlinePass);
        this.composer.addPass(new OutputPass());
        this.FPS = options.FPS || 30
        this.renderer.setPixelRatio(window.devicePixelRatio);
        this.renderer.setSize(window.innerWidth, window.innerHeight);
        this.renderer.setClearColor(0xffffff);
        this.renderer.autoClear = false;

        this.stats.dom.style.position = 'absolute'
        this.stats.dom.style.left = ''
        this.stats.dom.style.right = '0px'

        const onWindowResize = () => {
            this.width = window.innerWidth
            this.height = window.innerHeight
            this.renderer.setSize(this.width, this.height)
            this.renderer.setPixelRatio(window.devicePixelRatio)
            this.composer.setSize(this.width, this.height)
            this.composer.setPixelRatio(window.devicePixelRatio)
            this.camera.aspect = this.width / this.height
            this.camera.updateProjectionMatrix()
        }
        window.addEventListener('resize', onWindowResize, false);
    }
    startRender(onRander?: (FPS: number) => void) {
        let timeS = 0;
        let realFPS = 0;
        const animate = () => {
            let T = this.clock.getDelta();
            timeS = timeS + T;
            if (timeS >= this.wait) {
                if (onRander) onRander(realFPS)
                this.renderer.clear();
                this.stats.update()
                Object.values(this.controls).forEach(controls => controls.update(T))
                this.composer.render();
                realFPS = 1 / timeS
                timeS = 0;
            }
            requestAnimationFrame(animate)
        }
        animate()

    }
    switchAxesHelper(show: boolean) {
        if (show && !this.showAxesHelper) {
            this.scene.add(this.axesHelper)
            this.showAxesHelper = false
        } else {
            this.scene.remove(this.axesHelper)
        }
    }
    switchGridHelper(show: boolean) {
        if (show && !this.showGridHelper) {
            this.scene.add(this.gridHelper)
            this.showGridHelper = true
        } else {
            this.scene.remove(this.gridHelper)
            this.showGridHelper = false
        }
    }
    switchStats(show: boolean) {
        if (show) {
            this.renderer.domElement.parentElement!.appendChild(this.stats.dom)
        } else {
            this.renderer.domElement.parentElement!.removeChild(this.stats.dom)
        }
    }
    generateTreeData() {
        function _generateTreeData(node: THREE.Object3D<THREE.Object3DEventMap>, parent: TreeNode | null = null) {
            const treeItem = {
                label: node.name || node.type,
                id: `${parent ? parent.id + '.' : ''}${node.uuid}`, // 使用唯一标识符作为id
                type: node.type,
                children: new Array<TreeNode>()
            }
            if (node.children && node.children.length > 0) {
                for (let child of node.children) {
                    treeItem.children.push(_generateTreeData(child, treeItem));
                }
            }
            return treeItem;
        }
        return _generateTreeData(this.scene).children
    }
    getObjectByUUID(uuidPath: string) {
        const uuids = uuidPath.split('.');
        let currentObject: THREE.Object3D = this.scene;
        for (let i = 1; i < uuids.length; i++) {
            const uuid = uuids[i];
            let found = null;
            if (currentObject.children) {
                for (let j = 0; j < currentObject.children.length; j++) {
                    const child = currentObject.children[j];
                    if (child.uuid === uuid) {
                        found = child;
                        break;
                    }
                }
            }
            if (!found) {
                return null;
            }
            currentObject = found;
        }
        return currentObject;
    }
    getObjectBySingleUUID(uuid: string): THREE.Object3D | null {
        const traverseScene = (node: THREE.Object3D): THREE.Object3D | null => {
            if (node.uuid === uuid) {
                return node;
            }
            for (let i = 0; i < node.children.length; i++) {
                const found = traverseScene(node.children[i]);
                if (found) {
                    return found;
                }
            }
            return null;
        };

        return traverseScene(this.scene);
    }
}