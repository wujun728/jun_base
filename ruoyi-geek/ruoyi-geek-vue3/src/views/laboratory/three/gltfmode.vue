<script setup name="Index" lang="ts">
import ModelPanel from './ModelPanel.vue'
import ThreePanel from './ThreePanel.vue'
import Panel from './Panel.vue';
import SelectedPanel from './SelectedPanel.vue';
import OperatePanel from './OperatePanel.vue';
import * as THREE from 'three'
import { TreeNode } from './three-plus/ThreeHelper'
import { onMounted, ref, shallowRef, watch } from 'vue';
import director, { modelthree, refreshThree, selectNode, selectObject, initDirector } from './director'
const handleNodeClick = (node: TreeNode) => {
    if (!director?.scene) return
    const obj = director.getObjectByUUID(node.id)
    if (!obj) return
    selectNode(obj)
}
const selected = shallowRef()
watch(selected, () => {
    if (!director) return
    if (director.controls.capsControls.enabled) return
    director.controls.dragControls.enabled = true
    director.controls.dragControls.objects = director.controls.selectControls.outlinePass.selectedObjects
})

const ThreeContainerRef = ref<HTMLElement | null>(null)
onMounted(() => {
    if (ThreeContainerRef.value == null) return
    // 获取ThreeContainerRef的宽高
    initDirector(ThreeContainerRef.value)
    director.controls.selectControls.onSelect = (obj: THREE.Object3D, event: MouseEvent) => {
        selectObject(obj)
        const label = document.createElement('div');
        label.textContent = obj.name;
        label.style.position = 'absolute';
        label.style.top = event.clientY + 'px';
        label.style.left = event.clientX + 'px';
        label.style.color = 'white';
        label.style.backgroundColor = 'rgba(0, 0, 0, 0.5)';
        label.style.padding = '5px';
        label.style.borderRadius = '5px';
        document.body.appendChild(label);
        setTimeout(() => document.body.removeChild(label), 5000);
    }
})
</script>

<template>
    <div class="three-container" ref="ThreeContainerRef" id="showCaps" style="position: relative;">
        <Panel style="left: 10px;top: 10px;width: 250px;">
            <OperatePanel />
        </Panel>
        <Panel style="right: 10px;top: 50px;width: 280px;">
            <SelectedPanel />
        </Panel>
        <Panel style="left: 10px;top: 250px;width: 280px;">
            <ThreePanel :modelthree="modelthree" @handleNodeClick="handleNodeClick" @refresh="refreshThree" />
        </Panel>
        <Panel style="bottom: 10px;">
            <ModelPanel />
        </Panel>
    </div>
</template>
<style scoped lang="scss">
.three-container {
    width: 100%;
    height: 100%;
}
</style>